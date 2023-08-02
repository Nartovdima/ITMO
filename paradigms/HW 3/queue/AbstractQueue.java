package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;


    protected abstract Object elementImpl();
    protected abstract void dequeueImpl();
    protected abstract void enqueueImpl(Object element);
    protected abstract void clearImpl();
    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);

        enqueueImpl(element);
        size++;
    }

    @Override
    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    @Override
    public Object dequeue() {
        assert size > 0;

        Object tmpElement = element();
        dequeueImpl();
        size--;
        return tmpElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        clearImpl();
        size = 0;
    }
}
