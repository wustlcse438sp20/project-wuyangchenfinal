package com.wustlcse438sp20.myrecipe.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.Repositories.RecipeRepository
import com.wustlcse438sp20.myrecipe.data.RecipeInformation
import com.wustlcse438sp20.myrecipe.data.recipesLoad

class RecipeViewModel (application: Application):AndroidViewModel(application){
    public val recipeRepository = RecipeRepository()
    public var recipeList:MutableLiveData<List<RecipeByIngredients>> = MutableLiveData()
    public var recipeInformation:MutableLiveData<recipesLoad> = MutableLiveData()

    fun searchRecipeByIngredients(ingredients:String){
        recipeRepository.searchRecipeByIngredients(recipeList,ingredients)
    }

    fun searchRecipeByRandom(){
        recipeRepository.searchRecipeByRandom(recipeInformation)
    }
}