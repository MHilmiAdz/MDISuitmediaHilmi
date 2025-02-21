package com.example.mdisuitmediahilmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondScreen : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var selectednametxv: TextView
    private lateinit var chooseButton: Button
    private lateinit var welcometxtv: TextView
    private lateinit var userNametxtv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.backButton)
        selectednametxv = findViewById(R.id.selectednametxv)
        chooseButton = findViewById(R.id.chooseButton)
        welcometxtv = findViewById(R.id.welcometxtv)
        userNametxtv = findViewById(R.id.userNametxtv)

        // Get the name from ThirdScreen and First
        val selectedName = intent.getStringExtra("SELECTED_NAME")
        if (!selectedName.isNullOrEmpty()) {
            userNametxtv.text = selectedName
        } else {
            userNametxtv.text = intent.getStringExtra("USERNAME")
        }

        // Get the name from ThirdScreen and first
        if (!selectedName.isNullOrEmpty()) {
            selectednametxv.text = selectedName
        } else {
            selectednametxv.text = intent.getStringExtra("USERNAME")
        }

        backButton.setOnClickListener {
            finish()
        }

        chooseButton.setOnClickListener {
            goToThirdScreen()
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (intent != null) {
            super.onNewIntent(intent)
        }
        intent?.let {
            handleIncomingIntent(it)
        }
    }

    private fun handleIncomingIntent(intent: Intent) {
        val selectedName = intent.getStringExtra("SELECTED_NAME")

        if (!selectedName.isNullOrEmpty()) {
            selectednametxv.text = selectedName
            userNametxtv.text = selectedName
        } else {
            val userName = intent.getStringExtra("USERNAME")
            selectednametxv.text = userName ?: "No User Selected"
            userNametxtv.text = userName ?: "No User Selected"
        }
    }

    private fun goToThirdScreen() {
        val intent = Intent(this, ThirdScreen::class.java)
        startActivity(intent)
    }
}