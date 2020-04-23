package com.wustlcse438sp20.myrecipe

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.wustlcse438sp20.myrecipe.Adapter.MealAdapter
import com.wustlcse438sp20.myrecipe.Adapter.PlanAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.MealViewModel
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.activity_meal_analysis_activity.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class MealAnalysisActivity : AppCompatActivity() {
    lateinit var mealViewModel: MealViewModel
    private lateinit var db : FirebaseFirestore
    private var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_analysis_activity)
        mealViewModel = ViewModelProviders.of(this).get(MealViewModel::class.java)
        db = FirebaseFirestore.getInstance()
        date = intent.getStringExtra("date")
        meal_time.text = date
        meal_time.setOnClickListener {
            mealViewModel.saveEx()
        }
        mealViewModel.queryMealByDay()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val list = arrayListOf<Meal>(
            Meal(1,"https://spoonacular.com/recipeImages/933310-312x231.jpg",1,1,"Manicotti"),
            Meal(1,"https://spoonacular.com/recipeImages/933310-312x231.jpg",1,1,"Manicotti")
        )

        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val ref = db.collection("mealPlan").document(intent.getStringExtra("user_email"))
            ref.get()
                .addOnSuccessListener {document ->
                    if (document != null) {
                        Log.e("TAG", "DocumentSnapshot data: ${document.data}")
                        if (document.data!![date] != null) {
                            val map = document.data!![date] as ArrayList<Map<String, Any>>
                            Log.e("TAG", map.toString())
                            recyclerView.adapter = PlanAdapter(map)
                        } else {
                            val builder: AlertDialog.Builder? = this.let { AlertDialog.Builder(it) }
                            builder?.setMessage("There are no plans yet")
                            builder?.setPositiveButton("Confirm",
                                DialogInterface.OnClickListener{ dialog, which->
                                    finish()
                                })
                            builder?.create()?.show()
                        }
                    } else {
                        Log.e("TAG", "No such document")
                    }
                }
                .addOnFailureListener { e -> Log.e("MSG", "Error updating document", e) }


        mealViewModel.getMealplanner()
        mealViewModel.mealList.observe(this, Observer {
            caloriesText.text ="Calories:"+ it.nutrients.calories.toString()
            fatText.text ="Fat:"+ it.nutrients.fat.toString()
            protein.text ="for protein:"+ it.nutrients.protein.toString()

        })
    }
}
