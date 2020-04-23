package com.wustlcse438sp20.myrecipe.Repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.NetworkTools.ApiClient
import com.wustlcse438sp20.myrecipe.data.RecipeInformation
import com.wustlcse438sp20.myrecipe.data.SimilarRecipe
import com.wustlcse438sp20.myrecipe.data.recipesLoad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RecipeRepository {
    val service=
        ApiClient.makeRetrofitService()
//    val apiKey ="3d97dfa37191404d8f9a8a2c2123820c"
    val apiKey ="28bb80cceac342b298452511c30c732f"
    fun searchRecipeByIngredients(resBody:MutableLiveData<List<RecipeByIngredients>>, ingredients: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeByIngredients(ingredients,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

    fun searchRecipeByRandom(resBody:MutableLiveData<recipesLoad>){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeByRandom(12,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }

    fun searchRecipeInformation(resBody:MutableLiveData<RecipeInformation>,recipeId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchRecipeInformation(recipeId,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }


    fun searchSimilarRecipes(resBody:MutableLiveData<List<SimilarRecipe>>,recipeId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchSimilarRecipes(recipeId,apiKey)
            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        Log.v("aaaa","跑到这里")
                        resBody.value=response.body()
                    }
                }catch (e: HttpException){
                    println("http error")
                }

            }
        }
    }


}