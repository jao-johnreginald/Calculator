package com.johnreg.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.johnreg.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var number: String? = null

    private var firstNumber: Double = 0.0
    private var lastNumber: Double = 0.0

    private var status: String? = null
    private var operator: Boolean = false

    private val mFormatter = DecimalFormat("######.######")

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

        binding.btnDivide.setOnClickListener {
            if (operator) {
                when (status) {
                    "divide" -> divide()
                    "multi" -> multi()
                    "minus" -> minus()
                    "plus" -> plus()
                    else -> firstNumber = binding.tvResult.text.toString().toDouble()
                }
            }

            status = "divide"
            operator = false
            number = null
        }
        binding.btnMulti.setOnClickListener {
            if (operator) {
                when (status) {
                    "divide" -> divide()
                    "multi" -> multi()
                    "minus" -> minus()
                    "plus" -> plus()
                    else -> firstNumber = binding.tvResult.text.toString().toDouble()
                }
            }

            status = "multi"
            operator = false
            number = null
        }
        binding.btnMinus.setOnClickListener {
            if (operator) {
                when (status) {
                    "divide" -> divide()
                    "multi" -> multi()
                    "minus" -> minus()
                    "plus" -> plus()
                    else -> firstNumber = binding.tvResult.text.toString().toDouble()
                }
            }

            status = "minus"
            operator = false
            number = null
        }
        binding.btnPlus.setOnClickListener {
            if (operator) {
                when (status) {
                    "divide" -> divide()
                    "multi" -> multi()
                    "minus" -> minus()
                    "plus" -> plus()
                    else -> firstNumber = binding.tvResult.text.toString().toDouble()
                }
            }

            status = "plus"
            operator = false
            number = null
        }
    }

    private fun onNumberClicked(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else {
            number += clickedNumber
        }

        binding.tvResult.text = number
        operator = true
    }

    private fun plus() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber += lastNumber
        binding.tvResult.text = mFormatter.format(firstNumber)
    }

    private fun minus() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber -= lastNumber
        binding.tvResult.text = mFormatter.format(firstNumber)
    }

    private fun multi() {
        lastNumber = binding.tvResult.text.toString().toDouble()
        firstNumber *= lastNumber
        binding.tvResult.text = mFormatter.format(firstNumber)
    }

    private fun divide() {
        lastNumber = binding.tvResult.text.toString().toDouble()

        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "Cannot divide by 0", Toast.LENGTH_SHORT).show()
        } else {
            firstNumber /= lastNumber
            binding.tvResult.text = mFormatter.format(firstNumber)
        }
    }

}