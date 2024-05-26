package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse
import org.hse.moodactivities.databinding.ActivityRegisterBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.AuthViewModel
import org.hse.moodactivities.viewmodels.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginRedirectButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmation = binding.passwordConfirmInput.text.toString()
            val email = binding.emailInput.text.toString()
            val agreedTerms = binding.termsCheckbox.isChecked

            if (!agreedTerms) {
                Log.i("RegistrationResponse", "Did not agreed on terms")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please agree with the ToS if you want to register",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password != confirmation) {
                Log.d("RegistrationResponse", "Passwords do not match")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Passwords do not match",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Log.d("RegistrationResponse", "Password cannot be empty");
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Password cannot be empty",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Log.d("RegistrationResponse", "Email cannot be empty");
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Email cannot be empty",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val registrationResponseLiveData = authViewModel.register(
                username, email, password
            );

            authViewModel.errorMessage.observe(this) {
                if (it != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG
                    ).show()
                    authViewModel.clearErrorMessage()
                }
            }

            registrationResponseLiveData.observe(this) { registrationResponse ->
                if (registrationResponse.responseType == RegistrationResponse.ResponseType.ERROR) {
                    Log.d("RegistrationResponse", registrationResponse.message)
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        registrationResponse.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@observe
                }
                userViewModel.updateUserFromJwt(
                    applicationContext, registrationResponse.token
                )

                authViewModel.saveToken(
                    getSharedPreferences("userPreferences", Context.MODE_PRIVATE),
                    registrationResponse.token
                )

                userViewModel.user.observe(this) { user ->
                    Log.d("RegistrationResponse", user.id.toString())
                }
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }

        setColorTheme()
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set background color
        binding.registerScreenBackground.setBackgroundColor(ThemesService.getBackgroundColor())

        // set input fields colors
        binding.usernameInputBackground.setCardBackgroundColor(ThemesService.getColor3())
        binding.emailInputBackground.setCardBackgroundColor(ThemesService.getColor3())
        binding.passwordInputBackground.setCardBackgroundColor(ThemesService.getColor3())
        binding.passwordConfirmInputBackground.setCardBackgroundColor(ThemesService.getColor3())

        // set register button color
        binding.registerButtonBackground.setCardBackgroundColor(ThemesService.getColor4())

        binding.loginRedirectText.setTextColor(ThemesService.getColor4())
        binding.registerText.setTextColor(ThemesService.getDimmedBackgroundColor())
    }
}
