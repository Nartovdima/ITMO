package queue;

import java.util.Objects;

/*
Model
n - size of queue
data[0]...data[n - 1] - queue
Immutable(left, right) - forall (i in [left, right - 1]) queue'[i] == queue[i]
Shift(left, right) - forall (i in [pos, right)) data'[i] = data[i + 1]
RightShift(left, right) - forall (i in (left, right]) data'[i] = data[i - 1]
isExist(element) -
if (exist i in [0, n - 1] : data[i] == element)
    true
else
    false
 */
public class ArrayQueueADT {
    private Object[] elementData = new Object[16];
    private int head = 0, size = 0;

    /*
    Contract
    Pre: element != null
    Post: Immutable(0, n) n' = n + 1 && data[n'] = element
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
    Post: n' = n + 1 && RightShift(0, n') && data[0]' = element
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
    Post: R = data[n - 1] && Immutable(0, n)
     */
    public static Object peek(final ArrayQueueADT queue) {
        assert queue.size > 0;

        int tail = (queue.head + queue.size - 1) % queue.elementData.length;
        return queue.elementData[tail];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[n - 1] && n' = n - 1 && Immutable(0, n')
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
    Post: R = data[] && Immutable(0, n)
     */
    public static Object[] toArray(final ArrayQueueADT queue) {
        Object[] tmpElementData = new Object[queue.size];

        queueCopy(queue, tmpElementData);
        return tmpElementData;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && Immutable(0, n)
     */
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elementData[queue.head];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && n' = n - 1 && Shift(0, n')
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
    Post: R = n && Immutable(0, n)
     */
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    /*
    Contract
    Pre: true
    Post: R = n == 0 && Immutable(0, n)
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
