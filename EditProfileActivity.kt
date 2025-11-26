package com.example.projectdraft

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectdraft.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EditProfileScreen(
                currentName = "Jane Doe",
                currentEmail = "jane.doe@compareapp.com",
                onBack = { finish() },
                onSave = { newName ->
                    Toast.makeText(this, "Saved $newName", Toast.LENGTH_SHORT).show()
                    finish()
                }
            )
        }
    }
}
