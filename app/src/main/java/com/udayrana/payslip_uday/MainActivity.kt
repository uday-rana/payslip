package com.udayrana.payslip_uday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.udayrana.payslip_uday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCalculate.setOnClickListener {
            val hourlyPayText = binding.editTextPayHour.text.toString()
            val hoursWorkedText = binding.editTextHoursWorkedWeek.text.toString()

            if (hourlyPayText.isEmpty() || hoursWorkedText.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    "Error: Enter hourly pay and hours worked",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val hourlyPay = hourlyPayText.toDoubleOrNull()
            val hoursWorked = hoursWorkedText.toDoubleOrNull()

            if (hourlyPay == null || hoursWorked == null) {
                Snackbar.make(binding.root, "Error: Input must be a number", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (hourlyPay < 55 || hourlyPay > 105) {
                Snackbar.make(
                    binding.root,
                    "Error: Hourly pay must be between $55 and $105",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (hoursWorked < 0 || hoursWorked > 60) {
                Snackbar.make(
                    binding.root,
                    "Error: Hours worked must be between 0 and 60",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var overtimeHours = 0.0
            var regularPayHours = hoursWorked
            if (hoursWorked > 40) {
                overtimeHours = hoursWorked - 40
                regularPayHours = 40 - overtimeHours
            }

            val grossIncome = (regularPayHours * hourlyPay) + (overtimeHours * hourlyPay * 2)
            val ei = grossIncome * 6 / 100
            val cpp = grossIncome * 7.5 / 100
            val vacationPay = grossIncome * 5 / 100
            val incomeBeforeTax = grossIncome - ei - cpp - vacationPay

            var incomeTax = 0.0
            if (incomeBeforeTax < 2400) {
                incomeTax = incomeBeforeTax * 10 / 100
            } else if (incomeBeforeTax > 2400 && incomeBeforeTax < 4800) {
                val incomeInTaxBracket = incomeBeforeTax - 2400
                incomeTax = ((2400 * 10 / 100) + (incomeInTaxBracket * 18 / 100))
            } else if (incomeBeforeTax > 4800 && incomeBeforeTax < 6000) {
                val incomeInTaxBracket = incomeBeforeTax - 4800
                incomeTax =
                    ((2400 * 10 / 100) + (4800 * 18 / 100) + (incomeInTaxBracket * 22 / 100))
            } else if (incomeBeforeTax > 6000) {
                incomeTax = (((2400 * 10 / 100) + (4800 * 18 / 100) + (6000 * 22 / 100)).toDouble())
            }
            val netIncome = incomeBeforeTax - incomeTax

            binding.textViewResult.text = """
                Hours worked: $$hoursWorked
                Hourly rate: $$hourlyPay
                Weekly earnings: $$grossIncome
                EI: $$ei
                CPP: $$cpp
                Vacation pay: $$vacationPay
                Tax: $$incomeTax
                Total pay: $$netIncome
            """.trimIndent()

        }
    }
}