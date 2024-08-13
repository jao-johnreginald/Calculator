package com.johnreg.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.johnreg.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var number: String? = null

    private var firstNumber: Double = 0.0
    private var lastNumber: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListenersAndTexts()
    }

    private fun setListenersAndTexts() {
        binding.tvResult.text = getString(R.string.zero)

        binding.btnZero.setOnClickListener { onNumberClicked("0") }
        binding.btnOne.setOnClickListener { onNumberClicked("1") }
        binding.btnTwo.setOnClickListener { onNumberClicked("2") }
        binding.btnThree.setOnClickListener { onNumberClicked("3") }
        binding.btnFour.setOnClickListener { onNumberClicked("4") }
        binding.btnFive.setOnClickListener { onNumberClicked("5") }
        binding.btnSix.setOnClickListener { onNumberClicked("6") }
        binding.btnSeven.setOnClickListener { onNumberClicked("7") }
        binding.btnEight.setOnClickListener { onNumberClicked("8") }
        binding.btnNine.setOnClickListener { onNumberClicked("9") }
    }

    private fun onNumberClicked(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else {
            number += clickedNumber
        }

        binding.tvResult.text = number
    }

    private fun plus() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber += lastNumber
        binding.tvResult.text = firstNumber.toString()
    }

    private fun minus() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber -= lastNumber
        binding.tvResult.text = firstNumber.toString()
    }

    private fun multi() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber *= lastNumber
        binding.tvResult.text = firstNumber.toString()
    }

    private fun divide() {
        lastNumber = binding.tvResult.text.toString().toDouble()

        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "Cannot divide by 0", Toast.LENGTH_SHORT).show()
        } else {
            firstNumber *= lastNumber
            binding.tvResult.text = firstNumber.toString()
        }
    }

}