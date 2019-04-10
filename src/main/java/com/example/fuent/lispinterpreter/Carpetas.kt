package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Carpetas : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var dbCollection : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carpetas)

        dbCollection = FirebaseFirestore.getInstance()

        var atras : Button = findViewById(R.id.atrasButton)
        var lista : ListView = findViewById(R.id.lista)
        var nuevaCarpeta : ImageView = findViewById(R.id.nuevaCarpeta)
        var user = (this.application as MyApplication).getUser()
        var listaI : ArrayList<String> = arrayListOf()


        lista.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val intent = Intent(this, Archivos::class.java)
            startActivity(intent)
        }

        try{
            dbCollection.collection("usuarios").whereEqualTo("correo",user).limit(1).get().addOnCompleteListener(){
                task->
                if(task.isSuccessful){
                    val document = task.result
                    if (document!=null){
                        var user0 = document.toObjects(Usuario::class.java)
                        if (user0.size > 0){
                            var user = user0[0]
                            var lista0 = user.listaCarpeta
                            for (i in lista0){
                                listaI.add(i.nombre)
                            }
                        }else{
                            Toast.makeText(this, "Lista vacia", Toast.LENGTH_LONG).show()
                        }


                        val adapter : ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaI)
                        lista.adapter=adapter
                    }else{

                    }
                }else{
                    Toast.makeText(this, "Task != isSuccessful", Toast.LENGTH_LONG).show()
                }
            }
        }catch(e:Exception){}
    }
    fun atrasButtonClick(view: View){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun newCarpeta(view: View){

        val intent = Intent (this, NuevaCarpeta :: class.java)
        startActivity(intent)
/**
        db = FirebaseFirestore.getInstance()
        val nuevaCarpeta = HashMap<String, Any> ()
        nuevaCarpeta["nombre"] = "Nueva Carpeta"
        db.collection("usuario")**/
    }



}
