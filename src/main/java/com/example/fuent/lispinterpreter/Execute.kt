package com.example.fuent.lispinterpreter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Execute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execute)

        var intent = intent
        val name = intent.getStringExtra("lisp")

        val result = findViewById<TextView>(R.id.textView4)

        result.text = name
    }


    }

