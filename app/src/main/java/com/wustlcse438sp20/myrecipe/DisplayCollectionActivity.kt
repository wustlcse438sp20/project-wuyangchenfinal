package com.wustlcse438sp20.myrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.activity_display_collection.*

class DisplayCollectionActivity : AppCompatActivity() {

    private var recipeList: ArrayList<RecipeShownFormat> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecipeAdapter
    lateinit var recipeviewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_collection)
        val bundle = intent.extras
        val collectionId = bundle!!.getInt("collectionId")


        display_collection_return_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        var recipeId = 633508

        //RecyclerView Adapter
        recyclerView = recipe_in_collection_recyclerview
        adapter = RecipeAdapter(this,recipeList)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object :RecipeAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val detail_intent = Intent(this@DisplayCollectionActivity, RecipeInformationActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("recipeId", recipeId)
                detail_intent.putExtras(bundle)
                startActivity(detail_intent)
            }
        })
    }

}