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
import android.app.PendingIntent.getActivity
import android.view.Window
import android.widget.ImageView
import com.aptiv.watchdogapp.data.image.CapturedImage
import com.aptiv.watchdogapp.util.loadFromBase64
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter


class MainActivity : AppCompatActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val bgContext = Default

    private lateinit var graphView: GraphView
    private lateinit var adapter : ImageAdapter

    private lateinit var healthRepository: HealthRepository
    private lateinit var imageRepository: ImageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "WatchDog - Dashboard"

        setupGraph()
        setupReycleView()
        bindControlButtons()

        healthRepository = RepositoryFactory.createHealthRepository(applicationContext)
        imageRepository = RepositoryFactory.createImagesRepository(applicationContext)

        refreshData()
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
        graphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
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

            val dataPoints = result.mapIndexed { index, heartRateValue ->
                DataPoint(index.toDouble(), heartRateValue.value.toDouble())
            }.toTypedArray()

            graphView.addSeries(LineGraphSeries(dataPoints))
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

    private fun bindControlButtons() {
        findViewById<Button>(R.id.refresh_btn).setOnClickListener {
            refreshData()
        }
        findViewById<Button>(R.id.signout_btn).setOnClickListener {
            finish()
        }
    }
}
