package com.wustlcse438sp20.myrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.Adapter.IngredientAdapter
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.ExtendedIngredients
import com.wustlcse438sp20.myrecipe.data.SimilarRecipe
import kotlinx.android.synthetic.main.activity_recipe_information.*

class RecipeInformationActivity : AppCompatActivity() {

    lateinit var recipeviewModel: RecipeViewModel
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:IngredientAdapter
    var IngredientList:ArrayList<ExtendedIngredients> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)
        val bundle = intent.extras
        val recipeId = bundle!!.getInt("recipeId")
        //RecyclerView Adapter
        recyclerview = ingredient_recyclerview
        adapter = IngredientAdapter(this,IngredientList)
        recyclerview.layoutManager = GridLayoutManager(this,4)
        recyclerview.adapter = adapter

        //viewmodel
        recipeviewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        recipeviewModel.searchRecipeInformation(recipeId)
        recipeviewModel.recipeDetail.observe(this, Observer {
            if (it.image !== null)
                Picasso.get().load(it.image).into(detail_image)
            textView_cost.text = it.pricePerServing.toString()+" per"
            textView_likes.text = it.aggregateLikes.toString()+" likes"
            textView_readyTime.text = it.readyInMinutes.toString()+" Mins"
            instruction_text.setText( Html.fromHtml(it.instructions))
            IngredientList.clear()
            IngredientList.addAll(it.extendedIngredients)
            adapter.notifyDataSetChanged()
        })
        recipeviewModel.searchSimilarRecipes(recipeId)
        recipeviewModel.similarRecipes.observe(this, Observer { similarRecipes ->
            textView_similar.text = similarRecipes.joinToString(separator = ", ",
                prefix = "",
                postfix = "",
                limit = 3,
                truncated = "there’s more ..."){it -> "${it.title}"}
//            for(recipeS in similarRecipes){
//                Log.v("id",recipeS.id.toString())
//                Log.v("title",recipeS.image)
//                Log.v("image",recipeS.image)
//                Log.v("imageUrls",recipeS.imageUrls.toString())
//                Log.v("readyInMinutes",recipeS.readyInMinutes.toString())
//                Log.v("servings",recipeS.servings.toString())
//            }

        })
    }

}
