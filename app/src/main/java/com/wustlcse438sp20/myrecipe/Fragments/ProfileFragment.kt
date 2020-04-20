package com.wustlcse438sp20.myrecipe.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
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
import com.google.firebase.storage.FirebaseStorage
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
    private var tempCollectionList: ArrayList<Collection> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectionAdapter
    private var user_email = ""
    private var user_collections = ArrayList<String>()
    private lateinit var db: FirebaseFirestore
    private var onResumeFlag: Boolean = false


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

        // display profile and collections
        updateDisplay()
    }

    fun updateDisplay(){
        // display profile and collections for this user
        tempCollectionList.clear()
        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                if (task.isSuccessful) {
                    Log.v("Search in database", "Sucess")
                    println("search user profile success !!!!!!!!!!!!!!!!!!")
                    for (document in task.result!!) {
                        profile_email.text = document.get("email").toString()
                        profile_username.text = document.get("username").toString()
                        profile_height.text  = document.get("height").toString()
                        profile_weight.text  = document.get("weight").toString()
                        profile_goal.text  = document.get("goal").toString()
                        if (document.get("image") !== null)if (document.get("image") !== null) {
                            val image_url = document.get("image").toString()
                            // load firebase storage image
                            val storage: FirebaseStorage = FirebaseStorage.getInstance()
                            val storageRef = storage.getReferenceFromUrl("gs://final-project-cfa98.appspot.com")
                            var profileRef = storageRef.child(image_url)
                            val ONE_MEGABYTE: Long = 1024 * 1024
                            profileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                                // Data for "images/user_email_profile.jpg" is returned, use this as needed
                                var bitmap: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                                Log.v("成功加载 bitmap",bitmap.toString())
                                profile_user_image.setImageBitmap(bitmap)
                            }.addOnFailureListener {
                                // Handle any errors
                                Log.v("加载失败 bitmap","")
                            }
                        }
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
                                            Log.v("!!!document recipes",document.get("recipes").toString())
                                            tempCollectionList.add(Collection(collection_id, document.get("name").toString(), document.get("description").toString(), document.get("recipes") as ArrayList<RecipeShownFormat>))
                                        }
                                        collectionList = tempCollectionList
                                        Log.v("跑到collectionList",collectionList.toString())
                                        adapter.notifyDataSetChanged()
                                        //Log.v("adapter","更新啦啦啦")
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

        // collection RecyclerView Adapter
        recyclerView = profile_collection_recyclerview
        adapter = CollectionAdapter(context, collectionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object: CollectionAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                Log.v("Click on Collection",position.toString())
                val intent = Intent(context, DisplayCollectionActivity::class.java)
                var bundle = Bundle()
                bundle.putString("collectionId",collectionList[position].id)
                //bundle.putString("user_email",user_email)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }
        })


        edit_profile_button.setOnClickListener() {
            val intent = Intent(context, EditProfileActivity::class.java)
            var bundle=Bundle()
            bundle.putString("user_email",user_email)
            intent.putExtras(bundle)
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

    override fun onPause() {
        super.onPause()
        onResumeFlag = true
    }


    override fun onResume() {
        super.onResume()

        if(onResumeFlag){
            var mUpdatePageHandler: Handler = Handler()
            mUpdatePageHandler.postDelayed(kotlinx.coroutines.Runnable {
                // display profile and collections
                updateDisplay()
            },1000)
        }
    }


}

