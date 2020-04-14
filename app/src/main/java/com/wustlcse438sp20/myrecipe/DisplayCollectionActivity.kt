package com.wustlcse438sp20.myrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.activity_display_collection.*

class DisplayCollectionActivity : AppCompatActivity() {

    private var collectionList: ArrayList<Collection> = ArrayList()
    private var recipeList: ArrayList<RecipeShownFormat> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecipeAdapter
    var collection_id:String = ""
    private lateinit var db : FirebaseFirestore
    lateinit var recipeviewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_collection)
        val bundle = intent.extras
        collection_id = bundle!!.getString("collectionId")!!

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings


        display_collection_return_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        val intent = intent
        val bundle = intent.extras
        collection_id = bundle!!.getString("collectionId")!!

        //create some data
        collectionList.clear()
//        var temp_recipeList1: ArrayList<RecipeShownFormat> = ArrayList()
//        temp_recipeList1.add(RecipeShownFormat(633508,"Baked Cheese Manicotti","https://spoonacular.com/recipeImages/Baked-Cheese-Manicotti-633508.jpg"))
//        collectionList.add(Collection(1,"American Food","this collection contains American food",temp_recipeList1))
//        var temp_recipeList2: ArrayList<RecipeShownFormat> = ArrayList()
//        temp_recipeList2.add(RecipeShownFormat(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeImages/716429-556x370.jpg"))
//        collectionList.add(Collection(2,"Foreign Food","this collection contains Foreign food",temp_recipeList2))

        db.collection("collections")
            .whereEqualTo(FieldPath.documentId(), collection_id)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("search success !!!!!!!!!!!!!!!!!!")
                    for (document in task.result!!) {
                        display_collection_name.text = document.get("name").toString()
                            for(recipe in document.get("recipes") as List<RecipeShownFormat>){
                            recipeList.add(recipe)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                else {
                    Log.v("Search in database", "Fail")
                    println("failed to get user data")
                }
            })

        //RecyclerView Adapter
        recyclerView = recipe_in_collection_recyclerview
        adapter = RecipeAdapter(this,recipeList)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object :RecipeAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val detail_intent = Intent(this@DisplayCollectionActivity, RecipeInformationActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("recipeId", recipeList[position].id)
                Log.v("send recipeId to detail",recipeList[position].id.toString())
                detail_intent.putExtras(bundle)
                startActivity(detail_intent)
            }
        })

        // get collection from collectionId
//        Log.v("get collectionId",collection_id.toString())
//        collectionList.filter { it.id == collection_id }.forEach {
//            Log.v("filter collectionId",it.id.toString())
//            Log.v("recipes",it.recipes.toString())
//            Log.v("collection for id=1",it.toString())
//            recipeList.clear()
//            recipeList.addAll(it.recipes)
//            display_collection_name.text = it.name
//        }
//        adapter.notifyDataSetChanged()

    }

}