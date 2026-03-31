package com.abidnabil.expensio

import java.io.Serializable

data class Expense(
    val expenseId: Int,
    var title: String,
    var category: String,
    var amount: Int,
    var expenseDate: String
) :
    Serializable
