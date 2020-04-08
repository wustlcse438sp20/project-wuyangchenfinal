package com.wustlcse438sp20.myrecipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_collection.*

class AddCollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_collection)

        save_collection_button.setOnClickListener(){
            finish()
        }

        cancel_collection_button.setOnClickListener(){
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
    }

}