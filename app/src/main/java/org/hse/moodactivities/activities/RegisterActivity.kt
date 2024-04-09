package org.hse.moodactivities.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse
import org.hse.moodactivities.databinding.ActivityRegisterBinding
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

        binding.tvLoginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmation = binding.etPasswordConfirm.text.toString()
            val agreedTerms = binding.cmTerms.isActivated()

            if (!agreedTerms) {
                Log.d("RegistrationResponse", "Did not agreed on terms")
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

            val registrationResponseLiveData = authViewModel.register(username, password);

            authViewModel.errorMessage.observe(this) {
                if (it != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        it,
                        Snackbar.LENGTH_LONG
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
                    applicationContext,
                    registrationResponse.token
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
            }
        }
    }
}
