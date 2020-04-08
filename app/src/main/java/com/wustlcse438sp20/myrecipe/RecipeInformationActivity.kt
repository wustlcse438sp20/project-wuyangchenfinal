package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.SimilarRecipe
import kotlinx.android.synthetic.main.activity_recipe_information.*

class RecipeInformationActivity : AppCompatActivity() {

    lateinit var recipeviewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)
        val bundle = intent.extras
        val recipeId = bundle!!.getInt("recipeId")

        //viewmodel
        recipeviewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        // recipe details
        recipeviewModel.searchRecipeInformation(recipeId)
        recipeviewModel.recipeDetail.observe(this, Observer {
            if (it.image !== null)
                Picasso.get().load(it.image).into(detail_image)
            textView_cost.text = it.pricePerServing.toString()+" per"
            textView_likes.text = it.aggregateLikes.toString()+" likes"
            textView_readyTime.text = it.readyInMinutes.toString()+" Mins"
            instruction_text.setText( it.instructions)

        })
        // similar recipes
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
