package com.example.fuent.lispinterpreter.lispI;

public class Nodo <E> {

    private Object Elemento; //Elemento al cual pertenece el nodo
    private Nodo<E> SiguienteElemento; //Hace referencia al siguiente elemento


    /**
     * Metodo constructor de la clase Nodo
     * @param elemento: de tipo object, contiene el elemento al cual pertenece el nodo
     * @param next: de tipo Nodo, hace referencia al nodo del elemento siguiente
     */
    public Nodo(Object elemento, Nodo<E> next) {
        Elemento = elemento;
        SiguienteElemento = next;
    }

    /**
     *Metodo que indica el elemento siguiente
     * @param next:de tipo Nodo, hace referencia al nodo del siguiente elemento
     */
    public void setNext(Nodo<E> next){
        SiguienteElemento = next;
    }

    /**
     * Metodo que devuelve el elemento correspondiente al nodo
     * @return Elemento: Elemento que corresponde al nodo
     */
    public Object value() {
        return Elemento;
    }

    /**
     *  Metodo que devuelve el elemento que sigue en el Stack
     * @return SiguienteElemento: elemento que sigue en el Stack
     */
    public Nodo<E> next() {
        return SiguienteElemento;
    }


}