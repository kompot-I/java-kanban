package com.yandex.app.service;

import com.yandex.app.model.Node;
import com.yandex.app.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node<Task> curNode = head;

        while (curNode != null) {
            result.add(curNode.getItem());
            curNode = curNode.getNext();
        }

        return result;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            final int id = task.getId();

            if (history.containsKey(id)) {
                remove(id);
            }

            history.put(id, linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public void removeAll(Set<Integer> idsSet) {
        for (Integer id : idsSet) {
            if (history.containsKey(id)) {
                remove(id);
            }
        }
    }

    private Node linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);

        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }

        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }

            node.setItem(null);
        }
    }
}