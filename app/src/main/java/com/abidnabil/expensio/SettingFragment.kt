package com.abidnabil.expensio

import android.R
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.abidnabil.expensio.databinding.FragmentSettingBinding
import com.google.android.material.snackbar.Snackbar


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currencies = arrayOf(TK_SYMBOL, USD_SYMBOL, EURO_SYMBOL)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, currencies)
        binding.spinnerCurrency.adapter = spinnerAdapter

        loadInitData(spinnerAdapter)
        setupClickListeners()


    }

    private fun loadInitData(spinnerAdapter: ArrayAdapter<String>) {
        if (sharedPrefs.contains("is_dark")) {
            val theme = sharedPrefs.getInt("is_dark", AppCompatDelegate.MODE_NIGHT_NO)

            when (theme) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    binding.radioGroupTheme.check(binding.radioButtonLight.id)
                }

                AppCompatDelegate.MODE_NIGHT_YES -> {
                    binding.radioGroupTheme.check(binding.radioButtonDark.id)
                }
            }
        }

        if (sharedPrefs.contains("currency")) {
            when (sharedPrefs.getString("currency", null)) {

                "৳" -> {
                    val spinnerPosition = spinnerAdapter.getPosition(TK_SYMBOL)
                    binding.spinnerCurrency.setSelection(spinnerPosition)


                }

                "$" -> {
                    val spinnerPosition = spinnerAdapter.getPosition(USD_SYMBOL)
                    binding.spinnerCurrency.setSelection(spinnerPosition)

                }

                "€" -> {
                    val spinnerPosition = spinnerAdapter.getPosition(EURO_SYMBOL)
                    binding.spinnerCurrency.setSelection(spinnerPosition)

                }
            }
        }

    }

    private fun setupClickListeners() {

        binding.radioGroupTheme.setOnCheckedChangeListener { group, i ->

            when (i) {
                binding.radioButtonDark.id -> {

                    sharedPrefs.edit {
                        putInt("is_dark", AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                }

                binding.radioButtonLight.id -> {

                    sharedPrefs.edit {
                        putInt("is_dark", AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


                }
            }
        }
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {


                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedItem = parent.getItemAtPosition(position).toString()


                    when (selectedItem) {
                        TK_SYMBOL -> {
                            sharedPrefs.edit {
                                putString("currency", "৳")
                            }


                        }

                        USD_SYMBOL -> {
                            sharedPrefs.edit {
                                putString("currency", "$")
                            }

                        }

                        EURO_SYMBOL -> {
                            sharedPrefs.edit {
                                putString("currency", "€")
                            }

                        }
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }


    companion object {
        const val TK_SYMBOL = "TK (৳)"
        const val USD_SYMBOL = "USD ($)"
        const val EURO_SYMBOL = "EUR (€)"
    }


}