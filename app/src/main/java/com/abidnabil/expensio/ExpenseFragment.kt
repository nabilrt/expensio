package com.abidnabil.expensio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

                Log.v("Expense", searchResults.toString())

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
            binding.textInputSearch.visibility = View.INVISIBLE
        } else {
            binding.textInputSearch.visibility = View.VISIBLE
        }
    }


}
