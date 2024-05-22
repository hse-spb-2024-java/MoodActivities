import android.content.Context
import android.content.Intent
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

    fun signOut(context: Context) {
        googleSignInClient.signOut().addOnCompleteListener {
            // Handle sign out
        }
    }
}
