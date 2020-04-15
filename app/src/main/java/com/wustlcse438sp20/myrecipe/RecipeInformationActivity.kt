package com.wustlcse438sp20.myrecipe

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.Adapter.IngredientAdapter
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.*
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import com.wustlcse438sp20.myrecipe.data.Collection
import kotlinx.android.synthetic.main.activity_recipe_information.*
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class RecipeInformationActivity : AppCompatActivity() {

    lateinit var recipeviewModel: RecipeViewModel
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:IngredientAdapter
    var IngredientList:ArrayList<ExtendedIngredients> = ArrayList()
    private lateinit var recipe:RecipeInformation
    private lateinit var collectionIds: List<String>
    private var collectionInfos: ArrayList<Collection> = ArrayList()
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)
        val bundle = intent.extras
        val recipeId = bundle!!.getInt("recipeId")
        var user_email = bundle!!.getString("user_email")
        Log.v("recipeId get",recipeId.toString())
        //RecyclerView Adapter
        recyclerview = ingredient_recyclerview
        adapter = IngredientAdapter(this,IngredientList)
        recyclerview.layoutManager = GridLayoutManager(this,4)
        recyclerview.adapter = adapter

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings


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
            instruction_text.setText( Html.fromHtml(it.instructions))
            IngredientList.clear()
            IngredientList.addAll(it.extendedIngredients)

            recipe = it
            adapter.notifyDataSetChanged()
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

        // search for all collections in userProfile database
        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    collectionIds = document.get("collections") as ArrayList<String>
                    collectionInfos.clear()
                    Log.v("collectionIds",collectionIds.toString())
                    for(collectionId in collectionIds){
                        db.collection("collections")
                            .whereEqualTo(FieldPath.documentId(), collectionId)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    Log.d("TAG", "${document.id} => ${document.data}")
                                    collectionInfos.add(Collection(collectionId,document.get("name").toString(),document.get("description").toString(),document.get("recipes") as ArrayList<RecipeShownFormat>))
                                    Log.v("id",collectionId.toString())
                                    Log.v("name",document.get("name").toString())
                                    Log.v("description",document.get("description").toString())
                                    Log.v("recipes",document.get("recipes").toString())
                                    Log.v("collectionInfos",collectionInfos.toString())
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("TAG", "Error getting documents: ", exception)
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }


        add_to_collection.setOnClickListener(){


            // 在数据库查询之前加载了
            Log.v("bbbb","跑到这里")
            // pop up alertdialog for user to choose from
            if (collectionInfos.size == 0){
                Log.v("cccc","跑到这里")
                Toast.makeText(this,"You have to create a new collection first" ,Toast.LENGTH_LONG).show()
            }else{
                Log.v("dddd","跑到这里")
                // Dialog  choose playlist
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                var collectionItems = arrayOfNulls<CharSequence>(collectionInfos.size)
                var selectedCollections: ArrayList<Collection> = ArrayList()
                Log.v("listsize:",collectionItems.size.toString())
                for (i in 0..collectionInfos.size-1){
                    Log.v("which",i.toString())
                    collectionItems[i]=collectionInfos[i].name
                }
                builder.setTitle("Add to a collection")
                builder.setMultiChoiceItems(collectionItems, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        Log.v("choose collection", which.toString())
                        selectedCollections.add(collectionInfos[which])
                    })
                builder.setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, which ->
                        for (selectedCollection in selectedCollections){
                            var collection_recipes: ArrayList<RecipeShownFormat> = ArrayList()

                            db.collection("collections")
                                .whereEqualTo(FieldPath.documentId(), selectedCollection.id)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        Log.d("TAG", "${document.id} => ${document.data}")
                                        var collection_recipes = document.get("recipes") as ArrayList<MutableMap<String,Any>>
//                                        for(collection_recipe in document.get("recipes") as ArrayList<RecipeShownFormat>){
//                                            collection_recipes.add(collection_recipe)
//                                        }
                                        Log.v("recipes type",collection_recipes.toString())
                                        val new_recipe:MutableMap<String,Any> = HashMap()
                                        new_recipe["id"] = recipe.id
                                        new_recipe["title"] = recipe.title
                                        new_recipe["image"] = recipe.image!!
                                        collection_recipes.add(new_recipe)
                                        //collection_recipes.add(RecipeShownFormat(recipe.id,recipe.title,recipe.image!!))
                                        Log.v("recipes type",collection_recipes.toString())

                                        //firebase database can only be updated by map
                                        val updateMap: MutableMap<String, Any> = HashMap()
                                        updateMap.put("recipes", collection_recipes)

                                        document.reference
                                            .update(updateMap)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Database Update",
                                                    "recipe in collection: DocumentSnapshot successfully updated!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "Database Update",
                                                    "recipe in collection: Error updating document",
                                                    e
                                                )
                                            }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("TAG", "Error getting documents: ", exception)
                                }
                        }
                        Toast.makeText(this,"You have added collection: " + recipe.title + " to collections" ,Toast.LENGTH_LONG).show()
                    })
                builder.setNegativeButton("NO", DialogInterface.OnClickListener({ dialog, which ->
                }))
                val  dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }//listener
    }

}
