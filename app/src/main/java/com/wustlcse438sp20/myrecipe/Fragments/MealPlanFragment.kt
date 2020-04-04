package com.wustlcse438sp20.myrecipe.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wustlcse438sp20.myrecipe.R



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MealPlanFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MealPlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MealPlanFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_plan, container, false)
    }


}
