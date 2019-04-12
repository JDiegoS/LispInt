package com.example.fuent.lispinterpreter.lispI;

public class Listas_Stack<E> implements Stack<E> {

    /**
     * Variable contadora de elementos dentro de la stack
     */
    public int count;

    /**
     * Nodo inicial que apunta ala posicion siguiente en el stack
     */
    public Nodo<E> head;


    /**
     * Constructor de la implementacion
     */
    public Listas_Stack(){
        count = 0;
        head = null;

    }


    /**
     * Metodo que especifica si la Stack se encuentra vacia
     * @return true/false : dependiendo de si la stack se encuentra vacia o no
     */
    public boolean empty() {
        if (count==0)
            return true;
        else
            return false;
    }




    /**
     * Metodo que permite ingresar un elemento a la Stack
     * @param element: de tipo objeto que representa el elemento dentro de la stack
     */
    public void push(Object element){

        Nodo<E> nuevoNodo = new Nodo<E> (element, null);
        count++;
        //Si hay elementos en la lista
        if (head != null){
            //colocara el nuevo nodo al final de la list
            Nodo<E> finger = head;
            while (finger.next() != null){
                finger = finger.next();
            }
            finger.setNext(nuevoNodo);
        }
        else
            //de lo contrario, colocara el nuevo nodo al principio.
            head = nuevoNodo;

    }

    /**
     * Metodo que se encarga de indicar si la Stack se encuentra llena
     * @return true/false: Dependiendo si la stack esta llena o no
     */
    public boolean full() {
        if (count==100)
            return true;
        else{
            return false;
        }
    }

    /**
     *  Metodo encargado de devolver el valor que se encuentra al incio de la Stack
     * @return (E)finger.value(): valor de tipo generico
     */
    public E top() {
        Nodo<E> finger = head;

        while (finger.next() != null){ // Encontrar el final de lista
            finger = finger.next();
        }

        return (E)finger.value();
    }

    /**
     * Metodo que se encarga de sacar un elemento del Stack
     * @return (E)finger.value(): valor de tipo generico del elemento
     */
    public E pop (){

        Nodo<E> finger = head;
        Nodo<E> previous = null;

        while (finger.next() != null){ // Encontrar el final de lista
            previous = finger;
            finger = finger.next();
        }
        // finger is null, or points to end of list
        if (previous == null) {
            // has exactly one element
            head = null;
        }
        else {
            // pointer to last element is reset
            previous.setNext(null);
        }
        count--;
        return (E)finger.value();

    }

    /**
     * Metodo que se encarga de retornar un elemento en el Stack, dada una posicion
     * @param pos: de tipo int, indica la posicion del elemento que se desea
     * @return (E)finger.value(): valor de tipo generico que devuelve el elemento encontrado en la posicion indicada
     */
    public E returning(int pos) {
        if (pos>=count)
            return (E) ("Dispo"+Integer.toString(pos+1));
        else{

            int recorrido=0;
            Nodo<E> finger = head;
            while (recorrido<pos){ // Encontrar el final de lista
                finger = finger.next();
                recorrido++;
            }
            return (E)finger.value();
        }
    }

}