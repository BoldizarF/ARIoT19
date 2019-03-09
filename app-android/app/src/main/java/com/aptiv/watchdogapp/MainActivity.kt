package com.aptiv.watchdogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.data.Factory
import com.aptiv.watchdogapp.data.health.HealthRepository
import com.aptiv.watchdogapp.data.image.ImageRepository
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.aptiv.watchdogapp.data.attack.AttackManager
import com.aptiv.watchdogapp.data.image.CapturedImage
import com.aptiv.watchdogapp.util.DateHelper
import com.aptiv.watchdogapp.util.loadFromBase64
import com.aptiv.watchdogapp.util.toggleVisibility
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.LegendRenderer
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.util.Log


class MainActivity : AppCompatActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val bgContext = Default

    private lateinit var graphView: GraphView
    private lateinit var adapter : ImageAdapter

    private lateinit var attackManager: AttackManager
    private lateinit var medicalManager: MedicalAssistManager

    private lateinit var healthRepository: HealthRepository
    private lateinit var imageRepository: ImageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGraph()
        setupReycleView()
        bindControlButtons()

        medicalManager = Factory.createMedicalManager()
        attackManager = Factory.createAttackManager()
        healthRepository = Factory.createHealthRepository(applicationContext)
        imageRepository = Factory.createImagesRepository(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        schedulePolling()
    }

    private fun setupReycleView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = ImageAdapter(::onImageClicked)
        recyclerView.adapter = adapter
    }

    private fun onImageClicked(item: CapturedImage) {
        onImageClicked(item, false)
    }

    private fun onImageClicked(item: CapturedImage, isNewImage: Boolean) {
        if (isNewImage) {
            playSound()
        }

        val dialog = Dialog(this@MainActivity)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_image, null)

        dialogView.findViewById<ImageView>(R.id.attack_txt).toggleVisibility(isNewImage)
        dialogView.findViewById<ImageView>(R.id.info_txt).toggleVisibility(isNewImage)
        dialogView.findViewById<ImageView>(R.id.dialog_image).loadFromBase64(item.value)

        dialogView.findViewById<Button>(R.id.attackdialog_btn).apply {
            toggleVisibility(isNewImage)
            setOnClickListener {
                launchAttack()
                dialog.dismiss()
            }
        }

        dialogView.findViewById<Button>(R.id.close_btn).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.delete_btn).apply {
            toggleVisibility(!isNewImage)
            setOnClickListener {
                deleteImage(item.timestamp)
                dialog.dismiss()
            }
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun setupGraph() {
        // http://www.android-graphview.org/simple-graph/
        graphView = findViewById(R.id.graph)

        graphView.viewport.isScalable = true
        graphView.viewport.setMinY(20.0)
        graphView.viewport.setMaxY(180.0)
        graphView.viewport.isYAxisBoundsManual = true
        graphView.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        val calendar = GregorianCalendar()
        calendar.time = Date()

        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val endTime = calendar.time

        calendar.add(Calendar.HOUR_OF_DAY, -6)
        val startTime = calendar.time

        graphView.viewport.setMinX(startTime.time.toDouble())
        graphView.viewport.setMaxX(endTime.time.toDouble())
        graphView.viewport.isXAxisBoundsManual = true

        graphView.gridLabelRenderer.numHorizontalLabels = 4
        graphView.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    DateHelper.formatTimestamp(value.toLong())
                } else {
                    value.toLong().toString()
                }
            }
        }
    }

    private fun refreshData() {
        retrieveHealthData()
        retrieveImages()
    }

    private fun deleteImage(timestamp: Long) {
        uiScope.launch {
            val result = withContext(bgContext) {
                imageRepository.deleteImage(timestamp)
            }

            if (result) {
                adapter.removeImage(timestamp)
            }
        }
    }

    private fun deleteAllImages() {
        uiScope.launch {
            val result = withContext(bgContext) {
                imageRepository.deleteAll()
            }

            if (result) {
                adapter.clear()
            }
        }
    }

    private fun retrieveHealthData() {
        uiScope.launch {
            val result = withContext(bgContext) {
                healthRepository.getValues(getApiKey())
            }

            if (result.isEmpty()) return@launch

            if (graphView.series.size > 0) {
                val latestValue = graphView.series.first().highestValueX
                if (result.last().timestamp.toDouble() == latestValue) {
                    return@launch
                }
            }

            medicalManager.checkValues(this@MainActivity, result)

            graphView.removeAllSeries()

            val heartRateDataPoints = result.map {
                DataPoint(it.timestamp.toDouble(), it.heartRate.toDouble())
            }.toTypedArray()

            val heartRateSeries = LineGraphSeries(heartRateDataPoints).apply {
                title = "Heart Rate"
                isDrawBackground = true
                isDrawDataPoints = true
                dataPointsRadius = 10f
                thickness = 8
                setAnimated(true)
            }

            val temperatureDataPoints = result.map {
                DataPoint(it.timestamp.toDouble(), it.temperature)
            }.toTypedArray()

            val temperatureSeries = LineGraphSeries(temperatureDataPoints).apply {
                title = "Body Temperature"
                color = Color.RED
                isDrawDataPoints = true
                dataPointsRadius = 10f
                thickness = 8
                setAnimated(true)
            }

            graphView.addSeries(heartRateSeries)
            graphView.addSeries(temperatureSeries)

            graphView.legendRenderer.isVisible = true
        }
    }

    private fun retrieveImages() {
        uiScope.launch {
            val result = withContext(bgContext) {
                imageRepository.getValues(getApiKey())
            }

            if (result.isEmpty()) return@launch

            val latestImage = adapter.getImages().firstOrNull()
            if (latestImage != null && latestImage.timestamp != result.firstOrNull()?.timestamp) {
                onImageClicked(result.first(), true)
            }

            adapter.addImages(result)
        }
    }

    private fun deleteAllHealthValues() {
        uiScope.launch {
            val result = withContext(bgContext) {
                healthRepository.clearAll()
            }

            if (result) {
                graphView.removeAllSeries()
                graphView.legendRenderer.isVisible = false
            }

            val msg = if (result) "Successfully cleared health values" else "No health values were cleared."
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchAttack() {
        uiScope.launch {
            val result = withContext(bgContext) {
                attackManager.attack()
            }

            if (!result) {
                Toast.makeText(this@MainActivity, "Unable to send attack request", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindControlButtons() {
        findViewById<Button>(R.id.attack_btn).setOnClickListener {
            launchAttack()
        }
        findViewById<Button>(R.id.clear_images_btn).setOnClickListener {
            deleteAllImages()
        }
        findViewById<Button>(R.id.clear_btn).setOnClickListener {
            deleteAllHealthValues()
        }
        findViewById<Button>(R.id.refresh_btn).setOnClickListener {
            refreshData()
        }
        findViewById<Button>(R.id.signout_btn).setOnClickListener {
            clearApiKey()
            finish()
        }
    }

    private fun schedulePolling() {
        val scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler.scheduleAtFixedRate({
            try {
                refreshData()
            } catch (ex: Exception) {
                Log.e("MainActivity", "RefreshData crashed", ex)
            }
        }, 0, 10, TimeUnit.SECONDS)
    }

    private fun getApiKey(): String {
        val sharedPref = getSharedPreferences("com.aptiv", Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString("API_KEY", "")!!
    }

    private fun clearApiKey() {
        val sharedPref = getSharedPreferences("com.aptiv", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("API_KEY", "")
            commit()
        }
    }

    private fun playSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            Log.e("MainActivity", "Unable to play sound", e)
        }
    }
}
