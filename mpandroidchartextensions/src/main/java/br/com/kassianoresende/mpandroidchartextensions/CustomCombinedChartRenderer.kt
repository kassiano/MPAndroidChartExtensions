package br.com.kassianoresende.mpandroidchartextensions

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.renderer.*
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomCombinedChartRenderer(chart: CombinedChart, animator: ChartAnimator, viewPortHandler: ViewPortHandler) :
    CombinedChartRenderer(chart, animator, viewPortHandler) {

    init {
        createRenderers()
    }

    var barChartRadius = 0

    /**
     * Creates the renderers needed for this combined-renderer in the required order. Also takes the DrawOrder into
     * consideration.
     */
    override fun createRenderers() {

        mRenderers.clear()

        val chart = mChart.get() as CombinedChart ?: return

        val orders = chart.drawOrder

        for (order in orders) {

            when (order) {
                CombinedChart.DrawOrder.BAR -> if (chart.barData != null)
                    mRenderers.add(CustomBarChartRender(chart, mAnimator, mViewPortHandler).apply {
                        setRadius(barChartRadius)
                    })
                CombinedChart.DrawOrder.BUBBLE -> if (chart.bubbleData != null)
                    mRenderers.add(CustomBubbleChartRenderer(chart, mAnimator, mViewPortHandler))
                CombinedChart.DrawOrder.LINE -> if (chart.lineData != null)
                    mRenderers.add(CustomLineChartRenderer(chart, mAnimator, mViewPortHandler))
                CombinedChart.DrawOrder.CANDLE -> if (chart.candleData != null)
                    mRenderers.add(CustomCandleStickChartRenderer(chart, mAnimator, mViewPortHandler))
                CombinedChart.DrawOrder.SCATTER -> if (chart.scatterData != null)
                    mRenderers.add(CustomScatterChartRenderer(chart, mAnimator, mViewPortHandler))
                else->{ }
            }
        }
    }


    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {

        val chart = mChart.get() ?: return

        for (renderer in mRenderers) {

            val data = when(renderer){
                is CustomBarChartRender -> renderer.chart.barData
                is CustomLineChartRenderer -> renderer.chart.lineData
                is CustomCandleStickChartRenderer -> renderer.chart.candleData
                is CustomScatterChartRenderer -> renderer.chart.scatterData
                is CustomBubbleChartRenderer -> renderer.chart.bubbleData
                else -> null
            }

            val dataIndex = if (data == null)
                -1
            else
                (chart.data as CombinedData).allData.indexOf(data)

            mHighlightBuffer.clear()

            for (h in indices) {
                if (h.dataIndex == dataIndex || h.dataIndex == -1)
                    mHighlightBuffer.add(h)
            }

            renderer.drawHighlighted(c, mHighlightBuffer.toTypedArray())
        }
    }

}


class CustomLineChartRenderer(val chart: LineDataProvider,
                              animator: ChartAnimator,
                              viewPortHandler: ViewPortHandler
) : LineChartRenderer(chart, animator,viewPortHandler)


class CustomCandleStickChartRenderer(val chart: CandleDataProvider,
                                     animator: ChartAnimator,
                                     viewPortHandler: ViewPortHandler
) : CandleStickChartRenderer(chart, animator, viewPortHandler)

class CustomScatterChartRenderer(val chart: ScatterDataProvider,
                                 animator: ChartAnimator,
                                 viewPortHandler: ViewPortHandler
) : ScatterChartRenderer(chart, animator, viewPortHandler)

class CustomBubbleChartRenderer(val chart: BubbleDataProvider,
                                animator: ChartAnimator,
                                viewPortHandler: ViewPortHandler
) : BubbleChartRenderer(chart, animator, viewPortHandler)