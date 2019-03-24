package com.brianku.loginandcheckgrade

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login_check_grade.*

class LoginCheckGradeActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_check_grade)
        mAuth = FirebaseAuth.getInstance()

        val fb = FirebaseDatabase.getInstance()
        val table = fb.getReference("students")
        table.setValue("Hello Firebase")
    }

    override fun onStart() {
        super.onStart()

        mAuth.currentUser?.let{
            updateUI(it)
        }
    }


    fun loginClick(view: View){
        val name = name_field.text.toString()
        val password = password_filed.text.toString()
        if(name.trim().length == 0 || password.trim().length == 0) {
            Toast.makeText(this,"name and password must be fill out",Toast.LENGTH_SHORT).show()
            return
        }
        createAccount(name,password)
        name_field.setText("")
        password_filed.setText("")

    }


    fun signInClick(view:View){
        val name = sign_in_name.text.toString()
        val password = sign_in_password.text.toString()
        if(name.trim().length == 0 || password.trim().length == 0) {
            Toast.makeText(this,"name and password must be fill out",Toast.LENGTH_SHORT).show()
            return
        }
        signIn(name,password)
        sign_in_name.setText("")
        sign_in_password.setText("")
    }

    private fun signIn(name:String,password:String){
        mAuth.signInWithEmailAndPassword(name,password)
            .addOnCompleteListener(this){
              loginSignUpCompletion(it,"Sign In")
            }
    }

    private fun updateUI(user: FirebaseUser){
        user.email?.let{
            welcome_text.text = "Welcome $it!!"
        }
    }

    private fun createAccount(name:String,password:String){
        mAuth.createUserWithEmailAndPassword(name, password)
            .addOnCompleteListener(this){
                loginSignUpCompletion(it,"Sign up")
            }
    }

    private fun loginSignUpCompletion(task:Task<AuthResult>,taskName:String){
        if(task.isSuccessful){
            mAuth.currentUser?.let{
                updateUI(it)
            }
        }else{
            Toast.makeText(this@LoginCheckGradeActivity,"$taskName Failed!!",Toast.LENGTH_LONG).show()
        }
    }
}
