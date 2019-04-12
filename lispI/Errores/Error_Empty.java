package com.example.fuent.lispinterpreter.lispI.Errores;


public class Error_Empty extends Exception {

    /**
     *
     */
    public Error_Empty() {
        this ("No ingreso nada");
    }

    /**
     * @param message
     */
    public Error_Empty(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_Empty(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_Empty(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}