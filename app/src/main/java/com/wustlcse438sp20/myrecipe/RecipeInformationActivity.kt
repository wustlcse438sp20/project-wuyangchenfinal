package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
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
        recipeviewModel.searchRecipeInformation(recipeId)
        recipeviewModel.recipeDetail.observe(this, Observer {
            if (it.image !== null)
                Picasso.get().load(it.image).into(detail_image)
            textView_cost.text = it.pricePerServing.toString()+" per"
            textView_likes.text = it.aggregateLikes.toString()+" likes"
            textView_readyTime.text = it.readyInMinutes.toString()+" Mins"
            instruction_text.setText( it.instructions)

        })
    }

}
