package com.wustlcse438sp20.myrecipe.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nitrico.lastadapter.LastAdapter
import com.wustlcse438sp20.myrecipe.Data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.R
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

    private var recipeList: ObservableList<RecipeByIngredients> = ObservableArrayList()
    private lateinit var lastAdapter: LastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val recyclerView: RecyclerView = recyclerView_mainpage
        recyclerView_mainpage.layoutManager = LinearLayoutManager(context)
        lastAdapter = LastAdapter(recipeList,BR.item)
            .map<RecipeByIngredients>(R.layout.item_recipe)
            .into(recyclerView)
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
        //searchView
        recipe_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_LONG).show()
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

}
