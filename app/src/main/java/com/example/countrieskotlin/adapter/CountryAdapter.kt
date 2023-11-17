package com.example.countrieskotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.countrieskotlin.R
import com.example.countrieskotlin.databinding.ItemCountryBinding
import com.example.countrieskotlin.model.Country
import com.example.countrieskotlin.view.FeedFragmentDirections

class CountryAdapter(val countryList: ArrayList<Country>):RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {


    class CountryViewHolder(var binding: ItemCountryBinding) :RecyclerView.ViewHolder(binding.root),CountryClickListener{
        override fun onCountryClicked(v: View) {
            var a = binding.countryUuidText.text.toString().toInt()
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(a)
            Navigation.findNavController(v).navigate(action)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        var binding = DataBindingUtil.inflate<ItemCountryBinding>(LayoutInflater.from(parent.context),R.layout.item_country,parent,false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.country= countryList[position]
        holder.binding.listener = holder

        /*
            holder.view.setOnClickListener{
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
            }

         */
        /*
        holder.view.name.text = countryList[position].countryName
        holder.view.region.text = countryList[position].countryRegion

         */
    }

    fun updateCountryList(newCountryList: List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }


}