package com.wustlcse438sp20.myrecipe.NetworkTools

import com.google.android.gms.common.api.internal.ApiKey
import com.wustlcse438sp20.myrecipe.Data.RecipeByIngredients
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchInterface {
    @GET("findByIngredients")
    suspend fun searchRecipeByIngredients(@Query("ingredients") ingredients:String,@Query("apiKey") apiKey:String):Response<List<RecipeByIngredients>>
}