package com.example.mdisuitmediahilmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FirstScreen : AppCompatActivity() {

    private lateinit var namaedittx: EditText
    private lateinit var palindromedittx: EditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        namaedittx = findViewById(R.id.namaedittx)
        palindromedittx = findViewById(R.id.palindromedittx)
        checkButton = findViewById(R.id.checkButton)
        nextButton = findViewById(R.id.nextButton)

        checkButton.setOnClickListener {
            val inputText = palindromedittx.text.toString().trim()

            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter text to check", Toast.LENGTH_SHORT).show()
            } else {
                val result = if (isPalindrome(inputText)) "is a Palindrome" else "is NOT a Palindrome"
                Toast.makeText(this, "\"$inputText\" $result", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            toSecondScreen()
        }
    }

    private fun isPalindrome(text: String): Boolean {
        val cleanedText = text.replace("\\s".toRegex(), "").lowercase() // Remove spaces & lowercase
        return cleanedText == cleanedText.reversed()
    }

    private fun toSecondScreen(){
        val name = namaedittx.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, SecondScreen::class.java)
            intent.putExtra("USERNAME", name) // ✅ Pass name to the second layout
            startActivity(intent)
        }
    }
}