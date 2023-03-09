package queue;

import java.util.Objects;

/*
    Model:
    n - queue length
    queue[0]..queue[n - 1]
    headElement - first element of queue
    tailElement - last element of queue
    next(queueElement) - the next element for queueElement
    prev(queueElement) - the previous element for queueElement
    Immutable(left, right) - forall queueElement from left to right : queueElement' == queueElement
     */
public class ArrayQueueModule {
    private static Object[] elementData = new Object[16];
    private static int head = 0, size = 0;

    /*
    Contract
    Pre: element != null
    Post: Immutable(headElement, tailElement) && next(tailElement) = element && n' = n + 1
     */
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();

        int tail = (head + size) % elementData.length;
        elementData[tail] = element;
        size++;
    }

    private static void queueCopy(Object[] dest) {
        int tail = (head + size) % elementData.length, firstPartSize = elementData.length - head;
        System.arraycopy(elementData, head, dest, 0, Math.min(firstPartSize, size));
        System.arraycopy(elementData, 0, dest, Math.min(firstPartSize, size), (size < firstPartSize ? 0 : tail));
    }
    private static void ensureCapacity() {
        if (size == elementData.length) {
            Object[] tmpElementData = new Object[size * 2];
            queueCopy(tmpElementData);
            elementData = tmpElementData;
            head = 0;
        }
    }

    /*
    Contract
    Pre: element != null
    Post: Immutable(headElement, tailElement) && prev(head) = element && n' = n + 1
     */
    public static void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();

        head = (head - 1 + elementData.length) % elementData.length;
        elementData[head] = element;
        size++;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = tailElement
     */
    public static Object peek() {
        assert size > 0;

        int tail = (head + size - 1) % elementData.length;
        return elementData[tail];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = tailElement && tailElement' = prev(tailElement) && Immutable(headElement, tailElement') && n' = n - 1
     */
    public static Object remove() {
        assert size > 0;

        Object tmpElement = peek();
        int tail = (head + size - 1) % elementData.length;
        elementData[tail] = null;
        size--;
        return tmpElement;
    }

    /*
    Contract
    Pre: true
    Post: R = queue[] && Immutable(headElement, tailElement)
     */
    public static Object[] toArray() {
        Object[] tmpElementData = new Object[size];

        queueCopy(tmpElementData);
        return tmpElementData;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = headElement
     */
    public static Object element() {
        assert size > 0;

        return elementData[head];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = headElement && headElement' = next(headElement) && Immutable(headElement', tailElement) && n' = n - 1
     */
    public static Object dequeue() {
        assert size > 0;

        Object tmpElement = element();
        elementData[head] = null;
        head = (head + 1) % elementData.length;
        size--;
        return tmpElement;
    }

    /*
    Contract
    Pre: true
    Post: R = n && Immutable(headElement, tailElement)
     */
    public static int size() {
        return size;
    }

    /*
    Contract
    Pre: true
    Post: R = n == 0 && Immutable(headElement, tailElement)
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /*
    Contract
    Pre: true
    Post: n' = 0
     */
    public static void clear() {
        elementData = new Object[16];
        head = 0;
        size = 0;
    }
}
