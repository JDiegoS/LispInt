package com.example.fuent.lispinterpreter

import android.R.attr.keySet



class Lisp(var nombre: String, var codigo: String) {

    constructor():this("","")

    fun toMap() : HashMap<String, Any>{
        val res = HashMap<String, Any>()
        res["nombre"] = nombre
        res["codigo"] = codigo
        return res
    }
}