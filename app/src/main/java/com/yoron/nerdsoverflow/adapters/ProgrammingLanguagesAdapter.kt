/*
 *
 * Created by Obaida Al Mostarihi on 8/1/21, 8:46 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/1/21, 8:46 AM
 *
 */

package com.yoron.nerdsoverflow.adapters

import android.R.attr.button
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.TransitionDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.classes.toPx
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import kotlinx.android.synthetic.main.item_chip_category.view.*


class ProgrammingLanguagesAdapter(var languages: ArrayList<ProgrammingLanguageModel>) :
    RecyclerView.Adapter<ProgrammingLanguagesAdapter.ViewHolder>(), Filterable {
    private var filteredLanguages: ArrayList<ProgrammingLanguageModel> = ArrayList()
    private var selectedLanguages: ArrayList<ProgrammingLanguageModel> = ArrayList()

    private var selectedLanguagesListener: SelectedLanguagesListener? = null
    class ViewHolder(
        val context: Context,
        private val view: View
        ) : RecyclerView.ViewHolder(view) {


        /**
         * setup all the chip function the click listener with setting the background color.
         */
        fun setupChip(language: ProgrammingLanguageModel, onChipClicked: (Boolean) -> Unit) {
            view.chipItem.text = language.language

            view.chipItem.setBackgroundResource(
                if (language.selected == true) {
                    R.drawable.selected_chip_shape
                } else {
                    R.drawable.chip_shape
                }
            )


            view.chipItem.setOnClickListener {
                language.selected = language.selected != true
                onChipClicked(language.selected!!)
                view.chipItem.setBackgroundResource(
                    if (language.selected == true) {
                        R.drawable.selected_chip_shape
                    } else {
                        R.drawable.chip_shape
                    }
                )




            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chip_category, parent, false)

        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == filteredLanguages.lastIndex){
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 100f.toPx().toInt()
            holder.itemView.layoutParams = params
        }else{
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }


        holder.setupChip(language = filteredLanguages[position]){ isSelected ->
            if (isSelected){
                selectedLanguages.add(filteredLanguages[position])
            }else{
                selectedLanguages.remove(filteredLanguages[position])
            }
            selectedLanguagesListener?.selectedList(selectedLanguages)
        }
    }

    override fun getItemCount(): Int {
        return filteredLanguages.size
    }


    fun addAllItems(languages: ArrayList<ProgrammingLanguageModel>) {
        this.languages = languages
        if (this.filteredLanguages.isEmpty())
            this.filteredLanguages = languages

        notifyDataSetChanged()
    }

    fun setSelectionListener(selectedLanguagesListener: SelectedLanguagesListener?){
        this.selectedLanguagesListener = selectedLanguagesListener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString: String = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredLanguages = languages

                } else {

                    val filteredList: ArrayList<ProgrammingLanguageModel> = ArrayList()

                    for (row in languages) {
                        if (row.language?.lowercase()
                                ?.contains(charString.lowercase()) == true
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredLanguages = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredLanguages
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    filteredLanguages = results.values as ArrayList<ProgrammingLanguageModel>
                }
                notifyDataSetChanged()
            }

        }
    }

    interface SelectedLanguagesListener{
        fun selectedList(selectedLanguages: ArrayList<ProgrammingLanguageModel>)
    }
}