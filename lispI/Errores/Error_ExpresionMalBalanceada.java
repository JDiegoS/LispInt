package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_ExpresionMalBalanceada extends Exception {

    /**
     *
     */
    public Error_ExpresionMalBalanceada () {
        this("Expresion Mal Balanceada");
    }

    /**
     * @param message
     */
    public Error_ExpresionMalBalanceada (String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public Error_ExpresionMalBalanceada (Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_ExpresionMalBalanceada (String message, Throwable cause) {
        super(message, cause);
    }

}