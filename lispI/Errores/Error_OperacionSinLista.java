package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_OperacionSinLista extends Exception {

    /**
     *
     */
    public Error_OperacionSinLista() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public Error_OperacionSinLista(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_OperacionSinLista(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_OperacionSinLista(String message,
                                   Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}