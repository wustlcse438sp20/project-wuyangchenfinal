package com.wustlcse438sp20.myrecipe.data

data class Collection (
    val id:Int,
    val name:String,
    val description:String,
    val recipes:List<RecipeInformation>
)