package org.hse.moodactivities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.buttonRegister.setOnClickListener {
            val username = binding.etRegisterUsername.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmation = binding.etRegisterPasswordConfirm.text.toString()

            if (password != confirmation) {
                Log.d("RegistrationResponse", "Passwords do not match")
            } else {
                authViewModel.register(username, password)
                    .observe(this, Observer<RegistrationResponse> { registrationResponse ->
                        if (registrationResponse.responseType == RegistrationResponse.ResponseType.ERROR) {
                            Log.d("RegistrationResponse", registrationResponse.message)
                        } else {
                            userViewModel.updateUserFromJwt(
                                applicationContext,
                                registrationResponse.token
                            )

                            userViewModel.user.observe(
                                this, Observer { user ->
                                    Log.d("RegistrationResponse", user.id.toString());
                                }
                            )
                            val intent = Intent(this, MainScreenActivity::class.java)
                            startActivity(intent)
                        }
                    })
            }
        }
    }
}
