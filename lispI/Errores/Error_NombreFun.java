package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_NombreFun extends Exception {

    /**
     *
     */
    public Error_NombreFun() {
        this("Ingreso un nombre de una funcion incorrecta");
    }

    /**
     * @param message
     */
    public Error_NombreFun(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public Error_NombreFun(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public Error_NombreFun(String message,
                           Throwable cause) {
        super(message, cause);
    }

}