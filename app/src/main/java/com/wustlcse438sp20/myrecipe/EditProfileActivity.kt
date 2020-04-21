package com.wustlcse438sp20.myrecipe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import android.graphics.drawable.BitmapDrawable
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.storage.FirebaseStorage
//import sun.font.LayoutPathImpl.getPath
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import android.R.attr.data
import android.graphics.BitmapFactory
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.ByteArrayOutputStream



class EditProfileActivity : AppCompatActivity() {

    private var user_email: String = ""
    private lateinit var db: FirebaseFirestore
    private lateinit var array_adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_edit_profile)

//        val bundle = intent.extras
//        user_email = bundle!!.getString("user_email")!!

        // set an instance of firebase
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)

        val spinner: Spinner = spinner_edit_goal
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.goal_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            array_adapter = adapter
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //take pic
        edit_image_button.setOnClickListener(){
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),1)
            }else{
                val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cIntent,1)
            }

        }

        save_profile_button.setOnClickListener(){

            db.collection("userProfile")
                .whereEqualTo("email", user_email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        Log.v("更新","user profile")

                        val updateMap: MutableMap<String, Any> = HashMap()
                        var username = edit_username.text.toString()
                        var height = edit_height.text.toString().toInt()
                        var weight = edit_weight.text.toString().toInt()
                        var goal = spinner.getSelectedItem().toString()
                        var image = user_email+"_profile.jpg"
                        //val bitmap:Bitmap = (edit_user_image.getDrawable() as BitmapDrawable).bitmap

                        updateMap.put("image", image)
                        updateMap.put("username", username)
                        updateMap.put("height", height)
                        updateMap.put("weight", weight)
                        updateMap.put("goal", goal)

                        document.reference
                            .update(updateMap)
                            .addOnSuccessListener {
                                Log.d(
                                    "Database Update",
                                    "更新用户简介！！！"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "Database Update",
                                    "未能更新用户简介！！！",
                                    e
                                )
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
            finish()
        }

        cancel_profile_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart(){
        super.onStart()
        //edit_user_image.setImageResource(R.drawable.no_image_found)

        db.collection("userProfile")
            .whereEqualTo("email", user_email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    Log.v("更新","user profile")
                    if (document.get("image") !== null) {
                        val image_url = document.get("image").toString()
                        // load firebase storage image
                        val storage = FirebaseStorage.getInstance()
                        val storageRef = storage.reference
                        storageRef.child(user_email+"_profile.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener {
                            // Data for "images/user_email_profile.jpg" is returned, use this as needed
                            var bitmap:Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                            Log.v("成功加载 bitmap",bitmap.toString())
                            edit_user_image.setImageBitmap(bitmap)
                        }.addOnFailureListener {
                            // Handle any errors
                            Log.v("加载失败 bitmap","")
                        }
                    }
                    edit_username.setText(document.get("username").toString())
                    edit_height.setText(document.get("height").toString())
                    edit_weight.setText(document.get("weight").toString())
                    var spinner_selected = document.get("goal").toString()
                    spinner_edit_goal.setSelection(array_adapter.getPosition(spinner_selected))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val bitmap = data!!.extras!!["data"] as Bitmap
                Log.v("照相机返回",bitmap.toString())
                edit_user_image.setImageBitmap(bitmap)

                // save to firebase storage
                val storage:FirebaseStorage = FirebaseStorage.getInstance()
                // Create a storage reference from our app
                val storageRef = storage.getReferenceFromUrl("gs://final-project-cfa98.appspot.com")
                // Create a reference to "user_email_profile.jpg"
                val profileRef = storageRef.child(user_email+"_profile.jpg")

                var baos:ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                var data = baos.toByteArray()
                val uploadTask = profileRef.putBytes(data)
                uploadTask
                    .addOnFailureListener(OnFailureListener {
                    // Handle unsuccessful uploads
                    })
                    .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        val downloadUrl = taskSnapshot.getStorage().downloadUrl
                        Log.v("download url",downloadUrl.toString())
                    })
            }
        }
    }

}