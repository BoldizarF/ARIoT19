package com.aptiv.watchdogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.data.RepositoryFactory
import com.aptiv.watchdogapp.data.health.HealthRepository
import com.aptiv.watchdogapp.data.image.ImageRepository
import com.aptiv.watchdogapp.recyclerview.RecyclerAdapter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val bgContext = Default

    private lateinit var graphView: GraphView

    private lateinit var healthRepository: HealthRepository
    private lateinit var imageRepository: ImageRepository
    private lateinit var adapter : RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupGraph()
        setupReycleView()

        healthRepository = RepositoryFactory.createHealthRepository(applicationContext)
        imageRepository = RepositoryFactory.createImagesRepository(applicationContext)

        retrieveHeartRateData()
        retrieveImages()
    }

    private fun setupReycleView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter
    }

    private fun setupGraph() {
        // http://www.android-graphview.org/simple-graph/
        graphView = findViewById(R.id.graph)
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
}
