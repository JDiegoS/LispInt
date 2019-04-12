package com.example.fuent.lispinterpreter.lispI;

public class Evaluar{

    private Stack<Integer> StackNumeros;// Stack en el cual se almacenan los numeros
    private Stack<Character> StackSimbolos; // Stack en el cual se almacenan los simbolos
    private boolean error;

    /**
     *Metodo constructor de la clase
     */
    public Evaluar(){
        StackSimbolos = new Listas_Stack<Character>();//Implementacion con Listas
        StackNumeros = new Listas_Stack<Integer>();

    };

    /**
     * Metodo que devuelve la evaluacion realizadas acerca de las operaciones
     * @param expression: de tipo String, se refiere a la expresion escrita por el usuario
     * @return Operaciones(): Retorna el resultado obtenido de todas las operaciones
     */
    public String Evaluar(String expression){
        error=false;
        Stacks(expression);
        if (!error)
            return Operaciones();
        else
            return "error";
    }


    /**
     * Metodo que permite analizar una operacion detenidamente y la secciona en sus partes
     * @param expression: de tipo String, se refiere a la expresion que contiene la operacion
     */
    public void Stacks(String expression){

        boolean numeros=false;
        String numero="";

        int i=0;
        while (i<expression.length()){

            char exp = expression.charAt(i);

            //Detecta que es un numero y formara una cadena de numeros
            if (exp>='0' && exp<='9'){
                if (!numeros)
                    numeros=true;
                numero += exp;
            }

            //Detecta si la expresion es un simbulo de operacion aritmetica
            else
            if (exp == '+' || exp == '*' || exp == '-' || exp == '/'){
                StackSimbolos.push(exp); //Se ingresara esta cadena al stack de simbolos
                if (numeros){
                    StackNumeros.push( Integer.parseInt( numero ) ); //Se ingresara esta cadena al stack de numeros
                    numeros=false;
                    numero="";
                }
            }
            else
                //Detecta que es un espacio en blanco
                if (exp==' '){
                    if (numeros){
                        StackNumeros.push( Integer.parseInt( numero ) ); //Se ingresara esta cadena al stack numerico
                        numeros=false;
                        numero="";
                    }
                }
                //Detecta que hay algo que no es numero ni simbolo
                else{
                    error=true;
                }

            i++;
        }

        if (numeros) StackNumeros.push( Integer.parseInt( numero ) );  //Se ingresara esta cadena al stack numerico
    }
    /**
     * Metodo que permite realizar la operacion de los numeros ingresados
     * @return resultado: Se obtiene el resultado de una operacion aritmetica
     */
    public String Operaciones(){

        int resultado=StackNumeros.pop();

        while (!StackSimbolos.empty()){

            int operando = StackNumeros.pop();
            char simbolo = StackSimbolos.pop();

            switch (simbolo){
                case '+': resultado += operando;
                    break;
                case '-': resultado -= operando;
                    break;
                case '*': resultado *= operando;
                    break;
                case '/': resultado /= operando;
                    break;
            }
        }

        return Integer.toString(resultado);
    }




}