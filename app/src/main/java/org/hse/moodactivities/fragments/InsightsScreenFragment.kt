package org.hse.moodactivities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hse.moodactivities.R
import io.data2viz.color.Colors
import io.data2viz.geom.size
import io.data2viz.scale.Scales
import io.data2viz.viz.*

class InsightsScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_insights_screen, container, false)
        return viz.toView(this.requireContext())
    }
}


const val vizSize = 500.0

val data = listOf(4, 8, 15, 16, 23, 42)
const val barHeight = 14.0
const val padding = 2.0


val xScale = Scales.Continuous.linear {
    domain = listOf(.0, data.max().toDouble())
    range = listOf(.0, vizSize - 2 * padding)
}

val viz : Viz = viz {
    size = size(vizSize, vizSize)
    data.forEachIndexed { index, datum ->
        group {
            transform {
                translate(
                    x = padding,
                    y = padding + index * (padding + barHeight))
            }
            rect {
                width = xScale(datum)
                height = barHeight
                fill = Colors.Web.indianred
            }
            text {
                textContent = datum.toString()
                hAlign = TextHAlign.RIGHT
                vAlign = TextVAlign.HANGING
                x = xScale(datum) - 2.0
                y = 1.5
                textColor = Colors.Web.white
                fontSize = 10.0
            }
        }
    }
}