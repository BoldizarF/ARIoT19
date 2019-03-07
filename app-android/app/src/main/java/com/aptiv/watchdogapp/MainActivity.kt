package com.aptiv.watchdogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.health.HealthRepository
import com.aptiv.watchdogapp.health.HealthRepositoryFactory
import com.aptiv.watchdogapp.recyclerview.RecyclerAdapter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView

class MainActivity : AppCompatActivity() {

    private lateinit var healthRepository: HealthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)

        recyclerView.layoutManager = GridLayoutManager(this, 4)

        healthRepository = HealthRepositoryFactory.create(this.applicationContext)

        addDummyData()
    }

    // http://www.android-graphview.org/simple-graph/
    private fun addDummyData() {
        val graph = findViewById<GraphView>(R.id.graph)

        val series = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )

        graph.addSeries(series)
    }
}
