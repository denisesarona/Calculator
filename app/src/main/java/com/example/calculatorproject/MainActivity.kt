package com.example.calculatorproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorproject.R
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var input = ""
    private var operator = ""
    private var operand1 = ""
    private var operand2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        // Mapping button IDs to their respective actions
        val buttonMap = mapOf(
            R.id.deleteButton to { clear() },
            R.id.numSevenBtn to { appendText("7") },
            R.id.numEightBtn to { appendText("8") },
            R.id.numNineBtn to { appendText("9") },
            R.id.numFourBtn to { appendText("4") },
            R.id.numOneBtn to { appendText("1") },
            R.id.numFiveBtn to { appendText("5") },
            R.id.numSixBtn to { appendText("6") },
            R.id.numTwoBtn to { appendText("2") },
            R.id.numThreeBtn to { appendText("3") },
            R.id.decimalBtn to { appendText(".") },
            R.id.numZeroBtn to { appendText("0") },
            R.id.signBtn to { toggleSign() },
            R.id.percentBtn to { computePercentage() },
            R.id.divideBtn to { setOperator("÷") },
            R.id.timesBtn to { setOperator("×") },
            R.id.minusBtn to { setOperator("—") },
            R.id.plusBtn to { setOperator("+") },
            R.id.equalBtn to { calculate() }
        )

        buttonMap.forEach { (id, action) ->
            findViewById<Button>(id).setOnClickListener { action() }
        }
    }

    private fun appendText(text: String) {
        if (text == "." && input.contains(".")) return // Prevent multiple decimal points
        input += text
        display.text = input
    }

    // CLEAR INPUT
    private fun clear() {
        input = ""
        operand1 = ""
        operand2 = ""
        operator = ""
        display.text = "0"
    }

    private fun toggleSign() {
        if (input.isNotEmpty()) {
            input = if (input.startsWith("-")) {
                input.substring(1)
            } else {
                "-$input"
            }
            display.text = input
        }
    }

    // SET OPERATOR
    private fun setOperator(op: String) {
        if (operand1.isEmpty()) {
            operand1 = input
            operator = op
            input = ""
        } else {
            operand2 = input
            calculate()
            operand1 = display.text.toString()
            operator = op
            input = ""
        }
    }

    // CALCULATE BASED ON OPERATORS
    private fun calculate() {
        try {
            if (operand1.isNotEmpty() && operator.isNotEmpty() && input.isNotEmpty()) {
                operand2 = input
                val result = when (operator) {
                    "+" -> operand1.toDouble() + operand2.toDouble()
                    "—" -> operand1.toDouble() - operand2.toDouble()
                    "×" -> operand1.toDouble() * operand2.toDouble()
                    "÷" -> {
                        // DIVIDE BY ZERO
                        if (operand2.toDouble() == 0.0) {
                            display.text = "Error"
                            return
                        } else {
                            operand1.toDouble() / operand2.toDouble()
                        }
                    }
                    else -> 0.0
                }

                // REMOVE DECIMAL
                display.text = formatNumber(result)
                input = display.text.toString()
                operand1 = ""
                operand2 = ""
                operator = ""
            }
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    // CALCULATE AND DISPLAY PERCENT
    private fun computePercentage() {
        if (input.isNotEmpty()) {
            val value = input.toDouble() / 100
            // IF THERE IS OPERATOR LIKE ADD
            if (operator.isNotEmpty() && operand1.isNotEmpty()) {
                val op1 = operand1.toDouble()
                operand2 = (op1 * value).toString()
                display.text = formatNumber(operand2.toDouble())
            } else {
                // JUST SHOW IF NONE
                display.text = formatNumber(value)
            }
            input = display.text.toString()
        }
    }

    // REMOVE DECIMAL
    private fun formatNumber(result: Double): String {
        return if (result % 1 == 0.0) {
            // If the result is an integer, format without decimal places
            result.toInt().toString()
        } else {
            // If the result is a decimal, keep the decimal places
            result.toString()
        }
    }
}
