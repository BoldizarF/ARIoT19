package com.aptiv.watchdogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.data.RepositoryFactory
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
import android.graphics.Color
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.aptiv.watchdogapp.data.attack.AttackManager
import com.aptiv.watchdogapp.data.image.CapturedImage
import com.aptiv.watchdogapp.util.DateHelper
import com.aptiv.watchdogapp.util.loadFromBase64
import com.jjoe64.graphview.DefaultLabelFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val bgContext = Default

    private lateinit var graphView: GraphView
    private lateinit var adapter : ImageAdapter

    private lateinit var attackManager: AttackManager
    private lateinit var healthRepository: HealthRepository
    private lateinit var imageRepository: ImageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "WatchDog - Dashboard"

        setupGraph()
        setupReycleView()
        bindControlButtons()

        attackManager = RepositoryFactory.createAttackManager()
        healthRepository = RepositoryFactory.createHealthRepository(applicationContext)
        imageRepository = RepositoryFactory.createImagesRepository(applicationContext)
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
        val dialog = Dialog(this)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_image, null)
        dialogView.findViewById<ImageView>(R.id.dialog_image).loadFromBase64(item.value)
        dialogView.findViewById<Button>(R.id.close_btn).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.delete_btn).setOnClickListener {
            deleteImage(item.timestamp)
            dialog.dismiss()
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun setupGraph() {
        // http://www.android-graphview.org/simple-graph/
        graphView = findViewById(R.id.graph)

        graphView.viewport.isScalable = true
        graphView.viewport.setMinY(20.0)
        graphView.viewport.setMaxY(220.0)
        graphView.viewport.isYAxisBoundsManual = true

        val calendar = GregorianCalendar()
        calendar.time = Date()

        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val endTime = calendar.time

        calendar.add(Calendar.HOUR_OF_DAY, -8)
        val startTime = calendar.time

        graphView.viewport.setMinX(startTime.time.toDouble())
        graphView.viewport.setMaxX(endTime.time.toDouble())
        graphView.viewport.isXAxisBoundsManual = true

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
        retrieveHeartRateData()
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

    private fun retrieveHeartRateData() {
        uiScope.launch {
            val result = withContext(bgContext) {
                healthRepository.getValues()
            }

            if (result.isEmpty()) return@launch

            if (graphView.series.size > 0) {
                val latestValue = graphView.series.first().highestValueX
                if (result.last().timestamp.toDouble() == latestValue) {
                    return@launch
                }
            }

            graphView.removeAllSeries()

            val heartRateDataPoints = result.map {
                DataPoint(it.timestamp.toDouble(), it.heartRate.toDouble())
            }.toTypedArray()

            val heartRateSeries = LineGraphSeries(heartRateDataPoints).apply {
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
                color = Color.GREEN
                isDrawDataPoints = true
                dataPointsRadius = 10f
                thickness = 8
                setAnimated(true)
            }

            graphView.addSeries(heartRateSeries)
            graphView.addSeries(temperatureSeries)
        }
    }

    private fun retrieveImages() {
        uiScope.launch {
            val result = withContext(bgContext) {
                imageRepository.getValues()
            }

            if (result.isEmpty()) return@launch

            adapter.addImages(result)
        }
    }

    private fun deleteAllHeartRateValues() {
        uiScope.launch {
            val result = withContext(bgContext) {
                healthRepository.clearAll()
            }

            val msg = if (result) "Successfully cleared heart rates" else "Unable to clear heart rates"
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
        findViewById<Button>(R.id.clear_btn).setOnClickListener {
            deleteAllHeartRateValues()
        }
        findViewById<Button>(R.id.refresh_btn).setOnClickListener {
            refreshData()
        }
        findViewById<Button>(R.id.signout_btn).setOnClickListener {
            finish()
        }
    }

    private fun schedulePolling() {
        val scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler.scheduleAtFixedRate({
            refreshData()
        }, 0, 10, TimeUnit.SECONDS)
    }
}
