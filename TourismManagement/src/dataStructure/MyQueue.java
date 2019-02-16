package dataStructure;

import java.util.Iterator;

/**
 * 使用链表构建队列
 * @param <T>
 */
public class MyQueue<T> {
    private MyLinkedList<T> myLinkedList;
    public MyQueue(){
        myLinkedList=new MyLinkedList<>();
    }

    public T pop(){
        T tmp=peek();
        myLinkedList.remove(myLinkedList.size()-1);
        return tmp;
    }

    public T peek(){
        return this.myLinkedList.get(myLinkedList.size()-1);
    }

    public void push(T tmp){
        myLinkedList.add(0,tmp);
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

    //迭代器的构建
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
