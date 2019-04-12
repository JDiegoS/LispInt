package com.example.fuent.lispinterpreter.lispI;

import android.os.Build;

import com.example.fuent.lispinterpreter.lispI.Errores.Error_ExpresionMalBalanceada;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_FuncionYaImplementada;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_NoExisteVariable;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OperacionNoExistente;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OperandosIncorrectos;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_OutOfIndex;
import com.example.fuent.lispinterpreter.lispI.Errores.Error_ParametrosIncorrectos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Interprete {
    private static Env globalEnvironment = new Env();
    //Declaracion del ambiente lisp
    private static class Env {
        private Map<Object, Object> inner = new HashMap<Object, Object>();
        private Env outer;

        public Env(Env outer, Object[] param, Object[] args) {
            this.outer = outer;
            if (param != null) {
                for (int i = 0; i < param.length; ++i) {
                    inner.put(param[i], args[i]);
                }
            }
        }
        //Construccion del ambiente lisp
        public Env() {
            this(null, null, null);
        }

        public void put(Object key, Object value) {
            inner.put(key, value);
        }

        public Object get(Object exp) {
            Object value = inner.get(exp);
            if (value != null)
                return value;
            if (outer != null)
                return outer.get(exp);
            return null;
        }

        public boolean containsKey(Object key) {
            if (inner.containsKey(key))
                return true;
            if (outer != null)
                return outer.containsKey(key);
            return false;
        }
    }

    private static String[] tokenize(String code) {
        return code.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").trim().split("\\s+");
    }
    //Los atomos que son numeros se convierten en numeros ya sea int, double o float
    private static Object atom(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            try {
                return Float.parseFloat(token);
            } catch (NumberFormatException e2) {
                try {
                    return Double.parseDouble(token);
                } catch (NumberFormatException e3) {
                    return token;
                }
            }
        }
    }

    private static Object read(List<String> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Token empty");
        }
        String token = tokens.remove(0);

        if (token.equals("(")) {
            List<Object> atoms = new ArrayList<Object>(tokens.size() - 1);
            while (!tokens.get(0).equals(")"))
                atoms.add(read(tokens));
            tokens.remove(0);
            return atoms;
        } else if (token.equals(")")) {
            throw new Exception("unexpected ')'");
        } else {
            return atom(token);
        }
    }

    private static Object parse(String code) throws Exception {
        return read(new ArrayList<String>(Arrays.asList(tokenize(code))));
    }

    private static Object eval(Object exp, Env env) {
        if (exp instanceof String) {
            return env.get(exp);
        } else if (exp instanceof List) {
            List<Object> atoms= (List<Object>) exp;
            Object token = atoms.get(0);
            if (token instanceof String) {
                String symbol = (String) token;
                switch (symbol) {
                    case "COND": {
                        boolean result = !eval(atoms.get(1), env).equals(0);
                        Object action = result ? atoms.get(2) : atoms.get(3);
                        return eval(action, env);
                    }
                    case "DEFUN": {
                        Object result = eval(atoms.get(2), env);
                        env.put(atoms.get(1), result);
                        return result;
                    }
                    case "set!":
                        Object key = atoms.get(1);
                        if (!env.containsKey(key)) {
                            String indef = "Variable indefinida";
                            return indef;
                        }
                        Object result = eval(atoms.get(2));
                        env.put(key, result);
                        return result;
                    case "lambda":
                        List<Object> params = (List<Object>) atoms.get(1);
                        return (Function<Object[], Object>)(x) -> {
                            return eval(atoms.get(2), new Env(env, params.toArray(), x));
                        };
                    default:
                        Object proc = env.get(token);
                        Object[] args = new Object[atoms.size() - 1];
                        for (int i = 0; i < args.length; ++i) {
                            args[i] = eval(atoms.get(i + 1), env);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (proc instanceof BinaryOperator) {
                                BinaryOperator<Object> op = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    op = (BinaryOperator<Object>) proc;
                                }
                                return op.apply(args[0], args[1]);
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if (proc instanceof Function) {

                                    Function<Object[], ?> fnc = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        fnc = (Function<Object[], ?>) proc;
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        return fnc.apply(args);
                                    }
                                    return proc;
                                }
                            }
                        }
                }
            }
        } else if (exp instanceof Number) {
            return exp;
        }
        return null;
    }

    private static Object eval(Object exp) {
        return eval(exp, globalEnvironment);
    }
    // Calcula la suma de 2 numeros
    private static Number add(Number n1, Number n2) {
        if (n1 instanceof Double || n2 instanceof Double) {
            return n1.doubleValue() + n2.doubleValue();
        } else if (n1 instanceof Float || n2 instanceof Float) {
            return n1.floatValue() + n2.floatValue();
        } else if (n1 instanceof Integer || n2 instanceof Integer) {
            return n1.intValue() + n2.intValue();
        } else if (n1 instanceof Long || n2 instanceof Long) {
            return n1.longValue() + n2.longValue();
        } else if (n1 instanceof Short || n2 instanceof Short) {
            return n1.shortValue() + n2.shortValue();
        }

        return n1.byteValue() + n2.byteValue();
    }
    //Calcula la resta de 2 numeros
    private static Number substract(Number n1, Number n2) {
        if (n1 instanceof Double || n2 instanceof Double) {
            return n1.doubleValue() - n2.doubleValue();
        } else if (n1 instanceof Float || n2 instanceof Float) {
            return n1.floatValue() - n2.floatValue();
        } else if (n1 instanceof Integer || n2 instanceof Integer) {
            return n1.intValue() - n2.intValue();
        } else if (n1 instanceof Long || n2 instanceof Long) {
            return n1.longValue() - n2.longValue();
        } else if (n1 instanceof Short || n2 instanceof Short) {
            return n1.shortValue() - n2.shortValue();
        }

        return n1.byteValue() + n2.byteValue();
    }
    // Calcula la multiplicacion de 2 numeros
    private static Number multiply(Number n1, Number n2) {
        if (n1 instanceof Double || n2 instanceof Double) {
            return n1.doubleValue() * n2.doubleValue();
        } else if (n1 instanceof Float || n2 instanceof Float) {
            return n1.floatValue() * n2.floatValue();
        } else if (n1 instanceof Integer || n2 instanceof Integer) {
            return n1.intValue() * n2.intValue();
        } else if (n1 instanceof Long || n2 instanceof Long) {
            return n1.longValue() * n2.longValue();
        } else if (n1 instanceof Short || n2 instanceof Short) {
            return n1.shortValue() * n2.shortValue();
        }

        return n1.byteValue() * n2.byteValue();
    }
    // Calcula la division de dos numeros
    private static Number divide(Number n1, Number n2) {
        if (n1 instanceof Double || n2 instanceof Double) {
            return n1.doubleValue() / n2.doubleValue();
        } else if (n1 instanceof Float || n2 instanceof Float) {
            return n1.floatValue() / n2.floatValue();
        } else if (n1 instanceof Integer || n2 instanceof Integer) {
            return n1.intValue() / n2.intValue();
        } else if (n1 instanceof Long || n2 instanceof Long) {
            return n1.longValue() / n2.longValue();
        } else if (n1 instanceof Short || n2 instanceof Short) {
            return n1.shortValue() / n2.shortValue();
        }

        return n1.byteValue() * n2.byteValue();
    }
    //Calcula el valor absoluto de un numero
    private static Number abs(Number val) {
        if (val instanceof Double) {
            return Math.abs(val.doubleValue());
        } else if (val instanceof Float) {
            return Math.abs(val.floatValue());
        } else if (val instanceof Long) {
            return Math.abs(val.longValue());
        }

        return Math.abs(val.intValue());
    }




    public String met(String codigo){
        String cod = "";
        globalEnvironment.put("+", (BinaryOperator<Number>) Interprete::add);
        globalEnvironment.put("*", (BinaryOperator<Number>) Interprete::multiply);
        globalEnvironment.put("-", (BinaryOperator<Number>) Interprete::substract);
        globalEnvironment.put("/", (BinaryOperator<Number>)Interprete::divide);
            try {

                cod += Arrays.toString((tokenize(codigo))) + " ";
                cod += parse(codigo) + " ";
                cod += eval(parse((codigo))) + " ";
                return cod;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cod;
        }


    }
