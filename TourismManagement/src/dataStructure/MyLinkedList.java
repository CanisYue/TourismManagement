package dataStructure;
import java.util.Iterator;

/**
 * 构建链表
 */
@SuppressWarnings("ALL")
public class MyLinkedList<T> {
    //头结点，头结点是为了方便在下面的方法中遍历链表用的
    private Node head = new Node(null);
    private int size;
    //私有节点类
    private class Node<T> {
        Node next;
        T t;

        Node(T t) {
            this.t = t;
        }
    }
    public MyLinkedList() {

    }

    public int getSize() {
        return size;
    }

    public int size(){
        return size;
    }

    /**
     * 获取指定位置的节点
     */
    public T get(int i) {
        if (i < 0 || i > size - 1) {
            throw new ArrayIndexOutOfBoundsException("获取的位置不合法");
        } else {
            //把第一个节点给临时节点temp，让temp遍历
            Node temp = head;
            //counter用来计数，找到i在链表里的节点位置,头结点不算链表的真实节点，所以从-1开始计数
            int counter = -1;
            while (temp != null) {
                if (counter == i) {
                    return (T) temp.t;
                }
                temp = temp.next;
                counter++;
            }
        }
        return null;
    }

    /**
     * 添加元素
     */
    public void add(T t) {
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = new Node(t);
        size++;
    }

    /**
     * 检测是否含有指定元素
     */
    public boolean contain(T t) {
        Iterator<T> it = getIterator();
        while (it.hasNext()) {
            if (it.next().equals(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不重复添加：若带添加元素存在于链表中，返回false
     */
    public boolean addNotSame(T t){
        Node temp = head;
        while (temp.next != null) {
            if(t.equals(temp.next.t))
                return false;
            temp = temp.next;
        }
        temp.next = new Node(t);
        size++;
        return true;
    }
    /**
     * 指定位置添加元素
     */
    public void add(int i, T t) {
        if (i < 0 || i > size) {
            throw new ArrayIndexOutOfBoundsException("插入的位置不合法");
        } else {
            Node temp = head;
            int counter = -1;
            while (temp != null) {
                if ((i - 1) == counter) {
                    //将i前面的节点指向node，node的指向i节点
                    Node node = new Node(t);
                    Node back = temp.next;
                    temp.next = node;
                    node.next = back;
                    size++;
                }
                temp = temp.next;
                counter++;
            }
        }
    }


    /**
     * 删除指定位置的节点
     */
    public void remove(int i) {
        if (i < 0 || i > size) {
            System.out.println(i);
            throw new ArrayIndexOutOfBoundsException("删除的位置不合法");
        } else {
            Node temp = head;
            int counter = -1;
            while (temp != null) {
                //将i前面的节点指向i后面的节点
                if ((i - 1) == counter) {
                    temp.next = temp.next.next;
                    size--;
                }
                counter++;
                temp = temp.next;
            }
        }
    }

    /**
     * 迭代器实现
     */
    public Iterator<T> getIterator(){
        return new Itr();
    }

    private class Itr implements Iterator<T>{
        int cursor=0;
        int lastRet=-1;
        public T next(){
            T next=get(cursor);
            lastRet=cursor++;
            return next;
        }

       public boolean hasNext(){
            return cursor!=size;
       }
    }

    public boolean isEmpty(){
        return size==0;
    }


}
