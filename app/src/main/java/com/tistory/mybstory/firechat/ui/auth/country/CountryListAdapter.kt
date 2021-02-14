package com.tistory.mybstory.firechat.ui.auth.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.databinding.ItemCountryListBinding

class CountryListAdapter(val callback: ((Country) -> Unit)?) :
    RecyclerView.Adapter<CountryListAdapter.CountryListViewHolder>() {

    private val countryList: MutableList<Country> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListViewHolder {
        val binding: ItemCountryListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_country_list,
            parent,
            false
        )
        val viewHolder = CountryListViewHolder(binding)
        binding.root.setOnClickListener { callback?.invoke(countryList[viewHolder.bindingAdapterPosition]) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) {
        holder.binding.country = countryList[position]
    }

    override fun getItemCount(): Int = countryList.size

    fun putData(data: List<Country>) {
        countryList.clear()
        countryList.addAll(data)
        notifyDataSetChanged()
    }

    class CountryListViewHolder(val binding: ItemCountryListBinding) :
        RecyclerView.ViewHolder(binding.root)

}



