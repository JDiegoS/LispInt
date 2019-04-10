package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot


class NuevaCarpeta : AppCompatActivity() {

    private lateinit var guardarButton : Button
    private lateinit var atrasButton : Button
    private lateinit var nameText : EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var dbCollection : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_carpeta)
        nameText = findViewById(R.id.nameText)
        dbCollection = FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
    }

    fun save (view : View){
       // var carpeta = Carpeta(nameText.toString(), ArrayList<Lisp>()).toMap()
        try{
            dbCollection.collection("usuarios").whereEqualTo("correo", (this.application as MyApplication).getUser()).limit(1).get().addOnCompleteListener { task->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!= null) {
                        var u0 = document.toObjects(Usuario :: class.java)[0].listaCarpeta
                        u0.add(Carpeta(nameText.toString(),arrayListOf()))

                        //u0!!.listaCarpeta.add(Carpeta(nameText.text.toString(),arrayListOf()))

                        var dbRef =dbCollection.collection("usuarios").document()

                        dbRef.update("listaCarpeta",u0.toList()).addOnCompleteListener(this) {
                            task ->
                            if (task.isSuccessful){
                                Toast.makeText(this, "Carpeta agregada",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this,"No se pudo agregar la carpeta", Toast.LENGTH_LONG).show()
                            }

                        }

                        var intent = Intent(this, Carpetas::class.java)
                        startActivity(intent)
                    } else {

                        //   Log.d(FragmentActivity.TAG, "No such document")
                        Toast.makeText(this, "No existe el documento", Toast.LENGTH_LONG).show()
                        var u = Usuario("Marco","fua18188","Marco",arrayListOf())
                        //u.addCarpeta(Carpeta("Prueba", arrayListOf()))
                        var map = u.toMap()
                        dbCollection.collection("usuarios").document(auth.uid!!).set(map)
                        //TODO solucionar esto
                    }
                } else {
                    // Log.d(FragmentActivity.TAG, "get failed with ", task.exception)
                    Toast.makeText(this, "Fallo con ${task.exception}", Toast.LENGTH_LONG).show()
                }
            }
        }catch(e:Exception){}
    }


    fun back (view: View){
        val intent = Intent(this, Carpetas :: class.java)
        startActivity(intent)
    }

    /**fun arrayToMap(c : ArrayList<Carpeta>): HashMap<String, String>{
        var res = HashMap<String, String>()

    }**/
}
