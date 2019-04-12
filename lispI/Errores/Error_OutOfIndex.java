package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_OutOfIndex extends Exception {

    /**
     *
     */
    public Error_OutOfIndex () {
        this("Inserto cosas de mas en la expresion");
    }

    /**
     * @param message
     */
    public Error_OutOfIndex (String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_OutOfIndex (Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_OutOfIndex (String message,
                             Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}