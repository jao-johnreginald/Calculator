package com.johnreg.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.johnreg.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mFormatter = DecimalFormat("######.######")

    private var firstNumber = 0.0
    private var lastNumber = 0.0

    private var stringNumber: String? = null
    private var status = Operation.NULL

    private var operatorControl = false
    private var dotControl = true
    private var equalsControl = false

    private var stringHistory = ""
    private var stringResult = ""

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
            stringNumber?.let {
                if (it.length == 1) {
                    onAcClicked()
                } else {
                    stringNumber = it.substring(0, it.length - 1)
                    binding.tvResult.text = stringNumber
                    dotControl = !stringNumber!!.contains(".")
                }
            }
        }

        binding.btnEquals.setOnClickListener {
            stringHistory = binding.tvHistory.text.toString()
            stringResult = binding.tvResult.text.toString()

            if (operatorControl) {
                performOperation()

                binding.tvHistory.text = stringHistory
                    .plus(stringResult)
                    .plus("=")
                    .plus(binding.tvResult.text.toString())
            }

            operatorControl = false
            dotControl = true
            equalsControl = true
        }

        binding.btnDot.setOnClickListener {
            if (dotControl) {
                val resultText = binding.tvResult.text.toString()

                stringNumber = when {
                    stringNumber == null -> "0."

                    equalsControl -> if (resultText.contains(".")) {
                        resultText
                    } else {
                        resultText.plus(".")
                    }

                    else -> "$stringNumber."
                }

                binding.tvResult.text = stringNumber
            }

            dotControl = false
        }
    }

    /*
    When the user clicks on any number, we will first check the variable number
    If the number variable is null, it means there is no number on the screen
    And the number that the user clicks will appear on the screen
    But if the variable number is not null
    All it means there are already entered numbers on the screen
    And the number the user clicks will be added to the end of these numbers
     */
    private fun onNumberClicked(clickedNumber: String) {
        when {
            stringNumber == null -> stringNumber = clickedNumber

            equalsControl -> {
                stringNumber = if (dotControl) {
                    clickedNumber
                } else {
                    binding.tvResult.text.toString().plus(clickedNumber)
                }

                binding.tvHistory.text = null
                firstNumber = stringNumber!!.toDouble()
                lastNumber = 0.0
                status = Operation.NULL
            }

            else -> stringNumber += clickedNumber
        }

        binding.tvResult.text = stringNumber
        operatorControl = true
        equalsControl = false
    }

    private fun onOperatorClicked(symbol: String, operation: Operation) {
        stringHistory = binding.tvHistory.text.toString()
        stringResult = binding.tvResult.text.toString()

        // The plus method of the String class is used to concatenate 2 String expressions together
        binding.tvHistory.text = stringHistory.plus(stringResult).plus(symbol)

        if (operatorControl) performOperation()

        stringNumber = null
        status = operation

        operatorControl = false
        dotControl = true
    }

    private fun onAcClicked() {
        binding.tvResult.text = getString(R.string.zero)
        binding.tvHistory.text = null
        firstNumber = 0.0
        lastNumber = 0.0
        stringNumber = null
        status = Operation.NULL
        dotControl = true
        equalsControl = false
    }

    /*
    The lastNumber will always keep the last value printed on the screen
    So the new value is entered on the screen and any of the operators are clicked
    The values in the lastNumber will be transferred to the firstNumber
    And the new value of the lastNumber will be the number that is now on the screen
     */
    private fun performOperation() {
        lastNumber = binding.tvResult.text.toString().toDouble()

        when (status) {
            Operation.DIVIDE -> {
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
                firstNumber *= lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.MINUS -> {
                firstNumber -= lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.PLUS -> {
                firstNumber += lastNumber
                binding.tvResult.text = mFormatter.format(firstNumber)
            }

            Operation.NULL -> firstNumber = lastNumber
        }
    }

}