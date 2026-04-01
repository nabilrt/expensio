package com.abidnabil.expensio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.abidnabil.expensio.databinding.FragmentExpenseBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Locale.getDefault


class ExpenseFragment : Fragment(), ExpenseAdapter.OnItemClickListeners {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var adapter: ExpenseAdapter

    private val sharedPrefs by lazy {
        context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExpenseAdapter(AppData.expenseList, this)

        binding.recyclerViewListExpense.adapter = adapter

        totalExpenseDisplay()
        textInputVisibility()

        setupOnClickListeners()


    }

    private fun setupOnClickListeners() {
        binding.buttonAddExpense.setOnClickListener {
            val intent = Intent(requireContext(), UpsertExpenseActivity::class.java)
            intent.putExtra("insertType", "add")
            startActivity(intent)
        }

        binding.textViewSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                val searchKey: String = p0.toString().lowercase(getDefault())
                val searchResults =
                    AppData.expenseList.filter { item ->
                        item.title.lowercase(getDefault()).contains(searchKey)
                    } as MutableList<Expense>

                if (searchKey.isEmpty()) {
                    adapter.expenseList = AppData.expenseList
                } else {
                    adapter.expenseList = searchResults

                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
        totalExpenseDisplay()
        textInputVisibility()
    }

    override fun onDeleteClick(position: Int) {
        AlertDialog.Builder(requireContext()).setTitle("Warning!")
            .setMessage("Are you sure to delete the expense?").setPositiveButton(
                "Yes"
            ) { _, _ ->
                AppData.expenseList.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, AppData.expenseList.size)

                totalExpenseDisplay()
                textInputVisibility()

                Snackbar.make(
                    binding.frameContainer,
                    "Expense Deleted Successfully!",
                    Snackbar.LENGTH_SHORT
                ).show()


            }.setNegativeButton("No", { dialogue, _ ->
                dialogue.dismiss()
            }).show()


    }

    private fun textInputVisibility() {
        if (AppData.expenseList.isEmpty()) {
            binding.textInputSearch.visibility = View.GONE
            binding.cardViewData.visibility = View.GONE
        } else {
            binding.textInputSearch.visibility = View.VISIBLE
            binding.cardViewData.visibility = View.VISIBLE
        }
    }

    private fun totalExpenseDisplay() {
        if (!AppData.expenseList.isEmpty()) {
            binding.totalExpense.setText(
                AppData.totalExpense().toString() + " " + sharedPrefs?.getString("currency", null)
            )

        } else {
            binding.totalExpense.setText("0" + " " + sharedPrefs?.getString("currency", null))
        }
    }


}
