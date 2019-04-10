package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Archivos : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var dbCollection : FirebaseFirestore
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivos)

        dbCollection = FirebaseFirestore.getInstance()

        var lista: ListView = findViewById(R.id.lista)

        var listaI: ArrayList<String> = arrayListOf()

        var user = (this.application as MyApplication).getUser()

        dbCollection.collection("usuarios").whereEqualTo("correo",user).limit(1).get().addOnCompleteListener(){
            task->
            if (task.isSuccessful){
                val document = task.result
                if (document != null){
                    var u0 = document.toObjects(Usuario :: class.java)[0].listaCarpeta[0].codigos
                    var u : ArrayList<String> = arrayListOf()
                    for (i in u0){
                        u.add(i.nombre)
                    }

                    val adapter : ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, u)
                    lista.adapter=adapter
                }
            }
        }

        lista.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val intent = Intent(this, Editor::class.java)
            startActivity(intent)
        }

    }

    fun back(view: View){
        val intent = Intent(this,Carpetas::class.java)
        startActivity(intent)
    }
}
