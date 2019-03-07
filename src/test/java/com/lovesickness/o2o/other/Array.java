package com.lovesickness.o2o.other;

import java.util.ArrayList;
import java.util.List;

public class Array<T> {
    private ArrayList<T> a = new ArrayList(10);
    public Array(ArrayList<T> a){
        this.a = a;
    }

    public ArrayList<T> getA() {
        return a;
    }

    public void add(T t){
        a.add(t);
    }
    public void addAll(List<T> tList){
        for (T t:tList) {
            add(t);
        }
    }
}
