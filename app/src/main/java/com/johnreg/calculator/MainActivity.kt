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

    private var history: String = ""
    private var currentResult: String = ""

    private var dotControl: Boolean = true

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

        binding.btnAc.setOnClickListener { onButtonAcClicked() }
        binding.btnDel.setOnClickListener {
            number?.let {
                number = it.substring(0, it.length - 1)
                binding.tvResult.text = number
            }
        }

        binding.btnDivide.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            // The plus method of the String class is used to concatenate 2 String expressions together
            binding.tvHistory.text = history.plus(currentResult).plus("/")

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
            dotControl = true
        }
        binding.btnMulti.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            binding.tvHistory.text = history.plus(currentResult).plus("*")

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
            dotControl = true
        }
        binding.btnMinus.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            binding.tvHistory.text = history.plus(currentResult).plus("-")

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
            dotControl = true
        }
        binding.btnPlus.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            binding.tvHistory.text = history.plus(currentResult).plus("+")

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
            dotControl = true
        }

        binding.btnEquals.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            if (operator) {
                when (status) {
                    "divide" -> divide()
                    "multi" -> multi()
                    "minus" -> minus()
                    "plus" -> plus()
                    else -> firstNumber = binding.tvResult.text.toString().toDouble()
                }

                binding.tvHistory.text = history
                    .plus(currentResult)
                    .plus("=")
                    .plus(binding.tvResult.text.toString())
            }

            operator = false
            dotControl = true
        }

        binding.btnDot.setOnClickListener {
            if (dotControl) {
                number = if (number == null) "0." else "$number."
                binding.tvResult.text = number
            }

            dotControl = false
        }
    }

    private fun onButtonAcClicked() {
        number = null
        status = null
        binding.tvResult.text = getString(R.string.zero)
        binding.tvHistory.text = null
        firstNumber = 0.0
        lastNumber = 0.0
        dotControl = true
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