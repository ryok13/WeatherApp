package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.MainActivity;
import com.example.weatherapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Activity for user login with Firebase Authentication
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // If already logged in → go to MainActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMain();
            return;
        }

        // Login button click
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            // Check empty fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password required.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sign in with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            goToMain(); // Success → go to MainActivity
                        } else {
                            Toast.makeText(this,
                                    "Login failed: " + (task.getException() != null ? task.getException().getMessage() : ""),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Register link → open RegisterActivity
        binding.registerNow.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    // Navigate to MainActivity and finish LoginActivity
    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
