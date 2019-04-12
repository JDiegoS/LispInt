package com.example.fuent.lispinterpreter.lispI;

import com.example.fuent.lispinterpreter.lispI.Errores.Error_Empty;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_ExpresionMalBalanceada;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_FuncionYaImplementada;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_MalInterprete;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_NoExisteVariable;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_NombreFun;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OperacionNoExistente;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OperandosIncorrectos;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OutOfIndex;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_ParametrosIncorrectos;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * @author Jennifer Sandoval,Andrea Paniagua, Diego Solorzano
 * @Carne 18962,18733,18151
 * @date 06/03/19
 * @name LispInterpreter.java
 * <p></p>
 * */
public class LispInterpreter {

    Hashtable<String,Atomo> variables = new Hashtable<String,Atomo>();
    Hashtable<String,Atomo> funciones = new Hashtable<String,Atomo>();
    Lista Operaciones;
    Lista Predicados;

    /**
     * Constructor de la clase
     * @throws Exception
     */
    public LispInterpreter() throws Exception {
        OperacionesImplementadas();
        definirPredicados();
    }

    /**
     *Metodo que permite definir los predicados a utilizar en el interprete
     */
    private void definirPredicados() {
        this.Predicados = new Lista();
        this.Predicados.AgregarAlFinal(new Atomo("="));
        this.Predicados.AgregarAlFinal(new Atomo("/="));
        this.Predicados.AgregarAlFinal(new Atomo("<"));
        this.Predicados.AgregarAlFinal(new Atomo("<="));
        this.Predicados.AgregarAlFinal(new Atomo(">"));
        this.Predicados.AgregarAlFinal(new Atomo(">="));
    }

    /**
     * Metodo que analiza si se puede operar la lista obtenida
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return listaevaluada.esOperacion: define si la lista evaluada es una operacion
     * @throws Error_MalInterprete
     */
    public boolean operandoLista(Lista listaevaluada) throws Error_MalInterprete {
        if (!listaevaluada.esOperacion)
            throw new Error_MalInterprete();

        if (listaevaluada.size()>2)
            return false;

        if (this.variables.containsKey(listaevaluada.getAtomoEn(1).toString()))
            return true;

        if (!listaevaluada.getAtomoEn(1).EsLista())
            return false;

        return listaevaluada.esOperacion;
    }
    /**
     * Metodo que permite dividir dos numeros
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo(division): retorna el valor obtenido de la division
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_OperandosIncorrectos, cuando los operandos no son numeros enteros
     */
    private Atomo dividir(Lista listaevaluada) throws Error_MalInterprete, Error_OperandosIncorrectos {
        float division;
        //Se evalua la lista para obtener los operandos
        if (listaevaluada.getAtomoEn(1).Numero())
            division = listaevaluada.getAtomoEn(1).getNumero();
        else
            //Si en la posicion de los operandos no hay un numero, recurre a un error
            throw new Error_OperandosIncorrectos("La division, solo acepta numeros ");
        //Recorrer la lista para optener el segundo valor y dividir los dos operandos
        for (int i=2; i < listaevaluada.size() ; i++){
            if (listaevaluada.getAtomoEn(i).Numero()){
                division /=listaevaluada.getAtomoEn(i).getNumero();
            } else
                throw new Error_OperandosIncorrectos("La division, solo acepta numeros ");
        }
        //Retorna la operacion realizada
        return new Atomo(division);
    }
    /**
     * Metodo que permite definir las funciones y predicados propias del lenguaje Lisp
     */

    private void OperacionesImplementadas() {

        this.Operaciones = new Lista();
        this.Operaciones.AgregarAlFinal(new Atomo("ATOM"));
        this.Operaciones.AgregarAlFinal(new Atomo("COND"));
        this.Operaciones.AgregarAlFinal(new Atomo("DEFUN"));
        this.Operaciones.AgregarAlFinal(new Atomo("list-length"));
        this.Operaciones.AgregarAlFinal(new Atomo("SETQ"));
        this.Operaciones.AgregarAlFinal(new Atomo("+"));
        this.Operaciones.AgregarAlFinal(new Atomo("*"));
        this.Operaciones.AgregarAlFinal(new Atomo("-"));
        this.Operaciones.AgregarAlFinal(new Atomo("/"));
        this.Operaciones.AgregarAlFinal(new Atomo("EQUAL"));
        this.Operaciones.AgregarAlFinal(new Atomo("LIST"));


        /**
         * Predicados
         */
        this.Operaciones.AgregarAlFinal(new Atomo("="));
        this.Operaciones.AgregarAlFinal(new Atomo("/="));
        this.Operaciones.AgregarAlFinal(new Atomo("<"));
        this.Operaciones.AgregarAlFinal(new Atomo(">"));

    }

    /**
     *Metodo que permite identificar el indice de la lista en donde se encuentra un parentesis
     * @param expresion: de tipo String, contiene la expresion obtenida de la entrada del usuario
     * @return indice: retorna el indice de la posicion de la lista
     * @throws Error_MalInterprete, cuando los valores de la lista no fueron interpretados correctamente
     * @throws Error_ExpresionMalBalanceada cuando la expresion no se encuentra balanceada, es decir el numero de espacios es incorrecto
     */
    public int IndiceDelParentesis(String expresion) throws Error_MalInterprete, Error_ExpresionMalBalanceada {
        expresion = expresion.trim();
        //Verifica si el atomo(elemento) del inicio de la expresion es un parentesis
        if (!((new Atomo(expresion).EmpiezaEn("'("))  ||  (new Atomo(expresion).EmpiezaEn("("))))
            throw new Error_MalInterprete("Debe enviarse la expresion de una lista");
        int indice = expresion.indexOf('(')+1;
        int numeroDeParentesis= 1;
        //Se evalua en que posicion de la lista se encuentran los parentesis
        while (indice < expresion.length()){
            if (expresion.charAt(indice)=='('){
                numeroDeParentesis++;
            } else if (expresion.charAt(indice)==')'){
                numeroDeParentesis--;
                if (numeroDeParentesis==0)
                    return indice;
            }
            indice ++;
        }

        throw new Error_ExpresionMalBalanceada();
    }
    /**
     * Metodo que permite restar dos numeros
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo(resta): retorna el valor obtenido de la resta
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_OperandosIncorrectos, cuando los operandos no son numeros enteros
     */

    private Atomo restar(Lista listaevaluada) throws Error_MalInterprete, Error_OperandosIncorrectos {
        float resta;
        //Se evalua el primer operando de la lista
        if (listaevaluada.getAtomoEn(1).Numero())
            resta = listaevaluada.getAtomoEn(1).getNumero();
        else
            throw new Error_OperandosIncorrectos("La resta, solo acepta numeros ");
        //Se verifica que todos los numeros sean enteros
        boolean todosSonEnteros = true;
        //Se evalua la lista para obtener el segundo operando y se realiza la operacion
        for (int i=2; i < listaevaluada.size() ; i++){
            if (listaevaluada.getAtomoEn(i).Numero()){
                if (!listaevaluada.getAtomoEn(i).Entero())
                    todosSonEnteros = false;

                resta -= listaevaluada.getAtomoEn(i).getNumero();
            } else
                throw new Error_OperandosIncorrectos("La resta, solo acepta numeros ");
        }
        if (todosSonEnteros)
            return new Atomo((int) resta);
        else
            return new Atomo(resta);
    }

    /**
     *
     * @param Parsear: de tipo String, es el elemento que se desea parsear
     * @param definirLista: de tipo Boolean, permite saber si el parseo es una lista o no (true/false)
     * @param esClausula: de tipo Boolean, permite saber si el parseo es una clausula o no(true/false)
     * @return new Atomo(): regresa el atomo obtenido de la lista
     * @throws Error_OutOfIndex
     * @throws Error_OperacionNoExistente
     * @throws Error_Empty
     * @throws Error_ExpresionMalBalanceada
     * @throws Error_NoExisteVariable
     * @throws Error_MalInterprete
     */
    public Atomo parsearExpresion(String Parsear,boolean definirLista ,boolean esClausula) throws Error_OutOfIndex, Error_OperacionNoExistente, Error_Empty, Error_ExpresionMalBalanceada, Error_NoExisteVariable, Error_MalInterprete {
        Parsear = Parsear.trim();

        if (Parsear.compareTo("")==0)
            return new Atomo();
        /*Verificar si la ecuacion se encuentra balanceada*/
        if (!this.Balanceada(Parsear))
            throw new Error_ExpresionMalBalanceada("\"" + Parsear + "\" no esta bien balancada");
        //Se crea un nuevo atomo
        Atomo atomo = new Atomo();
        //Se tokeniza para poder separar el String que se desea parsear
        StringTokenizer separador = new StringTokenizer(Parsear);
        String primerToken = separador.nextToken();
        //Toma el primerToken extraido como un nuevo atomo
        Atomo atomoIngresado = new Atomo(primerToken);
        //Cuando toos los tokens se han convertido en atomos
        if ((separador.countTokens()==0)){
            //
            if ((this.variables.containsKey(primerToken)) && (!definirLista))
                return this.variables.get(primerToken);

            if ((primerToken.compareTo("'()")==0) || ((primerToken.compareTo("()")==0)))
                return new Atomo();

            if (primerToken.charAt(0)=='\''){
                if ((primerToken.charAt(1)!='(') && (primerToken.charAt(primerToken.length()-1)!=')'))
                    return new Atomo(primerToken.substring(1));
            }
        }
        //Cuando el primer token empieza con un parentesis
        if ((new Atomo(primerToken).EmpiezaEn("'("))){
            //se define que es una lista
            definirLista = true;
            atomo= new Atomo(new Lista());
            atomo.lista.esOperacion = false;
            int inicio = Parsear.indexOf('(') + 1;
            int fin = this.IndiceDelParentesis(Parsear);
            if ((fin==Parsear.lastIndexOf(')')) && (!(fin==Parsear.length()-1)))
                throw new Error_OutOfIndex();
            Parsear = Parsear.substring(inicio, fin);
        } else if (primerToken.charAt(0)=='('){
            //Se define la posicion del primer parentesis y el ultimo
            int inicio = Parsear.indexOf('(') + 1;
            int fin = this.IndiceDelParentesis(Parsear);
            //Parseado
            Parsear = Parsear.substring(inicio,fin);
            atomo = new Atomo(new Lista());

            separador = new StringTokenizer(Parsear);
            String primerTokenDeLaLista = separador.nextToken();

            boolean esFuncion = (OperacionImplementada(primerTokenDeLaLista)) || this.funciones.containsKey(primerTokenDeLaLista);

            if ((!definirLista) && (!esFuncion)){

                throw new Error_OperacionNoExistente(primerTokenDeLaLista + " no es una funcion implementada");
            }

            if (definirLista){
                if (!esClausula)
                    atomo.lista.esOperacion = false;
                else
                    atomo.lista.esOperacion = true;
            }else {
                atomo.lista.esOperacion = true;
            }
        }

        if (!atomo.EsLista())
            return new Atomo(Parsear);

        separador = new StringTokenizer(Parsear);

        while (separador.hasMoreTokens()) {
            Atomo atomoActual = new Atomo(separador.nextToken());

            if ((atomoActual.EmpiezaEn("'(")) || (atomoActual.EmpiezaEn("("))){

                int inicio = Parsear.indexOf(atomoActual.toString());
                Parsear = Parsear.substring(inicio);
                int fin = this.IndiceDelParentesis(Parsear);
                String expresionDeLaListaInterna = Parsear.substring(0, fin+1);
                Atomo atomoConLaListaInterna = new Atomo();

                boolean Defun = false;
                boolean Cond = false;

                if (atomoActual.EmpiezaEn("'("))
                    atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true, esClausula);
                else {
                    if (atomo.lista.size() > 0){
                        if (atomo.ListaConOperacion()){
                            String operacionDeLaLista = atomo.lista.getOperacion().toString();

                            if ((operacionDeLaLista.compareToIgnoreCase("defun")==0) && (atomo.lista.size()==2)){
                                atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true,false);
                                Defun = true;
                            }

                            if (operacionDeLaLista.compareToIgnoreCase("cond")==0){
                                Cond = true;
                                atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true,true);
                            }
                        }
                    }

                    if ((!Defun) && (!Cond))
                        atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, definirLista, esClausula);

                }

                atomo.lista.AgregarAlFinal(atomoConLaListaInterna);
                String expresionDespuesDeLaListaInterna = Parsear.substring(fin + 1);
                Parsear = expresionDespuesDeLaListaInterna;

                if (!Defun){

                    separador = new StringTokenizer(expresionDespuesDeLaListaInterna);
                } else {

                    String nombreDeLaFuncion = atomo.lista.getAtomoEn(1).toString();
                    this.funciones.put(nombreDeLaFuncion,new Atomo());

                    Atomo atomoConLaOperacion = this.parsearExpresion(Parsear.trim(), false,false);
                    atomo.lista.AgregarAlFinal(atomoConLaOperacion);
                    separador = new StringTokenizer("");

                    this.funciones.remove(nombreDeLaFuncion);
                }
            } else{

                int desdeDondeCortar = Parsear.indexOf(atomoActual.toString());
                Parsear = Parsear.substring(desdeDondeCortar);

                if (atomoActual.EmpiezaEn("'"))
                    atomoActual = new Atomo(atomoActual.toString().substring(1));

                atomo.lista.AgregarAlFinal(atomoActual);
            }
        }


        return atomo;
    }
    /**
     * Metodo que permite sumar dos numeros
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo(suma): retorna el valor obtenido de la suma
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_OperandosIncorrectos, cuando los operandos no son numeros enteros
     */
    private Atomo sumar(Lista listaevaluada) throws Error_OperandosIncorrectos, Error_MalInterprete {
        float suma = 0;
        boolean enteros = true;

        for (int i=1; i < listaevaluada.size() ; i++){
            if (listaevaluada.getAtomoEn(i).Numero()){
                if (!listaevaluada.getAtomoEn(i).Entero())
                    enteros = false;

                suma += listaevaluada.getAtomoEn(i).getNumero();
            } else
                throw new Error_OperandosIncorrectos("La suma, solo acepta numeros ");
        }

        if (enteros)
            return new Atomo((int) suma);
        else
            return new Atomo(suma);
    }

    private boolean OperacionImplementada(String posibleOperacion) {
        return this.Operaciones.existe(new Atomo(posibleOperacion));
    }

    /**
     *
     * @param atomoAEvaluar: de tipo Atomo, es el atomo que se evaluara para saber que operacion se debe hacer
     * @return new Atomo(): devuelve un nuevo atomo dependiendo de la evaluacion
     * @throws Error_MalInterprete
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     * @throws Error_ParametrosIncorrectos
     */
    public Atomo evaluar(Atomo atomoAEvaluar) throws Error_MalInterprete,Error_OperandosIncorrectos, Error_NombreFun, Error_FuncionYaImplementada, Error_ParametrosIncorrectos {
        Atomo AtomoDeRespuesta = new Atomo();
        if (atomoAEvaluar.Nulo)
            return new Atomo();

        if (atomoAEvaluar.EsLista()){
            Lista listaevaluada= atomoAEvaluar.lista;

            if (listaevaluada.esOperacion){
                String operacionDeLaLista = listaevaluada.getOperacion().toString();
                if ((operacionDeLaLista.compareToIgnoreCase("defun")!=0) && (operacionDeLaLista.compareToIgnoreCase("cond")!=0))
                    for (int i=0 ; i < listaevaluada.size() ; i++){
                        if (listaevaluada.getAtomoEn(i).ListaConOperacion()){
                            Atomo listaEvaluada = this.evaluar(listaevaluada.getAtomoEn(i));
                            listaevaluada.remplazarEn_Por(i, listaEvaluada);
                        } else if (this.variables.containsKey(listaevaluada.getAtomoEn(i).toString())){
                            Atomo valorDelAtomo = this.variables.get(listaevaluada.getAtomoEn(i).toString());

                            if (listaevaluada.esOperacion){
                                if (!((i==1) && (listaevaluada.getOperacion().toString().compareTo("setq")==0)))
                                    listaevaluada.remplazarEn_Por(i, valorDelAtomo);
                            } else
                                listaevaluada.remplazarEn_Por(i, valorDelAtomo);
                        }
                    }
            } else {

                return new Atomo(listaevaluada);
            }


            String operacion = listaevaluada.getOperacion().toString();
            if (this.Predicado(operacion)){
                return this.evaluarPredicado(listaevaluada);
            } else if (operacion.compareToIgnoreCase("list-length")==0){
                return this.list_length(listaevaluada);
            } else if (operacion.compareToIgnoreCase("setq")==0){
                return this.setq(listaevaluada);
            } else if (operacion.compareToIgnoreCase("+")==0){
                return this.sumar(listaevaluada);
            } else if (operacion.compareToIgnoreCase("*")==0){
                return this.multiplicar(listaevaluada);
            } else if (operacion.compareToIgnoreCase("-")==0){
                return this.restar(listaevaluada);
            } else if (operacion.compareToIgnoreCase("/")==0){
                return this.dividir(listaevaluada);
            } else if (operacion.compareToIgnoreCase("defun")==0){
                return this.defun(listaevaluada);
            } else if (this.funciones.containsKey(operacion)){
                return this.operarFuncionDelUsuario(listaevaluada);
            } else if (operacion.compareToIgnoreCase("equal")==0){
                return this.equal(listaevaluada);
            } else if (operacion.compareToIgnoreCase("cond")==0){
                return this.cond(listaevaluada);
            } else if (operacion.compareToIgnoreCase("list")==0){
                return this.list(listaevaluada);
            }
        } else{
            /**
             * No es una lista, puede ser una variable
             */
            if (this.variables.containsKey(atomoAEvaluar.toString()))
                AtomoDeRespuesta= this.variables.get(atomoAEvaluar.toString());
            else
                AtomoDeRespuesta = atomoAEvaluar;
        }

        return AtomoDeRespuesta;
    }

    /**
     * Metodo que permite evaluar un predicado y realizar lo que se deberia en cada uno de ellos
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return atomoDeRespuesta: retorna el atomo nuevo conla operacion que debe realizarse
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     */

    private Atomo evaluarPredicado(Lista listaevaluada) throws Error_MalInterprete, Error_ParametrosIncorrectos {
        Atomo atomoDeRespuesta = new Atomo();

        String predicadoAEvaluar = listaevaluada.getAtomoEn(0).toString();
        if (!this.Predicado(predicadoAEvaluar))
            throw new Error_MalInterprete("Se esta tratando de evaluar un predicado que no es predicado");

        if (predicadoAEvaluar.compareTo("=")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"=\" necesita de al menos un operando");

            boolean sonIguales = true;
            for (int i = 1 ; i < listaevaluada.size()-1 ; i++){
                sonIguales = listaevaluada.getAtomoEn(i).equals(listaevaluada.getAtomoEn(i+1));

                if (!sonIguales)
                    return new Atomo(sonIguales);

            }

            return new Atomo(sonIguales);
        } else if (predicadoAEvaluar.compareTo("/=")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"/=\" necesita de al menos un operando");

            boolean sonDiferentes = true;

            for (int i = 1 ; i < listaevaluada.size()-1 ; i++){
                sonDiferentes = !listaevaluada.getAtomoEn(i).equals(listaevaluada.getAtomoEn(i+1));

                if (!sonDiferentes)
                    return new Atomo(false);
            }
            return new Atomo(sonDiferentes);
        } else if (predicadoAEvaluar.compareTo("<")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"/<\" necesita de al menos un operando");

            boolean esMenorQue = true;

            for (int i = 1; i <listaevaluada.size()-1 ; i++){

                if (!(listaevaluada.getAtomoEn(i).Numero()))
                    throw new Error_ParametrosIncorrectos(listaevaluada.getAtomoEn(i)+" no es un numero");

                esMenorQue = listaevaluada.getAtomoEn(i).getNumero() < listaevaluada.getAtomoEn(i+1).getNumero();

                if (!esMenorQue)
                    return new Atomo (false);
            }

            return new Atomo(esMenorQue);
        } else if (predicadoAEvaluar.compareTo("<=")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"/<=\" necesita de al menos un operando");

            boolean esMenorIgualQue = true;

            for (int i = 1; i <listaevaluada.size()-1 ; i++){
                if (!(listaevaluada.getAtomoEn(i).Numero()))
                    throw new Error_ParametrosIncorrectos(listaevaluada.getAtomoEn(i)+" no es un numero");

                esMenorIgualQue = listaevaluada.getAtomoEn(i).getNumero() <= listaevaluada.getAtomoEn(i+1).getNumero();
                if (!esMenorIgualQue)
                    return new Atomo (false);
            }
            return new Atomo(esMenorIgualQue);
        } else if (predicadoAEvaluar.compareTo(">")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"/>\" necesita de al menos un operando");

            boolean esMayorQue = true;

            for (int i = 1; i <listaevaluada.size()-1 ; i++){

                if (!(listaevaluada.getAtomoEn(i).Numero()))
                    throw new Error_ParametrosIncorrectos(listaevaluada.getAtomoEn(i)+" no es un numero");

                esMayorQue = listaevaluada.getAtomoEn(i).getNumero() > listaevaluada.getAtomoEn(i+1).getNumero();

                if (!esMayorQue)
                    return new Atomo (false);
            }

            return new Atomo(esMayorQue);
        } else if (predicadoAEvaluar.compareTo(">=")==0){
            if (!(listaevaluada.size() > 1))
                throw new Error_ParametrosIncorrectos("El predicado \"/>=\" necesita de al menos un operando");

            boolean esMayorIgualQue = true;

            for (int i = 1; i <listaevaluada.size()-1 ; i++){
                if (!(listaevaluada.getAtomoEn(i).Numero()))
                    throw new Error_ParametrosIncorrectos(listaevaluada.getAtomoEn(i)+" no es un numero");

                esMayorIgualQue = listaevaluada.getAtomoEn(i).getNumero() >= listaevaluada.getAtomoEn(i+1).getNumero();
                if (!esMayorIgualQue)
                    return new Atomo (false);
            }
            return new Atomo(esMayorIgualQue);
        }

        throw new Error_MalInterprete("No se evaluo ningun predicado");
    }
    /**
     * Metodo que permite evaluar un predicado y realizar lo que se deberia en cada uno de ellos
     * @param operacion: Es de tipo String y se refiere a la operacion que debe realizarse
     */
    private boolean Predicado(String operacion) {
        return this.Predicados.existe(new Atomo(operacion));
    }

    /**
     * Metodo que permite realizar la operacion de condicion
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return atomoDeRespuesta: devuelve el atomo obtenido a partir de la operacion de condicion
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     */
    private Atomo cond(Lista listaevaluada) throws Error_MalInterprete, Error_ParametrosIncorrectos,Error_OperandosIncorrectos, Error_NombreFun, Error_FuncionYaImplementada {
        if (listaevaluada.size()<2)
            throw new Error_MalInterprete("cond no recibio ningun parametro");

        /**
         * Ir a traves de todas las sublistas y ver si hay que ejecutarlas
         */
        int revisandoSubListaIndex = 1;
        boolean haEncontradoRespuesta = false;
        Atomo atomoDeRespuesta = new Atomo();

        while ((revisandoSubListaIndex < listaevaluada.size()) && (!haEncontradoRespuesta)){

            if (!listaevaluada.getAtomoEn(revisandoSubListaIndex).EsLista())
                throw new Error_ParametrosIncorrectos(listaevaluada.getAtomoEn(revisandoSubListaIndex) + " deberia ser una lista y no lo es");
            else {
                /*
                 * Revisar que no sea una lista NIL
                 */

            }

            Lista evaluandoSubLista = listaevaluada.getAtomoEn(revisandoSubListaIndex).lista;

            /**
             * Evaluar su primer atomo, para ver si es TRUE
             */
            Atomo primerAtomo = evaluandoSubLista.getAtomoEn(0);

            /**
             * Si no es la ultima clausula, y todavia no hay respuesta, no validar
             */
            if (!((atomoDeRespuesta.Nulo) && (revisandoSubListaIndex==listaevaluada.size()-1))) {
                if ((!primerAtomo.EsLista()) && (!this.variables.containsKey(primerAtomo.toString())))
                    throw new Error_ParametrosIncorrectos(primerAtomo + " deberia ser una lista o una variable definida por el usuario y no lo es ninguna de las dos");
            }

            Atomo primerAtomoEvaluado = this.evaluar(primerAtomo);


            if (!primerAtomoEvaluado.Nulo){
                haEncontradoRespuesta = true;

                if (evaluandoSubLista.size() > 1)
                /**
                 * Entonces, especifico una respuesta
                 */
                    atomoDeRespuesta = this.evaluar(evaluandoSubLista.getAtomoEn(evaluandoSubLista.size()-1));
                else
                /**
                 * Retornar el valor evaluado
                 */
                    atomoDeRespuesta = primerAtomoEvaluado;
            }

            revisandoSubListaIndex ++;
        }


        return atomoDeRespuesta;
    }

    /**
     * Metodo que permite evaluar si un atomo es igual a otro
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return new Atomo (): Devuelve un nuevo atomo con todos los valores necesarios para saber si son o no iguales
     * @throws Error_ParametrosIncorrectos
     */
    private Atomo equal(Lista listaevaluada) throws Error_ParametrosIncorrectos {

        if (listaevaluada.size()!=3)
            throw new Error_ParametrosIncorrectos("equal solo acepta dos parametros");
        return new Atomo(listaevaluada.getAtomoEn(1).toString().compareTo(listaevaluada.getAtomoEn(2).toString())==0);
    }

    /**
     * Metodo que permite operar una funcion que el usuario definio previamente
     * @param listaevaluada, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return atomoDeLaOperacion: Retorna el atomo obtenido a partir de la funcion definida del usuario
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     */

    private Atomo operarFuncionDelUsuario(Lista listaevaluada) throws Error_MalInterprete,Error_ParametrosIncorrectos,Error_OperandosIncorrectos, Error_NombreFun, Error_FuncionYaImplementada {
        /**
         * Mapear parametros a valores
         */
        int numeroDeParametrosQueIngreso = listaevaluada.size()-1;

        Lista listaDeLosParametros = this.funciones.get(listaevaluada.getOperacion().toString()).lista.getAtomoEn(0).lista;


        int numeroDeParametrosDeLaFuncion = listaDeLosParametros.size();

        if (numeroDeParametrosDeLaFuncion!=numeroDeParametrosQueIngreso)
            throw new Error_ParametrosIncorrectos("La funcion que usted definio \""+listaevaluada.getOperacion().toString()+"\" contiene "+ numeroDeParametrosDeLaFuncion + " y usted ingreso "+numeroDeParametrosQueIngreso);

        Hashtable<String,Atomo> mapaDeParametrosYValores = new Hashtable<String,Atomo>();

        for (int i =1 ; i <= numeroDeParametrosQueIngreso ; i++){
            mapaDeParametrosYValores.put(listaDeLosParametros.getAtomoEn(i-1).toString(), listaevaluada.getAtomoEn(i));
        }


        Atomo atomoDeLaOperacion = this.funciones.get(listaevaluada.getOperacion().toString()).lista.getAtomoEn(1);

        if (atomoDeLaOperacion.EsLista()){
            Atomo listaAEvaluarConParametrosMapeados = this.MapingParam(mapaDeParametrosYValores, new Atomo(atomoDeLaOperacion.lista));
            return this.evaluar(listaAEvaluarConParametrosMapeados);
        } else
            return atomoDeLaOperacion;

    }


    private Atomo MapingParam(Hashtable<String, Atomo> mapa, Atomo atomoMap) throws Error_MalInterprete,Error_OperandosIncorrectos, Error_NombreFun, Error_FuncionYaImplementada, Error_ParametrosIncorrectos {

        Lista listaAEvaluar = new Lista(atomoMap.lista);
        listaAEvaluar.esOperacion = atomoMap.lista.esOperacion;

        for (int i= 0 ; i < listaAEvaluar.size(); i++){
            Atomo atomoActual = listaAEvaluar.getAtomoEn(i);

            if (atomoActual.EsLista()){
                listaAEvaluar.remplazarEn_Por(i,this.MapingParam(mapa, atomoActual));
            } else{
                if (mapa.containsKey(atomoActual.toString()))
                    listaAEvaluar.remplazarEn_Por(i,mapa.get(atomoActual.toString()));
            }
        }
        return new Atomo(listaAEvaluar);
    }
    /**
     * Metodo que permite definir una funcion
     * @param listaAEvaluar, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo: Retorna el nuevo atomo creado a partir de la funcion ingresada
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     */

    private Atomo defun(Lista listaAEvaluar) throws Error_NombreFun, Error_FuncionYaImplementada, Error_OperandosIncorrectos {

        if (listaAEvaluar.size()!=4)
            throw new Error_OperandosIncorrectos("defun debe tener 3 argumentos: <nombre de la funcion> <lista de parametros> <operacion>");
        else{
            if (listaAEvaluar.getAtomoEn(1).EsLista())
                throw new Error_OperandosIncorrectos("defun debe tener 3 argumentos: <nombre de la funcion> <lista de parametros> <operacion>");

            if (!listaAEvaluar.getAtomoEn(2).EsLista())
                throw new Error_OperandosIncorrectos("defun debe tener 3 argumentos: <nombre de la funcion> <lista de parametros> <operacion>");


        }
        String nombreFun = listaAEvaluar.getAtomoEn(1).toString();
        if (!FuncionValida(nombreFun))
            throw new Error_NombreFun();

        Atomo atomoDeLaFuncion = new Atomo(new Lista(listaAEvaluar.subList(2, 4)));
        this.funciones.put(nombreFun, new Atomo(new Lista(listaAEvaluar.subList(2, 4))));

        return new Atomo(nombreFun);
    }
    /**
     * Metodo que permite definir una lista
     * @param listaAEvaluar, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo: devuelve el atomo de una lista
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     */
    private Atomo list(Lista listaAEvaluar) throws Error_MalInterprete, Error_OperandosIncorrectos,Error_NombreFun, Error_FuncionYaImplementada, Error_ParametrosIncorrectos {
        // Declarar variables y objetos
        Lista lista = new Lista();

        for(int i = 1; i < listaAEvaluar.size(); i++) {
            lista.AgregarAlFinal(this.evaluar(listaAEvaluar.getAtomoEn(i)));
        }

        return new Atomo(lista);
    }

    /**
     *Metodo que permite saber si la expresion se encuentra balanceada, es decir si tiene el codigo correcto
     * @param expresion: de tipo String, se refiere a la entrada realizada por el usuario
     * @return inicio==fin, si son iguales significa que se encuentra que esta balanceada
     */
    public boolean Balanceada(String expresion) {
        int inicio = 0;
        int fin = 0;
        /*
         * Recorrer expresion
         */
        for (int i=0 ; i < expresion.length() ; i++){
            if (expresion.charAt(i) == '(')
                inicio++;
            else if (expresion.charAt(i) == ')')
                fin++;
        }

        return inicio== fin;
    }
    /**
     * Metodo que permite saber si la funcion ingresada existe
     * @param nombreDeLaFuncion: es de tipo String,contiene el nombre de la funcion
     * @return true/false: dependiendo si es una funcion valida o no
     * @throws Error_FuncionYaImplementada
     */
    private boolean FuncionValida(String nombreDeLaFuncion) throws Error_FuncionYaImplementada {

        if (OperacionImplementada(nombreDeLaFuncion))
            throw new Error_FuncionYaImplementada();

        /**
         * Ver que no empiece con un numero
         */
        String primerCaracter = Character.toString(nombreDeLaFuncion.charAt(0));
        try {
            int posibleNumero = Integer.parseInt(primerCaracter);
            return false;
        } catch(NumberFormatException noEmpiezaConNumero){
            return true;
        }
    }
    /**
     * Metodo que permite operar la multiplicacion de dos numero
     * @param listaAEvaluar, de tipo lista. Se refiere a la lista de los valores ingresados por el usuario
     * @return Atomo(multiplicacion)
     * @throws Error_MalInterprete, cuando no se separo correctamente la lista
     * @throws Error_ParametrosIncorrectos
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     */

    private Atomo multiplicar(Lista listaAEvaluar) throws Error_MalInterprete, Error_OperandosIncorrectos {
        float Multiplicacion = 1;
        boolean todosSonEnteros = true;

        for (int i=1; i < listaAEvaluar.size() ; i++){
            if (listaAEvaluar.getAtomoEn(i).Numero()){
                if (!listaAEvaluar.getAtomoEn(i).Entero())
                    todosSonEnteros = false;

                Multiplicacion *= listaAEvaluar.getAtomoEn(i).getNumero();
            } else
                throw new Error_OperandosIncorrectos("La multiplicacion, solo acepta numeros ");
        }

        if (todosSonEnteros)
            return new Atomo((int) Multiplicacion);
        else
            return new Atomo(Multiplicacion);
    }

    /**
     *Metodo que permite definir  parametros
     * @param listaAEvaluar: Se refiere a la lista de la expresion ingresada por el usuario
     * @return atomoAsignado: devuelve el atomo que se asigno a la variable
     * @throws Error_OperandosIncorrectos
     * @throws Error_MalInterprete
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     * @throws Error_ParametrosIncorrectos
     */
    public Atomo setq(Lista listaAEvaluar) throws Error_OperandosIncorrectos, Error_MalInterprete, Error_NombreFun, Error_FuncionYaImplementada, Error_ParametrosIncorrectos {

        if ((listaAEvaluar.size() != 3))
            throw new Error_OperandosIncorrectos("setq necesita de dos parametros: una nombre de variable y un elemento asignado, vea el manual para mas informacion (ayuda setq)");

        if (((Atomo)listaAEvaluar.get(1)).EsLista())
            throw new Error_OperandosIncorrectos("setq necesita de dos parametros: una nombre de variable y un elemento asignado, vea el manual para mas informacion (ayuda setq)");


        String variableDeAsignacion = ((Atomo) listaAEvaluar.get(1)).toString();
        Atomo atomoAsignado = ((Atomo) listaAEvaluar.get(2));
        atomoAsignado = this.evaluar(atomoAsignado);

        this.variables.put(variableDeAsignacion, atomoAsignado);
        return atomoAsignado;
    }

    /**
     * Metodo que permite conocer el largo de una lista
     * @param listaAEvaluar: Se refiere a la lista de la expresion ingresada por el usuario
     * @return Atomo: devuelve el atomo con el tamaño de la lista
     * @throws Error_MalInterprete
     * @throws Error_OperandosIncorrectos
     * @throws Error_NombreFun
     * @throws Error_FuncionYaImplementada
     * @throws Error_ParametrosIncorrectos
     */
    public Atomo list_length(Lista listaAEvaluar) throws Error_MalInterprete, Error_OperandosIncorrectos, Error_NombreFun,Error_FuncionYaImplementada, Error_ParametrosIncorrectos {

        if (!this.operandoLista(listaAEvaluar))
            throw new Error_OperandosIncorrectos("list-length solo acepta un operando: una lista");

        Atomo listaOperanda = this.evaluar(listaAEvaluar.getAtomoEn(1));
        return new Atomo(listaOperanda.lista.size());
    }

}
