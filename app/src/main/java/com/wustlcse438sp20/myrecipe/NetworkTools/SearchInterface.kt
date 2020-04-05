package com.wustlcse438sp20.myrecipe.NetworkTools

import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.data.RecipeInformation
import com.wustlcse438sp20.myrecipe.data.recipesLoad
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchInterface {
    @GET("findByIngredients")
    suspend fun searchRecipeByIngredients(@Query("ingredients") ingredients:String,@Query("apiKey") apiKey:String):Response<List<RecipeByIngredients>>

    @GET("random")
    suspend fun searchRecipeByRandom(@Query("number") number:Int,@Query("apiKey") apiKey:String):Response<recipesLoad>

}