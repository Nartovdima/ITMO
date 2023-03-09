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
public class ArrayQueueADT {
    private Object[] elementData = new Object[16];
    private int head = 0, size = 0;

    /*
    Contract
    Pre: element != null
    Post: Immutable(headElement, tailElement) && next(tailElement) = element && n' = n + 1
     */
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue);

        int tail = (queue.head + queue.size) % queue.elementData.length;
        queue.elementData[tail] = element;
        queue.size++;
    }

    private static void queueCopy(final ArrayQueueADT queue, Object[] dest) {
        int tail = (queue.head + queue.size) % queue.elementData.length, firstPartSize = queue.elementData.length - queue.head;
        System.arraycopy(queue.elementData, queue.head, dest, 0, Math.min(firstPartSize, queue.size));
        System.arraycopy(queue.elementData, 0, dest, Math.min(firstPartSize, queue.size), (queue.size < firstPartSize ? 0 : tail));
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        if (queue.size == queue.elementData.length) {
            Object[] tmpElementData = new Object[queue.size * 2];
            queueCopy(queue, tmpElementData);
            queue.elementData = tmpElementData;
            queue.head = 0;
        }
    }

    /*
    Contract
    Pre: element != null
    Post: Immutable(headElement, tailElement) && prev(head) = element && n' = n + 1
     */
    public static void push(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue);

        queue.head = (queue.head - 1 + queue.elementData.length) % queue.elementData.length;
        queue.elementData[queue.head] = element;
        queue.size++;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = tailElement
     */
    public static Object peek(final ArrayQueueADT queue) {
        assert queue.size > 0;

        int tail = (queue.head + queue.size - 1) % queue.elementData.length;
        return queue.elementData[tail];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = tailElement && tailElement' = prev(tailElement) && Immutable(headElement, tailElement') && n' = n - 1
     */
    public static Object remove(final ArrayQueueADT queue) {
        assert queue.size > 0;

        Object tmpElement = peek(queue);
        int tail = (queue.head + queue.size - 1) % queue.elementData.length;
        queue.elementData[tail] = null;
        queue.size--;
        return tmpElement;
    }

    /*
    Contract
    Pre: true
    Post: R = queue[] && Immutable(headElement, tailElement)
     */
    public static Object[] toArray(final ArrayQueueADT queue) {
        Object[] tmpElementData = new Object[queue.size];

        queueCopy(queue, tmpElementData);
        return tmpElementData;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = headElement
     */
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elementData[queue.head];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = headElement && headElement' = next(headElement) && Immutable(headElement', tailElement) && n' = n - 1
     */
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue.size > 0;

        Object tmpElement = element(queue);
        queue.elementData[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elementData.length;
        queue.size--;
        return tmpElement;
    }

    /*
    Contract
    Pre: true
    Post: R = n && Immutable(headElement, tailElement)
     */
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    /*
    Contract
    Pre: true
    Post: R = n == 0 && Immutable(headElement, tailElement)
     */
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /*
    Contract
    Pre: true
    Post: n' = 0
     */
    public static void clear(final ArrayQueueADT queue) {
        int tail = (queue.head + queue.size) % queue.elementData.length;
        for (int i = queue.head; i < tail; i = (i + 1) % queue.elementData.length) {
            queue.elementData[i] = null;
        }
        queue.head = 0;
        queue.size = 0;
    }
}
