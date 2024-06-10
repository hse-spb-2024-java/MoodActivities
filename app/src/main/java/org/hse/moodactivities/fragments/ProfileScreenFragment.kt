package org.hse.moodactivities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.services.ThemesService

class ProfileScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)

        setColorTheme(view)

        return view
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        view.findViewById<ConstraintLayout>(R.id.layout)
            .setBackgroundColor(colorTheme.getBackgroundColor())

        view.findViewById<TextView>(R.id.title).setTextColor(colorTheme.getFontColor())
    }
}
