package com.example.fuent.lispinterpreter

import android.view.View

class Usuario( var nombre: String, var correo: String, var contrasena: String, var listaCarpeta: ArrayList<Carpeta>) {

    constructor() : this("","","", arrayListOf())

    fun toMap () : HashMap<String, Any>{
        val res = HashMap<String, Any>()
        res["nombre"] = nombre
        res["correo"] = correo
        res["contrasena"] = contrasena
        res ["listaCarpeta"] = listaCarpeta
        return res
    }

    /**fun addCarpeta(carpeta : Carpeta) {
        listaCarpeta.add(carpeta)
    }**/

}