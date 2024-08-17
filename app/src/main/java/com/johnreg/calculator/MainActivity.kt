package com.johnreg.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.johnreg.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var number: String? = null

    private var firstNumber = 0.0
    private var lastNumber = 0.0

    private var status = Operation.NULL

    private val mFormatter = DecimalFormat("######.######")

    private var history = ""
    private var currentResult = ""

    private var operatorControl = false
    private var dotControl = true
    private var equalsControl = false

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

        binding.btnDivide.setOnClickListener { onOperatorClicked("/", Operation.DIVIDE) }
        binding.btnMulti.setOnClickListener { onOperatorClicked("*", Operation.MULTI) }
        binding.btnMinus.setOnClickListener { onOperatorClicked("-", Operation.MINUS) }
        binding.btnPlus.setOnClickListener { onOperatorClicked("+", Operation.PLUS) }

        binding.btnAc.setOnClickListener { onAcClicked() }

        binding.btnDel.setOnClickListener {
            number?.let {
                if (it.length == 1) {
                    onAcClicked()
                } else {
                    number = it.substring(0, it.length - 1)
                    binding.tvResult.text = number
                    dotControl = !number!!.contains(".")
                }
            }
        }

        binding.btnEquals.setOnClickListener {
            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            if (operatorControl) {
                performOperation()

                binding.tvHistory.text = history
                    .plus(currentResult)
                    .plus("=")
                    .plus(binding.tvResult.text.toString())
            }

            operatorControl = false
            dotControl = true
            equalsControl = true
        }

        binding.btnDot.setOnClickListener {
            if (dotControl) {
                number = when {
                    number == null -> "0."
                    equalsControl -> if (binding.tvResult.text.toString().contains(".")) {
                        binding.tvResult.text.toString()
                    } else {
                        binding.tvResult.text.toString().plus(".")
                    }

                    else -> "$number."
                }

                binding.tvResult.text = number
            }

            dotControl = false
        }
    }

    private fun onNumberClicked(clickedNumber: String) {
        when {
            number == null -> number = clickedNumber
            equalsControl -> {
                number = if (dotControl) {
                    clickedNumber
                } else {
                    binding.tvResult.text.toString().plus(clickedNumber)
                }

                firstNumber = number!!.toDouble()
                lastNumber = 0.0
                status = Operation.NULL
                binding.tvHistory.text = null
            }

            else -> number += clickedNumber
        }

        binding.tvResult.text = number
        operatorControl = true
        equalsControl = false
    }

    private fun onOperatorClicked(symbol: String, operation: Operation) {
        history = binding.tvHistory.text.toString()
        currentResult = binding.tvResult.text.toString()

        // The plus method of the String class is used to concatenate 2 String expressions together
        binding.tvHistory.text = history.plus(currentResult).plus(symbol)

        if (operatorControl) {
            performOperation()
        }

        status = operation
        operatorControl = false
        number = null
        dotControl = true
    }

    private fun onAcClicked() {
        binding.tvResult.text = getString(R.string.zero)
        binding.tvHistory.text = null
        firstNumber = 0.0
        lastNumber = 0.0
        number = null
        status = Operation.NULL
        dotControl = true
        equalsControl = false
    }

    private fun performOperation() {
        val resultTextToDouble = binding.tvResult.text.toString().toDouble()
        when (status) {
            Operation.DIVIDE -> {
                lastNumber = resultTextToDouble

                if (lastNumber == 0.0) {
                    Toast.makeText(
                        applicationContext, "The divisor cannot be zero", Toast.LENGTH_LONG
                    ).show()
                } else {
                    firstNumber /= lastNumber
                    binding.tvResult.text = mFormatter.format(firstNumber)
                }
            }

            Operation.MULTI -> {
                lastNumber = resultTextToDouble

                firstNumber *= lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.MINUS -> {
                lastNumber = resultTextToDouble

                firstNumber -= lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.PLUS -> {
                lastNumber = resultTextToDouble

                firstNumber += lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.NULL -> firstNumber = resultTextToDouble
        }
    }

}