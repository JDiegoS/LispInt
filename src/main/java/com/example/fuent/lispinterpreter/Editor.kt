package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fuent.lispinterpreter.lispI.Atomo
import com.example.fuent.lispinterpreter.lispI.Errores.*
import com.example.fuent.lispinterpreter.lispI.LispInterpreter
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.classifier.CodeProcessor
import io.github.kbiakov.codeview.highlight.ColorTheme
import java.io.BufferedReader
import java.io.FileReader
import java.util.ArrayList
import java.util.HashMap

class Editor : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // train classifier on app start
        CodeProcessor.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)


        var codeView: CodeView = findViewById(R.id.code_view)

        var codigo_java = "(+ 1 2 3 4 6) \n" +
                "(+ 1 1) \n"  +
                "(* 4 7) \n" +
                "(/ 200 3) \n" +
                "(- 234 543) \n"

        codeView.setOptions(Options.Default.get(this)
                .withLanguage("java")
                .withCode(codigo_java)
                .withTheme(ColorTheme.MONOKAI))


        var code = codigo_java
        var cod = codigo_java
        var interpretado = ""
        val interpreter = LispInterpreter()







        val ejBtn = findViewById<Button>(R.id.ejecutar)

        ejBtn.setOnClickListener {
            val se = code.replace("\n", "sep")
            val parts = se.split("sep")
            Toast.makeText(this, parts.toString(), Toast.LENGTH_LONG).show()

            var n = 0
            for(i in parts){
                try {

                    val atomoAEvaluar = interpreter.parsearExpresion(parts[n], false, false)
                    interpretado += interpreter.evaluar(atomoAEvaluar).toString() + "\n"
                    n += 1
                }catch (error: Error_ExpresionMalBalanceada){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }catch (error: Error_OperacionNoExistente){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }catch (error: Error_OperandosIncorrectos){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }catch (error: Error_OutOfIndex){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }catch (error: Error_ParametrosIncorrectos){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }catch (error: Error_FuncionYaImplementada){
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }
                Toast.makeText(this, interpretado, Toast.LENGTH_LONG).show()

            }
            
        }
    }
}

