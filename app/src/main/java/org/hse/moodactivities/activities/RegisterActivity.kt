package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse
import org.hse.moodactivities.databinding.ActivityRegisterBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.AuthViewModel
import org.hse.moodactivities.viewmodels.UserViewModel
import org.hse.moodactivities.models.AuthType


class RegisterActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // return to welcome activity button
        binding.returnButton.setOnClickListener {
            this.finish()
        }

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
                    applicationContext, registrationResponse.token, AuthType.PLAIN
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

    fun openTermsAndConditions(view: View) {
        val intent = Intent(this, TermsAndConditionsActivity::class.java)
        startActivity(intent)
    }

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set background color
        binding.layout.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to return button
        binding.returnImage.setColorFilter(colorTheme.getFontColor())

        // set title color
        binding.signupHeader.setTextColor(colorTheme.getFontColor())

        // set color to app name
        binding.appName.setTextColor(colorTheme.getFontColor())

        // set color to text and conditions
        binding.agree.setTextColor(colorTheme.getFontColor())
        binding.termsAndConditions.setTextColor(colorTheme.getButtonColor())

        // set check box color
        binding.termsCheckbox.buttonTintList = ColorStateList.valueOf(colorTheme.getButtonColor())

        // set colors to inputs
        binding.usernameInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.usernameInput.setTextColor(colorTheme.getInputTextColor())
        binding.usernameInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.usernameImage.setColorFilter(colorTheme.getInputHintTextColor())

        binding.emailInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.emailInput.setTextColor(colorTheme.getInputTextColor())
        binding.emailInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.emailImage.setColorFilter(colorTheme.getInputHintTextColor())

        binding.passwordInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.passwordInput.setTextColor(colorTheme.getInputTextColor())
        binding.passwordInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.passwordImage.setColorFilter(colorTheme.getInputHintTextColor())

        binding.passwordConfirmInputBackground.setCardBackgroundColor(colorTheme.getInputBackgroundColor())
        binding.passwordConfirmInput.setTextColor(colorTheme.getInputTextColor())
        binding.passwordConfirmInput.setHintTextColor(colorTheme.getInputHintTextColor())
        binding.passwordConfirmImage.setColorFilter(colorTheme.getInputHintTextColor())
        // todo: set colors to cursors

        // set register button color
        binding.registerButtonBackground.setCardBackgroundColor(colorTheme.getButtonColor())
        binding.registerText.setTextColor(colorTheme.getDimmedBackgroundColor())

        // set color to redirect text
        binding.haveAnAccount.setTextColor(colorTheme.getFontColor())
        binding.loginRedirectText.setTextColor(colorTheme.getButtonColor())
    }
}
