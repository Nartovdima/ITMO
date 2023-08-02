package queue;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private Node nextElement;
        private Object nodeData;

        private Node() {

        }
        private Node(final Object element, final Node nextNode) {
            nodeData = element;
            nextElement = nextNode;
        }
    }

    private Node head;
    private Node tail;

    public LinkedQueue() {
        super.clear();
    }
    @Override
    protected Object elementImpl() {
        return head.nextElement.nodeData;
    }

    @Override
    protected void dequeueImpl() {
        head = head.nextElement;
    }

    @Override
    protected void enqueueImpl(final Object element) {
        tail.nextElement = new Node(element, null);
        tail = tail.nextElement;
    }

    @Override
    protected void clearImpl() {
        head = tail = new Node();
    }

    private boolean findElement(final Object element, final boolean deletion) {
        Node i = next(head);
        Node j = head; // j = prev(i)
//        for (Node i = next(head);)
        for (int pos = 0; pos < size; pos++) { // :NOTE: pos
            if (element.equals(i.nodeData) ) {
                if (deletion) {
                    delete(i, j);
                }
                return true;
            }
//            if (element.equals(i.nodeData)) {
//                return true;
//            }
            j = i;
            i = next(i);
        }
        return false;
    }

    @Override
    public boolean contains(final Object element) {
        return findElement(element, false);
    }

    @Override
    public boolean removeFirstOccurrence(final Object element) {
        return findElement(element, true);
    }

    private Node next(final Node element) {
        return element.nextElement;
    }

    private void delete(final Node current, final Node prev) {
        if (current == tail) {
            tail = prev;
        } else {
            prev.nextElement = next(current);
        }
        size--;
    }
}
