package dataStructure;

import java.util.Iterator;

/**
 * 使用链表构建堆
 */
public class MyStack<T>{
    private MyLinkedList<T> myLinkedList;
    public MyStack(){
        myLinkedList =new MyLinkedList<>();
    }

    public T pop(){
        T tmp=peek();
        myLinkedList.remove(0);
        return tmp;
    }

    public T peek(){
        return this.myLinkedList.get(0);
    }

    public void push(T tmp){
        myLinkedList.add(0, tmp);
    }

    public int size(){
        return myLinkedList.size();
    }

    public boolean isEmpty(){
        return myLinkedList.size()==0;
    }

    public Iterator<T> getIterator(){
        return new Itr();
    }

    //实现迭代器
    private class Itr implements Iterator<T>{
        int cursor=0;
        int lastRet=-1;
        public T next(){
            T next=myLinkedList.get(cursor);
            lastRet=cursor++;
            return next;
        }

        public boolean hasNext(){
            return cursor!=myLinkedList.size();
        }
    }
}
