package com.wustlcse438sp20.myrecipe

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
//        val bundle = intent.extras
//        val recipeId = bundle!!.getInt("recipeId")

        val spinner: Spinner = spinner_edit_goal
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.goal_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        save_profile_button.setOnClickListener(){
            finish()
        }

        cancel_profile_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart(){
        super.onStart()
        edit_user_image.setImageResource(R.drawable.profile_image)
    }

}