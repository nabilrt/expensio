package com.abidnabil.expensio

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.abidnabil.expensio.databinding.ActivityUpsertExpenseBinding

class UpsertExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpsertExpenseBinding

    private val insertType by lazy {
        intent.getStringExtra("insertType")
    }

    private val expenseID by lazy {
        intent.getIntExtra("expenseId", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpsertExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (insertType == "update") {
            initialRecord()

        }

        binding.upsertRecord.setOnClickListener {
            upsertRecord()
        }

    }

    private fun initialRecord() {
        val expenseItem = AppData.expenseList.find { (expenseId) -> expenseId == expenseID }
        binding.editTextExpenseTitle.setText(expenseItem?.title)
        binding.editTextExpenseCategory.setText(expenseItem?.category)
        binding.editTextExpenseAmount.setText(expenseItem?.amount.toString())
        binding.editTextExpenseDate.setText(expenseItem?.expenseDate)
    }

    private fun upsertRecord() {
        if (insertType == "add") {
            AppData.expenseList.add(
                Expense(
                    AppData.expenseList.size + 1,
                    binding.editTextExpenseTitle.text.toString(),
                    binding.editTextExpenseCategory.text.toString(),
                    binding.editTextExpenseAmount.text.toString().toInt(),
                    binding.editTextExpenseDate.text.toString()
                )
            )
            finish()

        } else {
            val expenseItem = AppData.expenseList.find { (expenseId) -> expenseId == expenseID }
            expenseItem?.title = binding.editTextExpenseTitle.text.toString()
            expenseItem?.expenseDate = binding.editTextExpenseDate.text.toString()
            expenseItem?.category = binding.editTextExpenseCategory.text.toString()
            expenseItem?.amount = binding.editTextExpenseAmount.text.toString().toInt()

            finish()
        }

    }
}