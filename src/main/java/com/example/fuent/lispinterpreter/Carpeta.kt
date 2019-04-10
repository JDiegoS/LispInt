package com.example.fuent.lispinterpreter

class Carpeta (var nombre : String, var codigos: ArrayList<Lisp>){

    constructor():this("",arrayListOf()){}

    fun addCodigo (codigo : Lisp) {
        codigos.add(codigo)
    }

    fun toMap(): HashMap<String,Any>{
        val res = HashMap<String, Any>()
        res["nombre"] = nombre
        res["codigos"]=ArrayList<Lisp>()
        return res
    }
}