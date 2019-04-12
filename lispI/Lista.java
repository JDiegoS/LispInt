package com.example.fuent.lispinterpreter.lispI;

import com.example.fuent.lispinterpreter.lispI.Errores.Error_MalInterprete;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Jennifer Sandoval,Andrea Paniagua, Diego Solorzano
 * @Carne 18962,18733,18151
 * @date 06/03/19
 * @name Lista.java
 * <p>Esta clase contiene todas las funciones necesarias para una lista </p>
 * */
public class Lista extends ArrayList implements List{
    public boolean esOperacion;

    /**
     * Constructor de la lista
     * Usa el contructor de ArrayList()
     */
    public Lista() {
        super();
    }

    /**
     * Constructor a partir de una lista
     * @param lista lista a copiar
     */
    public Lista(List lista){
        super();

        if (lista == null){
            lista = new Lista();
        }

        int i = 0;

        while (i < lista.size()){
            this.add(lista.get(i));
            i++;
        }
    }
    /**
     * Metodo que verifica si dos listas son iguales
     *
     * @return true, si las dos listas tienen los mismos elementos
     */

    public void AgregarEn(int indice, Atomo atomo){
        this.add(indice, atomo);
    }

    public Atomo remplazarEn_Por(int i, Atomo atomoRemplazante) {
        Atomo atomoRemplazado = this.removeAtomoEn(i);
        this.AgregarEn(i, atomoRemplazante);

        return atomoRemplazado;
    }


    public Atomo getAtomoEn(int i) {
        return (Atomo) this.get(i);
    }

    private Atomo removeAtomoEn(int i) {
        return (Atomo) this.remove(i);
    }

    public void AgregarAlFinal(Atomo atomo){
        this.add(atomo);
    }



    public Lista(Atomo atomoIngresado) {
        super();

        this.add(atomoIngresado);
    }

    public boolean existe(Atomo atomo){
        int i = 0;

        while (i <= this.size() -1){
            if (this.get(i).equals(atomo))
                return true;
            i++;
        }

        return false;
    }

    public boolean IsEmpty(){
        return this.size()==0;
    }
    public boolean equals(Object objetoLista){
        Lista otraLista = (Lista)objetoLista;

        if (this.size()!=otraLista.size())
            return false;

        int indice = 0;
        while (indice < this.size()){
            if (!this.get(indice).equals(otraLista.get(indice)))
                return false;
        }

        return true;
    }
    /**
     * Metodo que devuelve la operacion de la lista si es una lista
     * @return La operacion de esta lista
     */
    public Atomo getOperacion() throws Error_MalInterprete {
        if (!this.esOperacion)
            throw new Error_MalInterprete();

        return (Atomo)this.get(0);
    }


}