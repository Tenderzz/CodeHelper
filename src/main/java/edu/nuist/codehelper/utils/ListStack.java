package edu.nuist.codehelper.utils;

import java.util.LinkedList;

public class ListStack<T>{
    private LinkedList<T> ll = new LinkedList<>();

    //入栈
    public void push(T t) {
        ll.addFirst(t);
    }

    //出栈
    public T pop() {
        return ll.removeFirst();
    }

    //栈顶元素
    public T peek() {
        T t = null;
        //直接取元素会报异常，需要先判断是否为空
        if (!ll.isEmpty())
            t = ll.getFirst();
        return t;
    }


}
