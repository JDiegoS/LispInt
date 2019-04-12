package com.example.fuent.lispinterpreter.lispI;

public interface Stack<E> {

    public boolean empty ();

    public boolean full ();

    public E returning(int pos);

    public void push (E element);

    public E pop ();

    public E top ();


}