package com.example.fuent.lispinterpreter.lispI.Errores;


public class Error_MalInterprete extends Exception {

    /**
     *
     */
    public Error_MalInterprete() {
        this("Error interno");
    }

    /**
     * @param message
     */
    public Error_MalInterprete(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_MalInterprete(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_MalInterprete(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}