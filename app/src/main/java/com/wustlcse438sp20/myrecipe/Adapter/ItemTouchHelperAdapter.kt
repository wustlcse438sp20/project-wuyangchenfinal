package com.wustlcse438sp20.myrecipe.Adapter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat

interface ItemTouchHelperAdapter {

   fun onItemDissmiss(position:Int){
      // remove from database
//      db.collection("collections")
//         .whereEqualTo(FieldPath.documentId(), collection_id)
//         .get()
//         .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
//            if (task.isSuccessful) {
//               Log.v("Search in database", "Sucess")
//               println("search success !!!!!!!!!!!!!!!!!!")
//               for (document in task.result!!) {
//
//                  var recipes = document.get("recipes") as ArrayList<MutableMap<String,Any>>){
//                     var recipe_id:Int =recipe["id"].toString().toInt()
//                     var recipe_title:String = recipe["title"].toString()
//                     var recipe_image:String = recipe["image"].toString()
//                     Log.v("recipe id",recipe_id.toString())
//                     Log.v("recipe title",recipe_title)
//                     Log.v("recipe image",recipe_image)
//                     recipeList.add(RecipeShownFormat(recipe_id,recipe_title,recipe_image))
//                  }
//               }
//               adapter.notifyDataSetChanged()
//            }
//            else {
//               Log.v("Search in database", "Fail")
//               println("failed to get user data")
//            }
//         })
   }
}