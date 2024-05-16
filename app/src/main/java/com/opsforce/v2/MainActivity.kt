package com.opsforce.v2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.onelogin.oidc.Callback
import com.onelogin.oidc.OIDCClient
import com.onelogin.oidc.OIDCConfiguration
import com.onelogin.oidc.OneLoginOIDC
import com.onelogin.oidc.login.SignInError
import com.onelogin.oidc.login.SignInSuccess

class MainActivity : AppCompatActivity() {
    var signInButton : Button? = null
    var oidcClient : OIDCClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
      var config=  OIDCConfiguration.Builder()
            .clientId("3f289810-f4e2-013c-2a28-467fb72a0341237586")
            .issuer("https://arvind123.onelogin.com/oidc/2")
            .redirectUrl("com.opsforce.v2://callback")
            .scopes(listOf("openid"))
            .isDebug(true)
            .build()


        signInButton = findViewById<Button>(R.id.signInButton)!!


        signInButton?.setOnClickListener {
            Log.d("MainActivity", "sign-in button clicked.")
            OneLoginOIDC.initialize(this.applicationContext,config)

            oidcClient = OneLoginOIDC.getClient()
            Log.d("MainActivity", "got client...")

            oidcClient?.signIn(this, object : Callback<SignInSuccess, SignInError> {
                override fun onSuccess(success: SignInSuccess) {
                    Log.d("MainActivity",
                        "token:$success.sessionInfo.idToken");
                    // The user has been authenticated successfully, the `success` param will contain the `SessionInfo` with the tokens ready to be used
                }

                override fun onError(loginError: SignInError) {
                    Log.d("MainActivity", "message: $loginError.message");

                    // An error has occurred during the authentication process
                }
            })
        }

    }
}