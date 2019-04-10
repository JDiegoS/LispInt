package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class NuevoUsuario : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var user: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: CollectionReference
    private lateinit var dbReference0: DatabaseReference
    private lateinit var database0: FirebaseDatabase
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_usuario)

        name=findViewById(R.id.name)
        user=findViewById(R.id.user)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)

        progressBar= findViewById(R.id.progressBar)

        database0 = FirebaseDatabase.getInstance()
        database= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()

        dbReference0 = database0.reference.child("user")
        dbReference=database.collection("usuario")
    }

    fun register(view: View){
        createNewAccount()
    }

    private fun createNewAccount(){
        val name:String=name.text.toString()
        val userB:String=user.text.toString()
        val email:String=email.text.toString()
        val password:String=password.text.toString()

        if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(userB) &&!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task ->

                if (task.isComplete){

                    val user: FirebaseUser? = auth.currentUser
                    verifyEmail(user)

                    val user0 = Usuario(name, email, password, ArrayList<Carpeta>()).toMap()

                    database.collection("usuarios")
                            .add(user0)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(this,"Usuario agregado a BD",Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,"Usuario no ha sido agregado",Toast.LENGTH_LONG).show()
                            }
                    /**val userBD = database0.child(user?.uid!!)
                    userBD.child("Name").setValue(name)
                    userBD.child("User").setValue(user)
                    userBD.child("Email").setValue(email)
                    userBD.child("Password").setValue(password)**/
                    progressBar.visibility=View.INVISIBLE
                    action()
                }else{

                }


            }

        }
    }
    private fun action(){
        startActivity(Intent(this,Login::class.java))
    }

    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
                ?.addOnCompleteListener(this){
                    task ->
                    if(task.isComplete){

                        Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"Error al enviar correo",Toast.LENGTH_LONG).show()
                    }
                }

    }
}
