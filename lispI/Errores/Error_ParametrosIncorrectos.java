package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_ParametrosIncorrectos extends Exception {

    /**
     *
     */
    public Error_ParametrosIncorrectos() {
        this("Ingreso parametros Incorrectos");
    }

    /**
     * @param message
     */
    public Error_ParametrosIncorrectos(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_ParametrosIncorrectos(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_ParametrosIncorrectos(String message,
                                       Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}