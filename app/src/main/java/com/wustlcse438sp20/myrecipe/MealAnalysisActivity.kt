package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wustlcse438sp20.myrecipe.Adapter.MealAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.MealViewModel
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.activity_meal_analysis_activity.*


class MealAnalysisActivity : AppCompatActivity() {
    lateinit var mealViewModel: MealViewModel
    var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = applicationContext as MyApplication
        user_email = globalVariable.getEmail()!!
        setContentView(R.layout.activity_meal_analysis_activity)
        mealViewModel = ViewModelProviders.of(this).get(MealViewModel::class.java)
        val string = intent.getStringExtra("date")
        meal_time.text = string
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
        recyclerView.adapter = MealAdapter(list)

    }
}
