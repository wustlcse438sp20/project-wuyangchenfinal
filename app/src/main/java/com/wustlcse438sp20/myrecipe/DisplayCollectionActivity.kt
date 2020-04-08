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
    var collectionId:Int = 0
    lateinit var recipeviewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_collection)
        val bundle = intent.extras
        collectionId = bundle!!.getInt("collectionId")


        display_collection_return_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        //create some data
        collectionList.clear()
        var recipeList: ArrayList<RecipeShownFormat> = ArrayList()
        recipeList.add(RecipeShownFormat(633508,"Baked Cheese Manicotti","Baked-Cheese-Manicotti-633508.jpg"))
        collectionList.add(Collection(1,"American Food","this collection contains American food",recipeList))
        recipeList.clear()
        recipeList.add(RecipeShownFormat(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeImages/716429-556x370.jpg"))
        collectionList.add(Collection(2,"Foreign Food","this collection contains Foreign food",recipeList))

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
        collectionList.filter { it.id == collectionId }.forEach {
            recipeList.addAll(it.recipes)
            display_collection_name.text = it.name
        }
        adapter.notifyDataSetChanged()

    }

}