package queue;

import java.util.Objects;

public class ArrayQueueADT {
/*
    enqueue – добавить элемент в очередь;
    element – первый элемент в очереди;
    dequeue – удалить и вернуть первый элемент в очереди;
    size – текущий размер очереди;
    isEmpty – является ли очередь пустой;
    clear – удалить все элементы из очереди.
*/
    private Object[] elementData;
    private int size;
    private int head;

    private ArrayQueueADT() {
    }
    /*
    Contract
    Pre: true
    Post: head = 0 && size = 0 && elementData = new Object[2]
     */
    public static ArrayQueueADT create() {
        ArrayQueueADT queue = new ArrayQueueADT();
        queue.elementData = new Object[2];
        queue.head = 0;
        queue.size = 0;
        return queue;
    }

    /*
        Contract
        Pre: element != null
        Post: elementData'[(head + size) % elementData.length] = element && size' = size + 1
    */
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue);
        queue.elementData[(queue.head + queue.size) % queue.elementData.length] = element;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        int tail = (queue.head + queue.size) % queue.elementData.length;
        if (tail == queue.head) {
            Object[] tmpQueue = new Object[queue.elementData.length * 2];
            System.arraycopy(queue.elementData, queue.head, tmpQueue, 0, queue.elementData.length - queue.head);
            System.arraycopy(queue.elementData, 0, tmpQueue, queue.elementData.length - queue.head, tail);
            queue.elementData = tmpQueue;
            queue.head = 0;
        }
    }

    /*
        Contract
        Pre: size > 0
        Post: R = element[head]
    */
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elementData[queue.head];
    }

    /*
        Contract
        Pre: size > 0
        Post: R = element[head] &&
            size' = size - 1 &&
            head' = (head + 1) % elementData.length &&
            elementData[head] = null;
    */
    public static Object deque(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object element = element(queue);
        queue.elementData[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elementData.length;
        queue.size--;
        return element;
    }

    /*
        Contract
        Pre: true
        Post: R = size
    */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /*
        Contract
        Pre: true
        Post: R = (elementData.length == 0)
    */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.elementData.length == 0;
    }

    /*
        Contract
        Pre: true
        Post: head' = 0 && size' = 0, forall (i in elementData) elementData[i] = null
    */
    public static void clear(ArrayQueueADT queue) {
        for (int i = queue.head; i < (queue.head + queue.size) % queue.elementData.length; i++) {
            queue.elementData[i] = null;
        }
        queue.head = 0;
        queue.size = 0;
    }
}
