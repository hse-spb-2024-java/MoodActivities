package org.hse.moodactivities.fragments

import FitnessViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.managers.FitnessDataManager
import org.hse.moodactivities.models.AuthType
import org.hse.moodactivities.models.GoogleFitRepositoryImpl
import org.hse.moodactivities.viewmodels.FitnessViewModelFactory
import org.hse.moodactivities.viewmodels.UserViewModel
import org.hse.moodactivities.services.ThemesService

class ProfileScreenFragment : Fragment() {
    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)

        setColorTheme(view)

        val googleFitRepository = GoogleFitRepositoryImpl(requireContext())
        val fitnessDataManager = FitnessDataManager(googleFitRepository)

        fitnessViewModel = ViewModelProvider(this, FitnessViewModelFactory(fitnessDataManager)).get(FitnessViewModel::class.java)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        fitnessViewModel.loadFitnessData()
        fitnessViewModel.fitnessData.observe(viewLifecycleOwner) { data ->
            Log.d("FitnessIntegration", data.steps.toString())
        }
//        userViewModel.user.observe(viewLifecycleOwner) { user ->
//            if (user.authType == AuthType.PLAIN) {
//                R.id.profile_steps_count
//            }
//        }

        return view
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        view.findViewById<ConstraintLayout>(R.id.layout)
            .setBackgroundColor(colorTheme.getBackgroundColor())

        view.findViewById<TextView>(R.id.title).setTextColor(colorTheme.getFontColor())
    }
}
