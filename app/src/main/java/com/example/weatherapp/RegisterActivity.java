package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Activity for user registration with Firebase Authentication
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Register button click
        binding.regBtn.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();
            String confirm = binding.editTextConfirmPassword.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new user in Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this,
                                    "Registered: " + (user != null ? user.getEmail() : ""),
                                    Toast.LENGTH_SHORT).show();
                            // Go to MainActivity after successful registration
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Login に戻る
                        } else {
                            Toast.makeText(this,
                                    "Registration failed: " + (task.getException() != null ? task.getException().getMessage() : ""),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
