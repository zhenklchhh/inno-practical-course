package com.innowise.collections;

/**
 * @author Evgeniy Zaleshchenok
 */
public class LinkedList<T> {
    private Node<T> head;

    private Node<T> tail;

    private int size;

    public int size() {
        return size;
    }

    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.setNext(head);
        if (head != null) {
            head.setPrev(newNode);
        }
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.setPrev(tail);
        if (tail != null) {
            tail.setNext(newNode);
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        size++;
    }

    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size) {
            addLast(value);
            return;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> current;
        if (index <= size / 2){
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        newNode.setNext(current);
        newNode.setPrev(current.getPrev());
        current.getPrev().setNext(newNode);
        current.setPrev(newNode);
        size++;
    }

    public T getFirst() {
        if (head == null) {
            return null;
        }
        return head.getData();
    }

    public T getLast() {
        if (tail == null) {
            return null;
        }
        return tail.getData();
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current;
        if (index <= size / 2){
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        }
        else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current.getData();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T data = head.getData();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrev(null);
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T data = tail.getData();
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            tail = tail.getPrev();
            tail.setNext(null);
        }
        size--;
        return data;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        }
        Node<T> current;
        if (index <= size / 2){
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        current.getPrev().setNext(current.getNext());
        current.getNext().setPrev(current.getPrev());
        size--;
        return current.getData();
    }
}
