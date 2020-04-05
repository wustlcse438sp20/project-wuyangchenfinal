package com.wustlcse438sp20.myrecipe.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import kotlinx.android.synthetic.main.fragment_recipe.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RecipeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeFragment : Fragment() {

    private var recipeList: ArrayList<RecipeByIngredients> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecipeAdapter
    lateinit var recipeviewModel:RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView Adapter
        recyclerView = recyclerView_mainpage
        adapter = RecipeAdapter(context,recipeList)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = adapter

        //viewmodel
        recipeviewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        //Get random recipe
        recipeviewModel.searchRecipeByRandom()
        //data oberserve
        recipeviewModel.recipeList.observe(this, Observer{
            recipeList.clear()
            recipeList.addAll(it)
            adapter.notifyDataSetChanged()
        })
        recipeviewModel.recipeInformation.observe(this, Observer {
          Log.v("random",it.recipes[0].sourceUrl+"")
        })
        //searchView
        recipe_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_LONG).show()
                recipeviewModel.searchRecipeByIngredients("apple")
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

    }

}
