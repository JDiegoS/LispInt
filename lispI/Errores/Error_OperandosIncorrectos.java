package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_OperandosIncorrectos extends Exception {

    /**
     *
     */
    public  Error_OperandosIncorrectos () {
        this ("Ingreso operandos Incorrectos");
    }

    /**
     * @param message
     */
    public  Error_OperandosIncorrectos (String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public  Error_OperandosIncorrectos (Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public  Error_OperandosIncorrectos (String message,
                                        Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}