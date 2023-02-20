package queue;

import java.util.Objects;

public class ArrayQueueModule {
/*
    enqueue – добавить элемент в очередь;
    element – первый элемент в очереди;
    dequeue – удалить и вернуть первый элемент в очереди;
    size – текущий размер очереди;
    isEmpty – является ли очередь пустой;
    clear – удалить все элементы из очереди.
*/

    private static Object[] elementData;
    private static int size;
    private static int head;

    /*
        Contract
        Pre: element != null
        Post: elementData'[(head + size) % elementData.length] = element && size' = size + 1
    */
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elementData[(head + size) % elementData.length] = element;
        size++;
    }

    private static void ensureCapacity() {
        int tail = (head + size) % elementData.length;
        if (tail == head) {
            Object[] tmpQueue = new Object[elementData.length * 2];
            System.arraycopy(elementData, head, tmpQueue, 0, elementData.length - head);
            System.arraycopy(elementData, 0, tmpQueue, elementData.length - head, tail);
            elementData = tmpQueue;
            head = 0;
        }
    }

    /*
        Contract
        Pre: size > 0
        Post: R = element[head]
    */
    public static Object element() {
        assert size > 0;

        return elementData[head];
    }

    /*
        Contract
        Pre: size > 0
        Post: R = element[head] &&
            size' = size - 1 &&
            head' = (head + 1) % elementData.length &&
            elementData[head] = null;
    */
    public static Object deque() {
        assert size > 0;

        Object element = element();
        elementData[head] = null;
        head = (head + 1) % elementData.length;
        size--;
        return element;
    }

    /*
        Contract
        Pre: true
        Post: R = size
    */
    public static int size() {
        return size;
    }

    /*
        Contract
        Pre: true
        Post: R = (elementData.length == 0)
    */
    public static boolean isEmpty() {
        return elementData.length == 0;
    }

    /*
        Contract
        Pre: true
        Post: head' = 0 && size' = 0, forall (i in elementData) elementData[i] = null
    */
    public static void clear() {
        for (int i = head; i < (head + size) % elementData.length; i++) {
            elementData[i] = null;
        }
        head = 0;
        size = 0;
    }
}
