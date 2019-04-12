package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_OperacionNoExistente extends Exception {

    /**
     *
     */
    public Error_OperacionNoExistente() {
        this ("Operacion no implementada");
    }

    /**
     * @param message
     */
    public Error_OperacionNoExistente(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_OperacionNoExistente(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_OperacionNoExistente(String message,
                                      Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}