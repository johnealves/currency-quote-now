package com.betrybe.currencyview.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import com.betrybe.currencyview.ui.adapters.PriceAdapter
import com.betrye.currencyview.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CurrencyListFragment : Fragment() {

    private val apiService = ApiServiceClient.instance
    private lateinit var mCurrencySelector: AutoCompleteTextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var symbolList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currencu_list_fragment, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCurrencySelector = view.findViewById(R.id.currency_selection_input_layout)
        mRecyclerView = view.findViewById(R.id.currency_rates_state)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        mCurrencySelector.setOnItemClickListener { parent, selectorView, position, id ->
            val selectedCurrency = parent.getItemAtPosition(position) as String
            val currency = selectedCurrency.split("-").first().trim()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val reponse = apiService.getRate(currency)
                    val rates: List<Pair<String, Double>> = reponse.body()?.rates?.toList().orEmpty()

                    val combinedList = getCombinedList(rates)
                    val priceAdapter = PriceAdapter(combinedList)

                    withContext(Dispatchers.Main) {
                        mRecyclerView.adapter = priceAdapter
                        priceAdapter.notifyDataSetChanged()
                        mRecyclerView.visibility = View.VISIBLE
                    }
                } catch (e: SocketTimeoutException) {
                    Toast
                        .makeText(context,"Conection error, try again later", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseSymbol = apiService.getSymbol()
                val symbolsBody = responseSymbol.body()
                val selectors = symbolsBody?.symbols?.map { "${it.key} - ${it.value}" }
                symbolList = selectors.orEmpty()
                val adapterKeys = ArrayAdapter(
                    requireContext(), android.R.layout.simple_list_item_1, selectors?.toList().orEmpty())
                withContext(Dispatchers.Main) {
                    mCurrencySelector.setAdapter(adapterKeys)
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context,"Conection error, try again later", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun getCombinedList(rates: List<Pair<String, Double>>): List<Pair<String, Double>> {
        val symbolMap = symbolList.associate {
            val parts = it.split(" - ")
            parts[0] to it
        }

        val combinedList = rates.mapNotNull {
            val symbolFull = symbolMap[it.first]
            if (symbolFull != null) {
                Pair(symbolFull, it.second)
            } else {
                null
            }
        }

        return combinedList
    }

}