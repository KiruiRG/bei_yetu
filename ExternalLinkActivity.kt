package com.example.projectdraft

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectdraft.R
import com.example.projectdraft.databinding.ActivityExternalLinkBinding
import android.widget.Toast

class ExternalLinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExternalLinkBinding
    private var externalUrl: String? = null

    companion object {
        const val EXTRA_URL = "extra_external_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExternalLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        externalUrl = intent.getStringExtra(EXTRA_URL)

        if (externalUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Error: Product link is missing.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.tvUrlPreview.text = externalUrl

        binding.btnContinue.setOnClickListener {
            navigateToExternalSite(externalUrl!!)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun navigateToExternalSite(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open link. Please check the URL.", Toast.LENGTH_LONG).show()
        }
    }
}