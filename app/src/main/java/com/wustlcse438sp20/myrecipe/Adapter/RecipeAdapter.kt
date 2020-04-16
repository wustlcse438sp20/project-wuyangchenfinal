package com.wustlcse438sp20.myrecipe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeAdapter (private var context: Context?, private var RecipeList: ArrayList<RecipeShownFormat>):

    RecyclerView.Adapter<RecipeAdapter.ViewHolder>(),ItemTouchHelperAdapter {

    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var onItemClickListener: OnItemClickListener?=null
    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return RecipeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.recipe_title.text= RecipeList[position].title
        if (!RecipeList[position].image.equals(""))
            Picasso.get().load(RecipeList[position].image).into(holder.itemView.recipe_image)
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener{
                onItemClickListener!!.OnItemClick(holder.itemView, position)
            }
        }
    }

    override fun onItemDissmiss(position: Int) {
        RecipeList.removeAt(position)
        notifyItemRemoved(position)
        //do database operation: Delete

    }
}