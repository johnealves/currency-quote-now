package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betrye.currencyview.R

class PriceAdapter(private val rates: List<Pair<String, Double>>): Adapter<PriceAdapter.PriceViewHolder>() {

    class PriceViewHolder(view: View): ViewHolder(view) {
        val currencyTitle: TextView by lazy { view.findViewById(R.id.price_currency) }
        val currencyValue: TextView by lazy { view.findViewById(R.id.price_price) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.price_adapter, parent, false)
        return PriceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.currencyTitle.text = rates[position].first.toString()
        holder.currencyValue.text = rates[position].second.toString()
    }

}