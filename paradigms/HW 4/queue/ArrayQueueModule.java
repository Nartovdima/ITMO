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
public class ArrayQueueModule {
    private static Object[] elementData = new Object[16];
    private static int head = 0, size = 0;

    /*
    Contract
    Pre: element != null
    Post: Immutable(0, n) n' = n + 1 && data[n'] = element
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
    Post: n' = n + 1 && RightShift(0, n') && data[0]' = element
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
    Post: R = data[n - 1] && Immutable(0, n)
     */
    public static Object peek() {
        assert size > 0;

        int tail = (head + size - 1) % elementData.length;
        return elementData[tail];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[n - 1] && n' = n - 1 && Immutable(0, n')
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
    Post: R = data[] && Immutable(0, n)
     */
    public static Object[] toArray() {
        Object[] tmpElementData = new Object[size];

        queueCopy(tmpElementData);
        return tmpElementData;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && Immutable(0, n)
     */
    public static Object element() {
        assert size > 0;

        return elementData[head];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && n' = n - 1 && Shift(0, n')
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
    Post: R = n && Immutable(0, n)
     */
    public static int size() {
        return size;
    }

    /*
    Contract
    Pre: true
    Post: R = n == 0 && Immutable(0, n)
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
