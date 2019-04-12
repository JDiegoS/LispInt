package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_FuncionYaImplementada extends Exception {

    /**
     *
     */
    public Error_FuncionYaImplementada() {
        this("Ingreso una funcion ya implementada por GtLisp");
    }

    /**
     * @param message
     */
    public Error_FuncionYaImplementada(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_FuncionYaImplementada(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_FuncionYaImplementada(String message,
                                       Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}