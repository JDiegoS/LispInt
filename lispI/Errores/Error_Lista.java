package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_Lista extends Exception {

    /**
     *
     */
    public Error_Lista() {
        this("Mal ingreso de una Lista");
    }

    /**
     * @param mensaje
     */
    public Error_Lista(String mensaje) {
        /*
         * Usar el constructor del padre
         */
        super(mensaje);
    }

    /**
     * @param cause
     */
    public Error_Lista(Throwable causa) {
        super(causa);
    }

    /**
     * @param mensaje
     * @param causa
     */
    public Error_Lista(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}