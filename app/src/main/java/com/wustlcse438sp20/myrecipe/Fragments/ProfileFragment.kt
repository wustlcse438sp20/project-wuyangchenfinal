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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectionAdapter
    private var user_email = ""
    private var user_collections = ArrayList<String>()
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity_intent = activity!!.intent
        user_email = activity_intent!!.extras!!.getString("user_email")!!

        // set an instance of firebase
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)


        // display profile for this user
        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("search user profile success !!!!!!!!!!!!!!!!!!")
                    for (document in task.result!!) {
                        //totalChips = document.get("chips").toString().toInt()
                        profile_email.text = document.get("email").toString()
                        profile_username.text = document.get("username").toString()
                        profile_height.text  = document.get("height").toString()
                        profile_weight.text  = document.get("weight").toString()
                        profile_goal.text  = document.get("goal").toString()
                        if (document.get("image") !== null)
                            Picasso.get().load(document.get("image").toString()).into(profile_user_image)
                        user_collections = document.get("collections") as ArrayList<String>
                        //profile_user_image.setImageResource(R.drawable.profile_image)


                        // display collections for this user
                        for(collection_id in user_collections){
                            db.collection("collections")
                                .whereEqualTo(FieldPath.documentId(), collection_id)
                                .get()
                                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                    if (task.isSuccessful) {
                                        Log.v("Search in database", "Sucess")
                                        println("search collection success !!!!!!!!!!!!!!!!!!")
                                        for (document in task.result!!) {
                                            collectionList.add(Collection(collection_id, document.get("name").toString(), document.get("description").toString(), document.get("recipes") as ArrayList<RecipeShownFormat>))
                                        }
                                        adapter.notifyDataSetChanged()
                                    }
                                    else {
                                        Log.v("Search in database", "Fail")
                                        println("failed to get collection data")
                                    }
                                })
                        }
                    }
                } else {
                    Log.v("Search in database", "Fail")
                    println("failed to get user profile data")
                }
            })
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
//        var temp_recipeList1: ArrayList<RecipeShownFormat> = ArrayList()
//        temp_recipeList1.add(RecipeShownFormat(633508,"Baked Cheese Manicotti","Baked-Cheese-Manicotti-633508.jpg"))
//        collectionList.add(Collection("1","American Food","this collection contains American food",temp_recipeList1))
//        var temp_recipeList2: ArrayList<RecipeShownFormat> = ArrayList()
//        temp_recipeList2.add(RecipeShownFormat(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeImages/716429-556x370.jpg"))
//        collectionList.add(Collection("2","Foreign Food","this collection contains Foreign food",temp_recipeList2))

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
                bundle.putString("collectionId",collectionList[position].id)
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
            var bundle=Bundle()
            bundle.putString("user_email",user_email)
            intent.putExtras(bundle)
            activity?.startActivity(intent)
        }
    }


}

