package queue;

import java.util.Objects;
/*
RightShift(left, right) - forall (i in (left, right]) data'[i] = data[i - 1]
 */
public class ArrayQueue extends AbstractQueue {
    private Object[] elementData;
    private int head;
    public ArrayQueue() {
        super.clear();
    }

    protected void enqueueImpl(final Object element) {
        ensureCapacity();
        final int tail = getTail();
        elementData[tail] = element;
    }

    private void queueCopy(final Object[] dest) {
        final int tail = getTail();
        final int firstPartSize = elementData.length - head;
        System.arraycopy(elementData, head, dest, 0, Math.min(firstPartSize, size));
        System.arraycopy(elementData, 0, dest, Math.min(firstPartSize, size), (size < firstPartSize ? 0 : tail));
    }
    private void ensureCapacity() {
        if (size == elementData.length - 1) {
            final Object[] tmpElementData = new Object[size * 2];
            queueCopy(tmpElementData);
            elementData = tmpElementData;
            head = 0;
        }
    }

    protected void dequeueImpl() {
        elementData[head] = null;
        head = next(head);
    }
    @Override
    protected Object elementImpl() {
        return elementData[head];
    }

    protected void clearImpl() {
        elementData = new Object[16];
        head = 0;
    }

    private int findElement(final Object element) {
        for (int i = head; i != getTail(); i = next(i)) {
            if (elementData[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    // :NOTE: common code
    @Override
    public boolean contains(final Object element) {
        return findElement(element) != -1;
    }

    @Override
    public boolean removeFirstOccurrence(final Object element) {
        final int pos = findElement(element);
        if (pos == -1) {
            return false;
        }
        size--;
        // :NOTE: arraycopy
        for (int i = pos; i != getTail(); i = next(i)) {
            elementData[i] = elementData[next(i)];
        }
        return true;
    }

    private int getTail() {
        return (head + size) % elementData.length;
    }

    private int next(final int pos) {
        return (pos + 1) % elementData.length;
    }

    private int prev(final int pos) {
        return (pos - 1 + elementData.length) % elementData.length;
    }

    /*
    Contract
    Pre: element != null
    Post: n' = n + 1 && RightShift(0, n') && data[0]' = element
     */
    public void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();

        head = prev(head);
        elementData[head] = element;
        size++;
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[n - 1] && Immutable(0, n)
     */
    public Object peek() {
        assert size > 0;

        return elementData[prev(getTail())];
    }

    /*
    Contract
    Pre: n > 0
    Post: R = data[n - 1] && n' = n - 1 && Immutable(0, n')
     */
    public Object remove() {
        assert size > 0;

        final Object tmpElement = peek();
        elementData[prev(getTail())] = null;
        size--;
        return tmpElement;
    }

    /*
    Contract
    Pre: true
    Post: R = data[] && Immutable(0, n)
     */
    public Object[] toArray() {
        final Object[] tmpElementData = new Object[size];

        queueCopy(tmpElementData);
        return tmpElementData;
    }
}
