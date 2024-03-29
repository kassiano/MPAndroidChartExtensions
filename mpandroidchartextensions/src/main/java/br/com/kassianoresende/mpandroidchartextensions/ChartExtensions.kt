package br.com.kassianoresende.mpandroidchartextensions

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart


fun CombinedChart.setBarRadius(radius:Int = 20){

    val barChatRender = CustomCombinedChartRenderer(this, this.animator, this.viewPortHandler).apply {
        barChartRadius = radius
    }

    this.apply {
        renderer = barChatRender
    }
}


fun BarChart.setBarRadius(radius:Int = 20){

    val barChatRender = CustomBarChartRender(this, this.animator, this.viewPortHandler).apply {
        setRadius(radius)
    }

    this.apply {
        renderer = barChatRender
    }
}
