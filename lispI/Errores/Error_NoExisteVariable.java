package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_NoExisteVariable extends Exception {

    /**
     *
     */
    public Error_NoExisteVariable() {
        this("Ingreso una variable que no existe");
    }

    /**
     * @param message
     */
    public Error_NoExisteVariable(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_NoExisteVariable(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_NoExisteVariable(String message,
                                  Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}