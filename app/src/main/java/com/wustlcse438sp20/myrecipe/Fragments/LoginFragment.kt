package com.wustlcse438sp20.myrecipe.Fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.MainPageActivity
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_signin.setOnClickListener() {
            if (signin_email.text != null && signin_email.text.toString() != "" && signin_password.text != null && signin_password.text.toString() != "") {
                val email = signin_email.text.toString()
                val password = signin_password.text.toString()
                button_signin.isEnabled = false
                signIn(email, password)
            }
        }
    }

    fun signIn(email:String,password:String){
        Toast.makeText(context, "Please Wait",
            Toast.LENGTH_SHORT).show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    button_signin.isEnabled = true
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_LONG).show()
                    button_signin.isEnabled = true
                }
            }
    }

    fun updateUI(user: FirebaseUser?){
        val intent = Intent(context, MainPageActivity::class.java)
        val bundle = Bundle()
        bundle.putString("user_email", user?.email)
        intent.putExtras(bundle)
        activity?.startActivity(intent)
    }




}
