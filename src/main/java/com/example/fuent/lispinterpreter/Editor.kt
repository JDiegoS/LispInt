package com.example.fuent.lispinterpreter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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





        var codeView : CodeView = findViewById(R.id.code_view)

        var codigo_java = "(defun esta (numero) numero)\n" +
                "(defun prueba (num) (* (esta 5) 2))\n" +
                "(defun se (num) (+ (prueba 1) 10))\n" +
                "(defun ve (num) (- (se 1) 10))\n" +
                "(defun simple (num) (+ (ve 1) 80))\n" +
                "(defun pero (num) (- (simple 1) 80))\n" +
                "(defun pone (num) (+ (pero 1) 1))\n" +
                "(defun a (num) (/ (pone 1) 8))\n" +
                "(defun prueba (num) (* (a 1) 8))\n" +
                "(defun el (num) (- (prueba 1) 5))\n" +
                "(defun depth (num) (+ (el 1) 5))\n" +
                "(defun de (num) (/ (depth 1) 20))\n" +
                "(defun su (num) (* (de 1) 20))\n" +
                "(defun programa (num 1) (- (su) 3))\n" +
                "(defun y (num) (+ (programa 1) 3))\n" +
                "(defun eso (num) (/ (y 1) 5))\n" +
                "(defun me (num) (* (eso 1) 5))\n" +
                "(defun hace (num) (- (me 1) 1))\n" +
                "(defun feliz (num) (+ (hace 1) 1))\n" +
                "(feliz 1)\n"
        codeView.setOptions(Options.Default.get(this)
                .withLanguage("java")
                .withCode(codigo_java)
                .withTheme(ColorTheme.MONOKAI))


            var code = codigo_java
            var interpretado = ""

            try {

                //arraylist con todas las palabras importantes, para distinguirlas
                val palabrasClave = ArrayList<String>()
                palabrasClave.add("ATOM")
                palabrasClave.add("LIST")
                palabrasClave.add("EQUAL")
                palabrasClave.add("<")
                palabrasClave.add(">")
                palabrasClave.add("+")
                palabrasClave.add("-")
                palabrasClave.add("*")
                palabrasClave.add("/")
                palabrasClave.add("DEFUN")
                palabrasClave.add("COND")
                val funciones = HashMap<String, Funcion>()

                interpretado+= executeFun(splitInst(split(code)), funciones)

            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo interpretar el codigo", Toast.LENGTH_LONG).show()
            }



        val ejBtn = findViewById<Button>(R.id.ejecutar)

        ejBtn.setOnClickListener{


            val intent = Intent(this@Editor, Execute::class.java)
            intent.putExtra("lisp", interpretado)
            startActivity(intent)
        }


    }

    fun back(view: View){
        var intent = Intent(this, Archivos::class.java)
        startActivity(intent)
    }

    fun execute (view:View){
        Toast.makeText(this, "Funcionalidad pendiente...", Toast.LENGTH_LONG).show()
    }





    fun splitInst(codigo: ArrayList<String>): ArrayList<ArrayList<String>> {
        //inst referencia a las instrucciones
        val inst = ArrayList<String>()
        //referencia a temporal
        val temp = ArrayList<ArrayList<String>>()
        var parentesis = 0
        var str = ""
        //para recorrer toda la parte del string donde empieza y termina
        for (i in codigo.indices) {
            if (codigo[i] == "(") {
                parentesis++
                str += codigo[i]
            } else if (codigo[i] == ")") {
                parentesis--
                if (parentesis == 0) {
                    str += codigo[i]
                    //se agrega instruccion al arraylist
                    inst.add(str)
                    str = ""
                } else {
                    str += codigo[i]
                }
                //espacios
            } else {
                if (codigo[i + 1] != "(" || codigo[i + 1] != ")" || codigo[i + 1] != "+" || codigo[i + 1] != "-" || codigo[i + 1] != "*" || codigo[i + 1] != "/") {
                    str += " "
                }
                str += codigo[i]
            }
        }
        //ya no habra espacios " " en el arraylist
        for (a in inst.indices) {
            val ins = split(inst[a])
            temp.add(ins)
        }
        return temp
    }

    /**
     * @param funciones Es el hashmap de Funciones del programa
     * @param operaciones  Es un ArrayList
     * Cada instruccion es del tipo (ALGO ASDKCI HOLA SALU2 (/ 5 9) HAHA)
     * Recibe un arraylist de un arraylist de strings
     */
    fun executeFun(operaciones: ArrayList<ArrayList<String>>, funciones: HashMap<String, Funcion>): String {
        //diferenciar entre operadores y predicados
        var c = ""
        val operadoresA = ArrayList<String>()
        operadoresA.add("+")
        operadoresA.add("-")
        operadoresA.add("*")
        operadoresA.add("/")

        val predicados = ArrayList<String>()
        predicados.add("ATOM")
        predicados.add("LIST")
        predicados.add("EQUAL")

        //si encuentra un defun entonces usa el metodo y hace lo correspondiente al metodo
        for (i in operaciones.indices) {
            if (operaciones[i][1].toUpperCase() == "DEFUN") {
                defun(operaciones[i], funciones)
            }
        }


        for (i in operaciones.indices) {
            //Busca de operadores
            if (operadoresA.contains(operaciones[i][1])) {
                val prueba = convertir(operaciones[i])
                if (operaciones[i].size > 0) {
                    //System.out.println(evaluarParentesis(prueba, funciones)[0]);
                    c += evaluarParentesis(prueba, funciones)[0]
                    return c
                }
            } else if (operaciones[i][1].toUpperCase() == "COND") {
                //System.out.println(condicion(operaciones.get(i),funciones));
                c += condicion(operaciones.get(i), funciones)
                return c
            } else if (predicados.contains(operaciones[i][1].toUpperCase())) {
                //System.out.println(evaluarPredicados(convertir(operaciones.get(i))));
                c += evaluarPredicados(convertir(operaciones.get(i)))
                return c

            } else if (funciones.keys.contains(operaciones[i][1])) {
                val f = funciones[operaciones[i][1]]
                val params = ArrayList<String>()
                var j = 3
                while (operaciones[i][j] != ")") {
                    params.add(operaciones[i][j])
                    j++
                }
                if (f!!.initParam(params)) {
                    f.replaceParams()
                    //System.out.println(executeFunSingle(f.getInst(),funciones));
                    c += executeFunSingle(f.getInst(), funciones)
                }

            } else {
                //Si ninguna es True, no pasa nada
            }//Busqueda de llamadas de funciones
            //Busqueda de predicados
            //Busqueda de condicionales
        }
        return c
    }

    /**
     * @param operaciones es una arraylist de Strings
     * @param funciones es el hashmap de funciones del programa
     * @return la respuesta
     * En cambio este recibe solo un arraylist de strings y retorna la respuesta
     */
    fun executeFunSingle(operaciones: ArrayList<String>, funciones: HashMap<String, Funcion>): String {
        //se vuelven a diferenciar entre operadores y predicados
        val operadoresA = ArrayList<String>()
        var res = ""
        operadoresA.add("+")
        operadoresA.add("-")
        operadoresA.add("*")
        operadoresA.add("/")

        val predicados = ArrayList<String>()
        predicados.add("ATOM")
        predicados.add("LIST")
        predicados.add("EQUAL")

        //        for (int i = 0; i<operaciones.size(); i++){
        //            if (operaciones.get(i).get(1).toUpperCase().equals("DEFUN")){
        //                defun(operaciones.get(i), funciones);
        //            }
        //        }


        for (i in operaciones.indices) {
            //Busca de operadores
            if (operadoresA.contains(operaciones[i])) {
                val prueba = convertir(operaciones)
                if (operaciones.size > 0) {
                    res = java.lang.Double.toString(evaluarParentesis(prueba, funciones)[0])
                }
            } else if (operaciones[i].toUpperCase() == "COND") {
                res = condicion(operaciones, funciones)
            } else if (predicados.contains(operaciones[i].toUpperCase())) {
                res = evaluarPredicados(convertir(operaciones))
                //TODO no esta del todo completo, creo que no es del todo recursivo
            } else if (funciones.keys.contains(operaciones[i])) {
                val f = funciones[operaciones[i]]
                val params = ArrayList<String>()
                var j = 3
                while (operaciones[j] != ")") {
                    params.add(operaciones[j])
                    j++
                }
                if (f!!.initParam(params)) {
                    f.replaceParams()
                    res = executeFunSingle(f.inst, funciones)
                }

            } else {
                //Si ninguna es True, no pasa nada
            }//Busqueda de llamadas de funciones
            //Busqueda de predicados
            //Busqueda de condicionales
        }
        return res
    }

    /**
     * @param lista es un arraylist de string
     * @param funciones es el hashmap de las funciones
     * Metodo para que se interprete las definiciones de funciones de lisp
     */
    //Recibe de parametro un arraylist desde el (DEFUN...)
    fun defun(lista: ArrayList<String>, funciones: HashMap<String, Funcion>) {
        var parentesis = 0
        var c = 0
        //se instancia la clase funcion
        val f = Funcion()
        while (lista[c] != ")") {
            if (lista[c] == "(") {
                parentesis++
                //se agrega el nombre de la funcion
            } else if (parentesis == 1 && lista[c].toUpperCase() != "DEFUN" && lista[c].toUpperCase() != ")") {
                f.nombre = lista[c]
            } else if (parentesis == 2 && lista[c] != ")") {
                //se van agregando los parametros
                while (lista[c] != ")") {
                    f.addParam(lista[c])
                    c++
                }
                //agrega instrucciones
            } else if (parentesis > 2) {
                f.addInst("(")
                var a = 0
                for (i in c until lista.size - 1) {
                    f.addInst(lista[i])
                    a++
                }
                c = c + a - 1
            }
            c++
        }
        //agrega al hash map
        funciones[f.nombre] = f
    }

    /**
     * @param values Es la matriz que contiene los elementos dentro del parentesis
     * @return En la primera posicion, el resultado de la operacion aritmetica. En el segundo, el contador interno
     * Este metodo las operaciones aritmeticas guarda los operandos y operandores por parentesis
     */
    fun evaluarParentesis(values: Array<String>, funciones: HashMap<String, Funcion>): DoubleArray {
        //del hashmap
        val set = funciones.keys
        var op = ""
        //valores
        val `val` = ArrayList<Double>()
        var r = 0.0
        var contador = 1
        //mientras no termine un parentesis
        while (values[contador] != ")") {
            if (esNum(values[contador].toString())!!) {
                `val`.add(java.lang.Double.parseDouble(values[contador]))
                //los operandos
            } else if (values[contador] == "+" || values[contador] == "-" || values[contador] == "*" || values[contador] == "/") {
                op = values[contador].toString()
                //cuando empieza un parentesis
            } else if (values[contador] == "(") {
                //matriz nueva
                val val0 = arrayOfNulls<String>(values.size - (contador + 1))
                for (c in contador until values.size - 1) {
                    val0[c - contador] = values[c]
                }
                val respuesta = evaluarParentesis(val0, funciones)
                `val`.add(respuesta[0])
                contador = respuesta[1].toInt() + contador

            } else if (set.contains(values[contador])) {
                val f = funciones[values[contador]]
                if (values[contador + 1] == "(") {
                    contador = contador + 2
                    val params = ArrayList<String>()
                    while (values[contador] != ")") {
                        params.add(values[contador].toString())
                        contador++
                    }
                    if (f!!.initParam(params)) {
                        f.replaceParams()
                        `val`.add(evaluarParentesis(convertir(f.inst), funciones)[0])
                    }
                }
            }
            contador++
        }
        r = operacionAritmetica(op, `val`)
        val res = DoubleArray(2)
        res[0] = r
        res[1] = contador.toDouble()
        return res
    }

    /**
     * @param n String
     * @return Retorna un booleano de true o false que se utiliza en evaluarParentesis
     * Para saber si donde estan los operadores que se utilizan en el metodo de evaluarParentesis
     */
    fun esNum(n: String): Boolean? {
        var b: Boolean
        try {
            java.lang.Double.parseDouble(n)
            b = true
        } catch (e: Exception) {
            b = false
        }

        return b
    }

    /**
     * @param op es el operador
     * @param num es el numero
     * @return el valor de la operacion
     * Esta hace las operaciones que estan puestas en formato lisp y retorna el resultado de esa
     * operacion aritmetica
     */
    fun operacionAritmetica(op: String, num: ArrayList<Double>): Double {
        //tipo double mas precision que un float
        var res = num[0]
        //para los 4 operadores que se pueden usar
        when (op) {
            "+" -> for (i in 1 until num.size) {
                res = res + num[i]
            }
            "-" -> for (i in 1 until num.size) {
                res = res - num[i]
            }
            "*" -> for (i in 1 until num.size) {
                res = res * num[i]
            }
            //precaucion con la division entre cero
            "/" -> if (num[num.size - 1].toInt() == 0) {
                //Error - Division entre 0
                throw ArithmeticException("Division entre 0")
            } else {
                for (i in 1 until num.size) {
                    res = res / num[i]
                }
            }
        }
        //retorna el double, resultado de la operacion
        return res
    }

    /**
     * @param s es un string
     * @return un arraylist de strings
     * Este metodo es para quitar los espacios
     */
    fun split(s: String): ArrayList<String> {
        var s = s
        //nueva arraylist
        val str = ArrayList<String>()
        //para que siempre este en mayuscula
        s = s.toUpperCase()
        //matriz de strings
        val matriz = s.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //for (int i = 0 ; i< s.length(); i++)
        var i = 0
        while (i < s.length) {

            if (matriz[i] == "'" || matriz[i] == "(" || matriz[i] == ")" || matriz[i] == "+" || matriz[i] == "-" || matriz[i] == "*" || matriz[i] == "/") {
                //se agrega al arraylist de strings
                str.add(matriz[i])
            } else if (matriz[i] != " ") {
                var continuar: Boolean? = true
                var st = ""
                var contador = 0
                while (continuar!!) {
                    st += matriz[contador + i]
                    contador++
                    if (matriz[contador + i] == "(" || matriz[contador + i] == ")" || matriz[contador + i] == "+" || matriz[contador + i] == "-" || matriz[contador + i] == "*" || matriz[contador + i] == "/" || matriz[contador + i] == " ") {
                        continuar = false
                    }
                }
                //al arraylist se le agrega un String
                str.add(st)
                i += contador - 1
            }
            i++

        }
        //el arraylist de strings
        return str
    }

    /**
     * @param vals un arraylist de strings
     * @return una matriz de strings
     * Se utiliza en executeFun, executeFunSingle y evaluarParentesis
     */
    fun convertir(vals: ArrayList<String>): Array<String> {
        var st = ""
        for (a in vals.indices) {
            st += vals[a] + ","
        }
        //en la comilla se hace split
        val values = st.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //matriz de strings
        return values
    }

    /**
     * @param matriz es una matriz de String
     * @return un t o nil. t si es un predicado y nil si no es un predicado
     */
    fun evaluarPredicados(matriz: Array<String>): String {
        //TODO recursividad
        val operadoresA = ArrayList<String>()
        operadoresA.add("+")
        operadoresA.add("-")
        operadoresA.add("*")
        operadoresA.add("/")
        val i = 1
        val str = ""
        //para que este toda esta parte en mayusculas
        //cada uno de los case es cada una de las formas de evaluar si es predicado en lisp
        when (matriz[i].toUpperCase()) {
            //forma de evaluar atom
            "ATOM" -> {
                if (matriz[i + 2] != ")") {
                    return if (matriz[i + 2] == "+" || matriz[i + 2] == "-" || matriz[i + 2] == "*" || matriz[i + 2] == "/") {
                        "T"
                    } else {
                        "Nil"
                    }
                } else if (matriz[i + 1] == "(") {
                    if (matriz[i + 2] == "DEFUN" || matriz[i + 2] == "COND" || matriz[i + 2] == "LIST") {
                        return "Nil"
                    }
                } else {
                    return "T"
                }
                return if (matriz[i + 1] == "(" && matriz[i + 2] == "'") {
                    "T"
                } else if (matriz[i + 1] == "'") {
                    "T"
                } else {
                    "nil"
                }
            }
            //forma de evaluar list
            "LIST" -> return if (matriz[i + 1] == "(" && matriz[i + 2] == "'") {
                "T"
            } else if (matriz[i + 1] == "'") {
                "T"
            } else {
                "nil"
            }
            //forma de evaluar equal
            "EQUAL" -> {
                try {
                    return if (matriz[i + 1] == matriz[i + 2]) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                val numeros = ArrayList<Double>()
                //Tiene que soportar que le metan funciones, operaciones o numeros
                try {
                    //Tiene que soportar que le metan funciones, operaciones o numeros
                    if (matriz[i + 1] == "(" && operadoresA.contains(matriz[i + 2])) {
                        for (j in i until matriz.size) {

                        }
                        //numeros.add(evaluarParentesis(Arrays.copyOfRange(matriz,i+2,matriz.length) , funciones));
                    }
                    val n1 = java.lang.Double.parseDouble(matriz[i + 1])
                    val n2 = java.lang.Double.parseDouble(matriz[i + 2])
                    return if (n1 < n2) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                try {
                    val n1 = java.lang.Double.parseDouble(matriz[i + 1])
                    val n2 = java.lang.Double.parseDouble(matriz[i + 2])
                    return if (n1 > n2) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                return "Ningun predicado dado"
            }
            //forma de evaluar menor que
            "<" -> {
                val numeros = ArrayList<Double>()
                try {
                    if (matriz[i + 1] == "(" && operadoresA.contains(matriz[i + 2])) {
                        for (j in i until matriz.size) {
                        }
                    }
                    val n1 = java.lang.Double.parseDouble(matriz[i + 1])
                    val n2 = java.lang.Double.parseDouble(matriz[i + 2])
                    return if (n1 < n2) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                try {
                    val n1 = java.lang.Double.parseDouble(matriz[i + 1])
                    val n2 = java.lang.Double.parseDouble(matriz[i + 2])
                    return if (n1 > n2) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                return "Ningun predicado dado"
            }
            ">" -> {
                try {
                    val n1 = java.lang.Double.parseDouble(matriz[i + 1])
                    val n2 = java.lang.Double.parseDouble(matriz[i + 2])
                    return if (n1 > n2) {
                        "T"
                    } else {
                        "nil"
                    }
                } catch (e: Exception) {
                    return "nil"
                }

                return "Ningun predicado dado"
            }
            else -> return "Ningun predicado dado"
        }
    }

    /**
     * @param inst es un arraylist de instrucciones
     * @param funciones hashmap de las funciones del programa
     * @return Un string
     * Evalua las condiciones en las instrucciones de lisp
     */
    fun condicion(inst: ArrayList<String>, funciones: HashMap<String, Funcion>): String {
        var contador = 0
        //arraylist de condiciones
        val condiciones = ArrayList<String>()
        //arraylist de acciones
        val acciones = ArrayList<String>()
        var cond = ""
        var accion = ""
        var condicion: Boolean? = false
        var act: Boolean? = false
        var i = 0
        while (i < inst.size) {
            if (inst[i] == "(") {
                if (inst[i + 1] == "(") {
                    condicion = true
                    contador = i + 1
                    //anadir la condicion
                    while (condicion!!) {

                        if (inst[contador] != ")" && inst[contador + 1] != "(") {
                            cond += inst[contador] + ","
                            contador++
                        } else {
                            cond += inst[contador]
                            condiciones.add(cond)
                            cond = ""
                            condicion = false
                            i = contador
                        }
                    }
                }
            }
            //evaluar opciones
            if (i + 1 != inst.size) {
                if (inst[i] == ")") {
                    if (inst[i + 1] == "(") {
                        act = true
                        contador = i + 1
                        while (act!!) {
                            if (inst[contador] == ")" && inst[contador + 1] == ")") {
                                accion += inst[contador + 1] + ","
                                acciones.add(accion)
                                accion = ""
                                act = false
                                i = contador + 1

                            } else {
                                accion += inst[contador] + ","
                                contador++
                            }
                        }
                    }
                }
            }
            i++
        }
        //ejecutar cada condicion
        for (a in condiciones.indices) {
            var eval = ""
            val codi = ArrayList<String>()
            val matriz = condiciones[a].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (evaluarPredicados(matriz) == "T") {
                val mat = acciones[a].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val action = ArrayList<String>()
                for (b in mat.indices) {
                    action.add(mat[b])
                }
                eval = executeFunSingle(action, funciones)
                return eval
            }
        }
        return "No se cumplio ninguna condicion"

    }
}
