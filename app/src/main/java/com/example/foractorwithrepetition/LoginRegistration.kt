package com.example.foractorwithrepetition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foractorwithrepetition.databinding.ActivityLoginRegistrationBinding
import com.example.foractorwithrepetition.databinding.FragmentFragmentDetailBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.button.MaterialButton
import com.yandex.mapkit.MapKitFactory

class LoginRegistration : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val REQUEST_CODE_PERMISSION = 1001
    private val REQUEST_CODE_SIGN_IN = 1000
    private var _binding: ActivityLoginRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ForActorWithRepetition_NoActionBar)
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginRegistrationBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(binding.root) // Используем разметку из binding

        binding.hiText.alpha=0f
        binding.signinGoogle.alpha=0f

        binding.hiText.animate().alpha(1f).translationYBy((30).toFloat()).setStartDelay(300).duration=1500
        binding.signinGoogle.animate().alpha(1f).translationYBy((-30).toFloat()).setStartDelay(300).duration=1500

        val lastInGoogle = GoogleSignIn.getLastSignedInAccount(this)
        if(lastInGoogle!=null){
            val intent = Intent(this, ActivityWithDrawerNavigation::class.java)
            startActivity(intent)
            finish()
        }
        // Настройка авторизации Google
        setupGoogleSignIn()
        // Обработка нажатия кнопки Google Sign-In
        val googleSignInButton = findViewById<ImageButton>(R.id.signin_google)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show()
                // Переход на новую активность после успешного входа
                val intent = Intent(this, ActivityWithDrawerNavigation::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                ActivityCompat.finishAffinity(this)
                finish()
            } else {
                Toast.makeText(this, "Ошибка входа в Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/calendar"))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }
}
