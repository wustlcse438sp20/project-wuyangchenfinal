package com.wustlcse438sp20.myrecipe.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.EditProfileActivity
import com.github.nitrico.lastadapter.LastAdapter
import com.wustlcse438sp20.myrecipe.Adapter.CollectionAdapter
import com.wustlcse438sp20.myrecipe.AddCollectionActivity
import com.wustlcse438sp20.myrecipe.DisplayCollectionActivity

import com.wustlcse438sp20.myrecipe.R
import kotlinx.android.synthetic.main.fragment_profile.*
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeInformation
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var collectionList: ArrayList<Collection> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CollectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //create some data
        var recipeList: List<RecipeInformation> = ArrayList()
        collectionList.add(Collection(1,"Asian Food","this collection contains Asian food",recipeList))
        collectionList.add(Collection(2,"American Food","this collection contains American food",recipeList))

        //RecyclerView Adapter
        recyclerView = profile_collection_recyclerview
        adapter = CollectionAdapter(context,collectionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object: CollectionAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val intent = Intent(context, DisplayCollectionActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("collectionId",collectionList[position].id)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }
        })


        edit_profile_button.setOnClickListener() {
            val intent = Intent(context, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        add_collection_button.setOnClickListener(){
            val intent = Intent(context, AddCollectionActivity::class.java)
            activity?.startActivity(intent)
        }
    }


}
