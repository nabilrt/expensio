package com.abidnabil.expensio

class AppData {
    companion object {
        val expenseList: MutableList<Expense> = mutableListOf()

        fun totalExpense(): Int {
            return expenseList.fold(0) { acc, expense -> acc + expense.amount }
        }
    }
}