package com.yandex.app.service;

import com.yandex.app.model.Node;
import com.yandex.app.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    // изменили работу метода под Ноды.
    // Проверить как выводиться ????
    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node<Task> curNode = head;

        while (curNode != null) {
            result.add(curNode.item);
            curNode = curNode.next;
        }

        return result;
    }

    // вроде ворк
    @Override
    public void add(Task task) {
        if (task != null) {
            final int id = task.getId();

            if (history.containsKey(id)) {
                System.out.println("Таска " + id + "добавлена повторно");

                remove(id);
            } else {
                System.out.println("Таска " + id + "добавлена без повторений");
            }

            history.put(id, linkLast(task));
            System.out.println(history);
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);

            System.out.println("remove сработал");
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
            oldTail.next = newNode;
        }

        return newNode;
    }

//    void linkLast(E e) {
//        final LinkedList.Node<E> l = last; // берем ласт ноду
//
//        // создаем новую ноду.где ссылка на предидущую ласт нода, дата сам элемент, и ссылка на след всегда нул
//        final LinkedList.Node<E> newNode = new LinkedList.Node<>(l, e, null);
//
//        last = newNode;       // меняем инфу, что ласт нода, теперь наша новая нода. В ТЗ tail
//        if (l == null)        // если последняя до этого была null
//            first = newNode;  // то первую ноду голову делаем нашей новой нодой
//        else                  // иначе
//            l.next = newNode; // у старой последней ноды меняем ссылку на след на нашу новую ноду
//        size++;               // размер +1
//        modCount++;           //?
//    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            node.item = null;
            System.out.println("removeNode сработал");
        }
    }
}

//    public boolean remove(Object o) {
//        if (o == null) {
//            for (LinkedList.Node<E> x = first; x != null; x = x.next) {
//                if (x.item == null) {
//                    unlink(x);
//                    return true;
//                }
//            }
//        } else {
//            for (LinkedList.Node<E> x = first; x != null; x = x.next) {
//                if (o.equals(x.item)) {
//                    unlink(x); // в нашем случае из метода remove нужна только эта строка
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


//    E unlink(LinkedList.Node<E> x) {
//        // assert x != null;
//        final E element = x.item; // наша нода
//        final LinkedList.Node<E> next = x.next; // следующая от нашей
//        final LinkedList.Node<E> prev = x.prev; // предидущая от нашей
//
//        if (prev == null) { // если пред нет (наша первая)
//            first = next; // если удаляем первую, то просто меняем флаг first на след ноду
//        } else { // иначе
//            prev.next = next; // предидущая ссылается на следующую
//            x.prev = null; // наша вместо предидущей ссылается на нул
//        }
//
//        if (next == null) { // если след нет (наша последняя)
//            last = prev; // если удаляем последнюю, то просто меняем флаг last на предидущую ноду
//        } else { // иначе
//            next.prev = prev; // следующаяя ссылается на предидущую
//            x.next = null; // наша ссылается вместо следующей на нул
//        }
//
//        x.item = null; // дата в нашей == нул
//        size--; // размер --
//        modCount++; // ?
//        return element; // возвращаем нашу удалееную ноду
//    }