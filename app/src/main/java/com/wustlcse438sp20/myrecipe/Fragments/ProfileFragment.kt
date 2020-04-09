package com.wustlcse438sp20.myrecipe.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.EditProfileActivity
import com.wustlcse438sp20.myrecipe.Adapter.CollectionAdapter
import com.wustlcse438sp20.myrecipe.AddCollectionActivity
import com.wustlcse438sp20.myrecipe.DisplayCollectionActivity

import com.wustlcse438sp20.myrecipe.R
import kotlinx.android.synthetic.main.fragment_profile.*
import com.wustlcse438sp20.myrecipe.data.Collection
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
        collectionList.clear()
        var temp_recipeList1: ArrayList<RecipeShownFormat> = ArrayList()
        temp_recipeList1.add(RecipeShownFormat(633508,"Baked Cheese Manicotti","Baked-Cheese-Manicotti-633508.jpg"))
        collectionList.add(Collection(1,"American Food","this collection contains American food",temp_recipeList1))
        var temp_recipeList2: ArrayList<RecipeShownFormat> = ArrayList()
        temp_recipeList2.add(RecipeShownFormat(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeImages/716429-556x370.jpg"))
        collectionList.add(Collection(2,"Foreign Food","this collection contains Foreign food",temp_recipeList2))

        //RecyclerView Adapter
        recyclerView = profile_collection_recyclerview
        adapter = CollectionAdapter(context,collectionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object: CollectionAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                Log.v("Click on Collection",position.toString())
                val intent = Intent(context, DisplayCollectionActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("collectionId",collectionList[position].id)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }
        })

        profile_username.text = "Jack"
        profile_height.text  = "height: 175cm"
        profile_weight.text  = "weight: 70kg"
        profile_goal.text  = "goal: Gain Muscle"
        profile_user_image.setImageResource(R.drawable.profile_image)


        edit_profile_button.setOnClickListener() {
            val intent = Intent(context, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        add_collection_button.setOnClickListener(){
            val intent = Intent(context, AddCollectionActivity::class.java)
            //to test displaycollectionactivity
//            val intent = Intent(context, DisplayCollectionActivity::class.java)
//            var bundle=Bundle()
//            bundle.putInt("collectionId",1)
//            intent.putExtras(bundle)
            activity?.startActivity(intent)
        }
    }


}

