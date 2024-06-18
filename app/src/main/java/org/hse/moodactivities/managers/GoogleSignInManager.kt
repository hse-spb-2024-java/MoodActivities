import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.tasks.Task
import org.hse.moodactivities.R

object GoogleSignInManager {

    private lateinit var CLIENT_ID: String
    private lateinit var googleSignInClient: GoogleSignInClient

    var RETURN_CODE_SIGN_IN = 9001;

    fun init(context: Context) {
        CLIENT_ID = context.resources.getString(R.string.app_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Fitness.SCOPE_ACTIVITY_READ)
            .requestIdToken(CLIENT_ID)
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun getSignedInAccount(task: Task<GoogleSignInAccount>): GoogleSignInAccount {
        return task.getResult(ApiException::class.java)
    }

    fun signOut(context: Context, onSignOutComplete: () -> Unit) {
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignOutComplete()
            } else {
                Toast.makeText(context, "Sign Out failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}
