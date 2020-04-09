package com.wustlcse438sp20.myrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wustlcse438sp20.myrecipe.Adapter.MealAdapter
import com.wustlcse438sp20.myrecipe.ViewModels.MealViewModel
import com.wustlcse438sp20.myrecipe.data.Meal
import kotlinx.android.synthetic.main.activity_smart_meal.*

class SmartMealActivity : AppCompatActivity() {
    lateinit var mealViewModel: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_meal)

        mealViewModel = ViewModelProviders.of(this).get(MealViewModel::class.java)
        mealViewModel.getMealplanner()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mealViewModel.mealList.observe(this, Observer {
            val list = arrayListOf<Meal>()
            list.addAll(it.meals)
            recyclerView.adapter = MealAdapter(list)
        })
    }
}
