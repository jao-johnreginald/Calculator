package com.johnreg.calculator

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.johnreg.calculator.databinding.ActivityMainBinding
import com.johnreg.calculator.databinding.DialogDarkModeBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val sharedPreferences by lazy { getSharedPreferences("all_data", Context.MODE_PRIVATE) }

    private val mFormatter = DecimalFormat("######.######")

    private var firstNumber = 0.0
    private var lastNumber = 0.0

    private var stringNumber: String? = null
    private var status = Operation.NULL

    // btnEquals & operator buttons will only doOperation() if isOperationDoable is true
    // true - Dot, numbers | false - AC, Equals, operators
    private var isOperationDoable = false

    // onAcClicked() if AC, DEL, Dot, numbers are clicked after btnEquals is clicked
    // true - Equals | false - operators, onAcClicked() from AC, DEL, Dot, numbers
    private var isEqualsClicked = false

    // btnDot will only work if isDotClickable is true
    // true - AC, DEL, Equals, operators | false - Dot
    private var isDotClickable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnMenuItemClickListenerToolbar()
        setOnClickListenerAllButtons()
    }

    override fun onResume() {
        super.onResume()

        val mode = sharedPreferences.getInt(MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)

        binding.tvResult.text = sharedPreferences.getString(RESULT_KEY, getString(R.string.zero))
        binding.tvHistory.text = sharedPreferences.getString(HISTORY_KEY, null)

        firstNumber = sharedPreferences.getString(FIRST_KEY, "0.0")!!.toDouble()
        lastNumber = sharedPreferences.getString(LAST_KEY, "0.0")!!.toDouble()

        stringNumber = sharedPreferences.getString(STRING_KEY, null)
        val statusName = sharedPreferences.getString(STATUS_KEY, Operation.NULL.name)!!
        status = Operation.valueOf(statusName)

        isOperationDoable = sharedPreferences.getBoolean(OPERATION_KEY, false)
        isEqualsClicked = sharedPreferences.getBoolean(EQUALS_KEY, false)
        isDotClickable = sharedPreferences.getBoolean(DOT_KEY, true)
    }

    override fun onPause() {
        super.onPause()

        sharedPreferences.edit {
            putString(RESULT_KEY, binding.tvResult.text.toString())
            putString(HISTORY_KEY, binding.tvHistory.text.toString())
            putString(FIRST_KEY, firstNumber.toString())
            putString(LAST_KEY, lastNumber.toString())
            putString(STRING_KEY, stringNumber)
            putString(STATUS_KEY, status.name)
            putBoolean(OPERATION_KEY, isOperationDoable)
            putBoolean(EQUALS_KEY, isEqualsClicked)
            putBoolean(DOT_KEY, isDotClickable)
        }
    }

    private fun setOnMenuItemClickListenerToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.menu_dark_mode -> {
                    showDialogAndSetDefaultNightMode()
                    true
                }

                else -> false
            }
        }
    }

    private fun showDialogAndSetDefaultNightMode() {
        val dialogBinding = DialogDarkModeBinding.inflate(layoutInflater)
        dialogBinding.switchDarkMode.isChecked = isDarkMode()
        MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Apply") { _, _ ->
                if (dialogBinding.switchDarkMode.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferences.edit { putInt(MODE_KEY, AppCompatDelegate.MODE_NIGHT_YES) }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferences.edit { putInt(MODE_KEY, AppCompatDelegate.MODE_NIGHT_NO) }
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("System") { _, _ ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                sharedPreferences.edit {
                    putInt(MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            .show()
    }

    private fun isDarkMode(): Boolean {
        val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
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
            if (isEqualsClicked) onAcClicked() else {
                if (stringNumber == null) binding.tvResult.text = getString(R.string.zero) else {
                    if (stringNumber!!.length <= 1) {
                        stringNumber = null
                        binding.tvResult.text = getString(R.string.zero)
                    } else {
                        stringNumber = stringNumber!!.dropLast(1)
                        isDotClickable = !stringNumber!!.contains(".")
                        binding.tvResult.text = stringNumber
                    }
                }
            }
        }

        binding.btnEquals.setOnClickListener {
            if (isOperationDoable) {
                val historyText = binding.tvHistory.text.toString()

                // If the last character is a dot, remove it
                var resultText = binding.tvResult.text.toString()
                if (resultText.takeLast(1) == ".") resultText = resultText.dropLast(1)

                // plus() - used to concatenate 2 String expressions together
                binding.tvHistory.text = historyText.plus(resultText).plus("=")
                doOperation()

                isOperationDoable = false
                isEqualsClicked = true
                isDotClickable = true
            }
        }

        binding.btnDot.setOnClickListener {
            if (isDotClickable) {
                stringNumber = if (isEqualsClicked) {
                    onAcClicked()
                    "0."
                } else {
                    if (stringNumber == null) {
                        "0."
                    } else {
                        "$stringNumber."
                    }
                }

                binding.tvResult.text = stringNumber
                isOperationDoable = true
                isDotClickable = false
            }
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
        stringNumber = if (isEqualsClicked) {
            onAcClicked()
            clickedNumber
        } else {
            if (stringNumber == null) {
                clickedNumber
            } else {
                stringNumber + clickedNumber
            }
        }

        binding.tvResult.text = stringNumber
        isOperationDoable = true
    }

    private fun onOperatorClicked(symbol: String, operation: Operation) {
        val historyText = binding.tvHistory.text.toString()

        var resultText = binding.tvResult.text.toString()
        if (resultText.takeLast(1) == ".") resultText = resultText.dropLast(1)

        // This will be skipped if isEqualsClicked is true (isOperationDoable = false)
        if (isOperationDoable) {
            binding.tvHistory.text = historyText.plus(resultText).plus(symbol)
            doOperation()

            status = operation
            isOperationDoable = false
        }

        // This will be skipped if isOperationDoable is true (isEqualsClicked = false)
        if (isEqualsClicked) {
            binding.tvHistory.text = resultText.plus(symbol)

            status = operation
            isEqualsClicked = false
        }

        stringNumber = null
        isDotClickable = true
    }

    /*
    The lastNumber will always keep the last value printed on the screen
    So the new value is entered on the screen and any of the operators are clicked
    The values in the lastNumber will be transferred to the firstNumber
    And the new value of the lastNumber will be the number that is now on the screen
     */
    private fun doOperation() {
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
        isOperationDoable = false
        isEqualsClicked = false
        isDotClickable = true
    }

    companion object {
        const val MODE_KEY = "mode"
        const val RESULT_KEY = "result_text"
        const val HISTORY_KEY = "history_text"
        const val FIRST_KEY = "first_number"
        const val LAST_KEY = "last_number"
        const val STRING_KEY = "string_number"
        const val STATUS_KEY = "status_name"
        const val OPERATION_KEY = "is_operation_doable"
        const val EQUALS_KEY = "is_equals_clicked"
        const val DOT_KEY = "is_dot_clickable"
    }

}