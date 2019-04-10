package com.example.fuent.lispinterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Marco Fuentes 18188, Andy Castillo 18040, Cristina Bautista 161260
 * @version 15/03/2019
 * Clase que tiene los parametros de las funciones
 */
public class Funcion {
    String nombre;//Nombre de la funcion. Es unico
    HashMap <String, String> parametros; //Lista con los parametros que recibe la funcion
    ArrayList<String> instrucciones; //Lista con las instrucciones 'en crudo'

    //Constructor
    public Funcion(String nom , ArrayList<String> param, ArrayList<String> inst){
        nombre = nom;
        instrucciones = inst;
        for (int i = 0; i<param.size();i++){
            parametros.put(param.get(i),"");
        }
    }

    /**
     * Se vuelve a escribir con hashmap, arraylist
     */
    public Funcion(){
        nombre = "";
        parametros = new HashMap();
        instrucciones = new ArrayList<>();
    }

    /**
     * @return el nombre de la funcion
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return las instrucciones que se van a guardar en un arraylist
     */
    public ArrayList<String> getInst(){
        return instrucciones;
    }

    /**
     * @param nombre el nombre de la funcion
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    /**
     * @param param agregar el parametro al hashmap
     */
    public void addParam (String param){
        parametros.put(param,"");
    }

    /**
     * @param params es el arraylist
     * @return da true o false. True cuando los argumentos son correctos; false cuando faltan o hay demasiados
     * argunentos para una funcion especifica
     */
    public boolean initParam(ArrayList<String> params){
        int contador = 0;
        if (params.size() == parametros.size()) {
            // de un hashmap el keyset
            for (String a : parametros.keySet()){
                parametros.put(a,params.get(contador));
                contador++;
            }
            return true;
        }else{/** if(params.size()<parametros.size()){
         System.out.println("ERROR - Falta de argumentos para la funcion '"+nombre+"'");
         return false;
         }else{
         System.out.println(params.size());
         System.out.println("ERROR - Demasiados argumentos para la funcion '"+nombre+"'");**/
            return false;
        }
    }

    /**
     * Se utiliza para sustituir los nuevos valores
     * Por ejemplo antes x = 5 y z = 2
     * Entonces ahora es 5 2
     */
    public void replaceParams(){
        //cuando se declaran variables estas se intercambian asi como x = 1 y a = 2 entonces es 1 y 2
        for (int i = 0; i<instrucciones.size();i++){
            String a = instrucciones.get(i);
            if  (parametros.keySet().contains(a)){
                instrucciones.set(i,parametros.get(a));
            }

        }
    }

    /**
     * @param i que es un string que se utiliza para agregar instrucciones
     */
    public void addInst(String i){
        instrucciones.add(i);
    }
    @Override
    public String toString() {
        return "Funcion{" +
                "nombre='" + nombre + '\'' +
                ", parametros=" + parametros +
                ", instrucciones=" + instrucciones +
                '}';
    }
}