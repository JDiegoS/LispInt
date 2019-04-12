package com.example.fuent.lispinterpreter.lispI;

import com.example.fuent.lispinterpreter.lispI.Errores.Error_MalInterprete;

public class Atomo {
    private boolean esLista;
    private boolean esNumero;

    /**
     * Permite saber si la entrada ingresada es atomo o no. Nulo=True: no es un atomo. Nulo=False: es un atomo
     */
    public boolean Nulo;

    /**
     * Si un atomo es una lista, crea una lista para el atomo
     */
    public Lista lista;

    /**
     * En este atributo se obtiene la descripcion del atomo
     */
    public String descripcion;
    /**
     * Atributo que hace referencia el atomo recibido por la entrada del usuario
     */
    private String atomo;

    private float numero;
    /**
     * Metodo encargado de analizar si el atomo termina con un substring
     * @param subString subString que se esta buscando
     * @return true si este atomo termina con subString
     */
    public boolean terminaCon(String subString) {
        if (!this.esLista)
            return this.atomo.lastIndexOf(subString) == this.atomo.length() - subString.length();

        return false;
    }

    /**
     *
     */
    public boolean valorBooleano;
    private boolean esBooleano;
    /**
     * Constructor de un atomo
     */
    public Atomo() {
        this.Nulo = true;
        this.esLista = false;
        this.esNumero = false;
    }

    /**
     *Constructor de un atomo cuando es una lista
     *@param lista: Lista en donde se encuentran los atomos
     */
    public Atomo(Lista lista){
        this.esLista = true;
        this.Nulo = false;
        this.esNumero = false;
        this.lista = lista;
    }
    /**
     * Metodo que obtiene el tipo de atomo
     * @return un String dependiendo del tipo de atomo
     */
    public String getTipo() {
        if (this.esLista)
            return "CONS";
        else if (this.esNumero)
            return "SINGLE-FLOAT";
        else if (this.Nulo)
            return "NULL";
        else
            return "SYMBOL";

    }

    /**
     * Se copia un atomo para obtener sus partes
     * @param atomoAcopiar: debe ser de tipo atomo
     */
    public void copiarAtomo(Atomo atomoAcopiar){
        this.esLista = atomoAcopiar.esLista;
        this.esNumero = atomoAcopiar.esNumero;
        this.lista = atomoAcopiar.lista;
        this.atomo = atomoAcopiar.atomo;
        this.descripcion = atomoAcopiar.descripcion;
        this.numero = atomoAcopiar.numero;
        this.Nulo = atomoAcopiar.Nulo;
    }

    /**
     * Metodo que permite saber la cantidad de atomos obtenidos
     * @param numero: de tipo int, indica la cantidad de atomos
     */

    public Atomo(int numero) {
        this.numero = numero;
        this.esNumero = true;
        this.Nulo = false;
        this.esLista = false;
        this.atomo = Integer.toString(numero);
    }

    /**
     * Metodo que determina partes del atomo a partir de los Strings
     * @param atomo: de tipo String, contiene el string del atomo
     * @param descripcion: de tipo String, contiene la descripcion del atomo
     */
    public Atomo(String atomo, String descripcion) {
        this.atomo = atomo;
        this.descripcion = descripcion;
    }
    /**
     * Metodo que determina que tipo de atomo es
     * @param atomo: de tipo String, contiene el string del atomo
     */

    public Atomo(String atomo) {
        /**
         * Tratar de parsear un entero
         */
        try {
            Atomo atomoConUnNumero = new Atomo(Integer.parseInt(atomo));
            this.copiarAtomo(atomoConUnNumero);
            this.esNumero = true;
        } catch (NumberFormatException atomoNoEsNumero){
            try {
                Atomo atomoConUnNumero = new Atomo(Float.parseFloat(atomo));
                this.copiarAtomo(atomoConUnNumero);
                this.esNumero = true;
            } catch (NumberFormatException atomoNoEsFlotante){
                this.atomo = atomo;
                this.esNumero = false;
                this.Nulo = false;
                this.esLista = false;
            }

        }
    }


    /**
     * Metodo para determinar que el atomo es float
     * @param numero: de tipo float
     */
    public Atomo(float numero) {
        this.numero = numero;
        this.esNumero = true;
        this.Nulo = false;
        this.esLista = false;

        this.atomo = Float.toString(this.numero);
    }

    /**
     * Metodo para determinar que el atomo es booleano
     * @param esTrue
     */
    public Atomo(boolean esTrue) {
        if (esTrue){
            this.esBooleano = true;
            this.valorBooleano = true;
            this.esNumero = false;
            this.Nulo = false;
            this.esLista = false;
            this.atomo = "T";
        } else {
            this.Nulo = true;
            this.esLista = false;
            this.esNumero = false;
        }
    }



    /**
     * Metodo que permite determinar si el atomo es una lista
     * @return this.esLista: puede ser true/false dependiendo si es una lista o no
     */
    public boolean EsLista(){
        return this.esLista;
    }
    /**
     * Metodo que permite determinar si el atomo es una lista y devuelve NIL
     * @return this.esLista: puede ser true/false dependiendo si es una lista o no
     */
    public String toString(){
        if (this.Nulo)
            return "NIL";

        if (this.EsLista())
            return this.lista.toString();
        else
            return this.atomo;
    }




    /**
     * Ve si el atomo, en caso de ser un string, comienza con un subString
     * @param substring: es de tipo String, un substring del inicio del atomo
     * @return false: solamente si no es una lista
     */
    public boolean EmpiezaEn(String substring){
        if ((substring.length()<=this.atomo.length()) && (!this.esLista)){
            return this.atomo.substring(0, substring.length()).compareTo(substring)==0;
        }

        return false;
    }

    /**
     * Metodo que verifica si este atomo es una lista con una operacion
     * @return True si la este atomo es una lista con una operacion
     *
     */
    public boolean ListaConOperacion() {
        if (this.EsLista())
            return (this.lista.esOperacion);

        return false;
    }

    /**
     * Metodo que verifica si este atomo es un numero
     * @return this.EsNumero: si es un numero retorna true y si no, false
     */
    public boolean Numero(){
        return this.esNumero;
    }
    /**
     * Metodo que verifica si un atomo es igual a otro atomo
     * @return this.Nulo, this.esNumero, this.esLista, this.atomo
     */
    public boolean equals(Object objeto){
        Atomo otroAtomo = (Atomo)objeto;

        if ((this.Nulo) && (otroAtomo.Nulo))
            return true;

        if ((this.esNumero) && (otroAtomo.esNumero))
            return this.numero==otroAtomo.numero;

        if ((this.esLista) && (otroAtomo.esLista))
            return this.lista.equals(otroAtomo.lista);

        return this.atomo.compareTo(otroAtomo.atomo)==0;
    }

    /**
     * Metodo que verifica si este es un numero entero
     * @return
     */
    public boolean Entero() {
        if (!this.esNumero)
            return false;

        /**
         * Si es numero, puede ser flotante o entero
         */

        try {
            int numero = Integer.parseInt(this.atomo);
            return true;
        } catch (NumberFormatException noEsEntero) {
            return false;
        }
    }
    /**
     * Se obtiene el numero del atomo
     * @return this.numero: retorna el atomo de tipo numero
     * @throws Error_MalInterprete
     */
    public float getNumero() throws Error_MalInterprete {
        if (!this.esNumero)
            throw new Error_MalInterprete("Error");

        return this.numero;
    }
    /**
     * Metodo que determina si un atomo es de tipo booleano
     * @return this.esBooleano: true si es un boleano, false si no lo es
     */
    public boolean Booleano(){
        return this.esBooleano;
    }


}