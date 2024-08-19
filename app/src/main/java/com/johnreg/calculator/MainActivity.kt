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

    // btnEquals & onOperatorClicked() will only performOperation() if operatorControl is true
    // true - numbers | false - AC, Equals, operators
    private var operatorControl = false

    // btnDot will only work if dotControl is true
    // true - AC, DEL, Equals, operators | false - Dot
    private var dotControl = true

    private var equalsControl = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListenerAllButtons()
    }

    private fun setOnClickListenerAllButtons() {
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
                    binding.tvResult.text = getString(R.string.zero)
                    stringNumber = null
                } else {
                    stringNumber = it.dropLast(1)
                    binding.tvResult.text = stringNumber
                    dotControl = !stringNumber!!.contains(".")
                }
            }
        }

        binding.btnEquals.setOnClickListener {
            if (operatorControl) {
                val historyText = binding.tvHistory.text.toString()
                val resultText = binding.tvResult.text.toString()

                // plus() - used to concatenate 2 String expressions together
                binding.tvHistory.text = historyText.plus(resultText).plus("=")
                performOperation()
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
        val historyText = binding.tvHistory.text.toString()
        var resultText = binding.tvResult.text.toString()

        // If the last character is a dot, remove it
        if (resultText.takeLast(1) == ".") resultText = resultText.dropLast(1)

        if (operatorControl) {
            binding.tvHistory.text = historyText.plus(resultText).plus(symbol)
            performOperation()
        }

        if (equalsControl) binding.tvHistory.text = resultText.plus(symbol)

        stringNumber = null
        status = operation

        operatorControl = false
        dotControl = true
    }

    /*
    The lastNumber will always keep the last value printed on the screen
    So the new value is entered on the screen and any of the operators are clicked
    The values in the lastNumber will be transferred to the firstNumber
    And the new value of the lastNumber will be the number that is now on the screen
     */
    private fun performOperation() {
        lastNumber = binding.tvResult.text.toString().toDouble()

        firstNumber = when (status) {
            Operation.DIVIDE -> firstNumber / lastNumber
            Operation.MULTI -> firstNumber * lastNumber
            Operation.MINUS -> firstNumber - lastNumber
            Operation.PLUS -> firstNumber + lastNumber
            Operation.NULL -> lastNumber
        }

        if (firstNumber.isFinite()) {
            binding.tvResult.text = mFormatter.format(firstNumber)
        } else {
            Toast.makeText(this, "THE DIVISOR CANNOT BE ZERO", Toast.LENGTH_LONG).show()
            onAcClicked()
        }
    }

    private fun onAcClicked() {
        binding.tvResult.text = getString(R.string.zero)
        binding.tvHistory.text = null
        firstNumber = 0.0
        lastNumber = 0.0
        stringNumber = null
        status = Operation.NULL
        operatorControl = false
        dotControl = true
        equalsControl = false
    }

}