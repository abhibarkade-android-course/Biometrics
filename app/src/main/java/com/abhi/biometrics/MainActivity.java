package com.abhi.biometrics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    LinearLayout parent;
    ImageView icon;
    TextView msg;

    BiometricPrompt biometricPrompt;

    BiometricPrompt.PromptInfo promptInfo;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.parent);
        icon = findViewById(R.id.icon);
        msg = findViewById(R.id.msg);

        // Check Hardware Status
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                msg.setTextColor(Color.GREEN);
                msg.setText("Fingerprint Sensor Available");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msg.setTextColor(Color.RED);
                icon.setBackgroundTintList(ContextCompat.getColorStateList(this, Color.RED));
                msg.setText("Your Device doesn't have Fingerprint Sensor");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg.setTextColor(Color.RED);
                icon.setBackgroundTintList(ContextCompat.getColorStateList(this, Color.RED));
                msg.setText("Fingerprint Sensor Currently Unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg.setTextColor(Color.RED);
                icon.setBackgroundTintList(ContextCompat.getColorStateList(this, Color.RED));
                msg.setText("No Fingerprint found on this Device");
                break;
        }

        // Executor and Show Fingerprint Dialog

        Executor executor=ContextCompat.getMainExecutor(this);

        biometricPrompt=new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Snackbar.make(parent,errString,Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Snackbar.make(parent,"Login Successfull",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Snackbar.make(parent,"Authentication Failed due to some reason !!",Snackbar.LENGTH_SHORT).show();
            }
        });

        promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login with Fingerprint")
                .setDescription("User Fingerprint to Login")
                .setNegativeButtonText("Cancel")
                .build();
    }

    public void login(View view) {
        biometricPrompt.authenticate(promptInfo);
    }
}


















