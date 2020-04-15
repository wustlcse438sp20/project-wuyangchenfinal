package com.wustlcse438sp20.myrecipe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.data.Collection
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.activity_add_collection.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class AddCollectionActivity : AppCompatActivity() {

    private var user_email:String = ""
    private var collection_id:String = ""
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_collection)

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings

    }

    override fun onStart() {
        super.onStart()

        val intent = intent
        val bundle = intent.extras
        user_email = bundle!!.getString("user_email")!!


        save_collection_button.setOnClickListener(){
            //create a new user
            val collection = Collection(
                "",
                edit_collection_name.text.toString(),
                edit_description.text.toString(),
                ArrayList<RecipeShownFormat>()
            )


//            for (model in mUserInitialPresenter.getUserInitialModel().getServicesOffered()) {
//                val servicesOffered = HashMap()
//                servicesOffered.put("serviceName", model.getServiceName())
//                servicesOffered.put("price", model.getPrice())
//                list.add(servicesOffered)
//            }
//            dataMap.put("servicesOffered", list)

            //store values for the database
            val recipes:ArrayList<MutableMap<String,Any>> = ArrayList()
            val collectionMap: MutableMap<String, Any> = HashMap()
            collectionMap["name"] = collection.name
            collectionMap["description"] = collection.description
            collectionMap["recipes"] = recipes


            // Add a new collection to collections database with a generated documentID
            db.collection("collections")
                .add(collectionMap)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                    Toast.makeText(this,  "collection created in the database!",Toast.LENGTH_LONG).show()
                    collection_id = documentReference.id

                    // update in userProfile database
                    db.collection("userProfile")
                        .whereEqualTo("email", user_email)
                        .get()
                        .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                            if (task.isSuccessful) {
                                Log.v("Search in database", "Sucess")
                                println("search success !!!!!!!!!!!!!!!!!!")
                                for (document in task.result!!) {
                                    var collections: ArrayList<String> = document.get("collections") as ArrayList<String>
                                    collections.add(collection_id)

                                    document.reference
                                        .update("collections",collections)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "Database Update",
                                                "collection in userProfile: DocumentSnapshot successfully updated!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "Database Update",
                                                "collection in userProfile: Error updating document",
                                                e
                                            )
                                        }
                                }
                            } else {
                                Log.v("Search in database", "Fail")
                                println("failed to get user data")
                            }
                        })
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(this, "Failed to create collection in the database!", Toast.LENGTH_LONG)
                })

            finish()
        }

        cancel_collection_button.setOnClickListener(){
            finish()
        }
    }

}