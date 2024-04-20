package com.yandex.app.model;

public class Node<E> {
    public E item;
    public Node prev;
    public Node next;

    public Node(Node<E> prev, E element,  Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
