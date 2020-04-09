package com.wustlcse438sp20.myrecipe.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.item_meal.view.*

class MealAdapter (private var list: ArrayList<Meal>):

    RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var onItemClickListener: OnItemClickListener?=null
    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.meal_mame.text= list[position].title
        if (!list[position].image.isNullOrEmpty()){
            var url = ""
            if (list[position].image.startsWith("http")) {
                url = list[position].image
            } else {
                url = "https://spoonacular.com/recipeImages/"+list[position].image
            }
            Picasso.get().load(url).into(holder.itemView.meal_image)
        }
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener{
                onItemClickListener!!.OnItemClick(holder.itemView, position)
            }
        }
    }

}