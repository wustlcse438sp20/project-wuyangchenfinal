package com.wustlcse438sp20.myrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.wustlcse438sp20.myrecipe.Fragments.LoginFragment
import com.wustlcse438sp20.myrecipe.Fragments.SignUpFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentAdapter: MyPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter=fragmentAdapter
        tab_main.setupWithViewPager(viewPager)
        // identify login status
        val currentUser = auth.currentUser
        if (currentUser != null){
            val bundle = Bundle()
            bundle.putString("user_email", currentUser?.email)
            val intent = Intent(this,MainPageActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount() : Int {
            return 2
        }

        override fun getItem(position: Int) : Fragment {
            return when (position) {
                0 -> {
                    LoginFragment()
                }
                else ->
                    SignUpFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Login"
                else ->"Sign Up"
            }
        }
    }

}
