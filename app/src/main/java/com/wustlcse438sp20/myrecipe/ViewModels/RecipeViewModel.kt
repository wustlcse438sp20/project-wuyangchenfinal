package com.wustlcse438sp20.myrecipe.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.Data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.Repositories.RecipeRepository

class RecipeViewModel (application: Application):AndroidViewModel(application){
    public val recipeRepository = RecipeRepository()
    public var recipeList:MutableLiveData<List<RecipeByIngredients>> = MutableLiveData()

    fun searchRecipeByIngredients(ingredients:String){
        recipeRepository.searchRecipeByIngredients(recipeList,ingredients)
    }
}