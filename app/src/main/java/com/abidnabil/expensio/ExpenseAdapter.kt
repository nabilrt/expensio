package com.abidnabil.expensio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.abidnabil.expensio.databinding.ItemExpenseBinding

class ExpenseAdapter(
    var expenseList: List<Expense>,
    private val listener: OnItemClickListeners
) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(p0.context),
            p0,
            false
        )
        sharedPreferences=p0.context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(
        p0: ExpenseViewHolder,
        p1: Int
    ) {

        val expense = expenseList[p1]

        p0.binding.textViewExpenseTitle.text = expense.title
        p0.binding.textViewCategory.text = expense.category
        p0.binding.textViewDate.text = expense.expenseDate
        p0.binding.textViewAmount.text = expense.amount.toString() + sharedPreferences.getString("currency",null)
        p0.binding.optionsMenu.setOnClickListener { view ->
            showPopupMenu(view, expense, p1)
        }

    }

    private fun showPopupMenu(view: View, expense: Expense, position: Int) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.action_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(view.context, UpsertExpenseActivity::class.java)
                    intent.putExtra("insertType", "update")
                    intent.putExtra("expenseId", expense.expenseId)
                    view.context.startActivity(intent)
                    true
                }

                R.id.action_delete -> {
                    listener.onDeleteClick(position)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    class ExpenseViewHolder(
        val binding: ItemExpenseBinding,
    ) :
        RecyclerView.ViewHolder(binding.root)


    interface OnItemClickListeners {
        fun onDeleteClick(position: Int)
    }

}