package org.hse.moodactivities.fragments

import GoogleSignInManager
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.hse.moodactivities.R
import org.hse.moodactivities.models.AuthType
import org.hse.moodactivities.viewmodels.UserViewModel
import org.hse.moodactivities.services.ThemesService

class ProfileScreenFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    companion object {
        const val NOT_CONNECTED = "Not connected."
        const val CONNECTED = "Connected."
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)

        setColorTheme(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val googleConnectionTextView = view.findViewById<TextView>(R.id.google_device_connection_status)
        val googleConnectionWidget = view.findViewById<androidx.cardview.widget.CardView>(R.id.google_connection_widget)
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) == null) {
            googleConnectionWidget.setOnClickListener {
                performGoogleSignIn()
            }
            googleConnectionTextView.text = NOT_CONNECTED
        } else {
            // Only allow Google logout on PLAIN accounts!
            if (userViewModel.getUser(requireContext())!!.authType == AuthType.PLAIN) {
                googleConnectionWidget.setOnClickListener {
                    performGoogleLogOut()
                }
            }
            googleConnectionTextView.text = CONNECTED
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleSignInManager.RETURN_CODE_SIGN_IN) {
            // Reload current fragment
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
        }
    }
    private fun performGoogleSignIn() {
        val signInIntent = GoogleSignInManager.getSignInIntent()
        startActivityForResult(signInIntent, GoogleSignInManager.RETURN_CODE_SIGN_IN)
    }

    private fun performGoogleLogOut() {
        GoogleSignInManager.signOut(requireContext()) {
            // Reload current fragment
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
        }
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        view.findViewById<ConstraintLayout>(R.id.layout)
            .setBackgroundColor(colorTheme.getBackgroundColor())

        view.findViewById<TextView>(R.id.title).setTextColor(colorTheme.getFontColor())
    }
}
