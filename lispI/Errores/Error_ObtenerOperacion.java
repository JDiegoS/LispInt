package com.example.fuent.lispinterpreter.lispI.Errores;

public class Error_ObtenerOperacion extends Exception {

    /**
     *
     */
    public Error_ObtenerOperacion() {
        this("Excepcion al tratar de obtener una operacion de donde no existe");
    }

    /**
     * @param message
     */
    public Error_ObtenerOperacion(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public Error_ObtenerOperacion(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public Error_ObtenerOperacion(String message,
                                  Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}