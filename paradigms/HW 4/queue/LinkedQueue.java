package queue;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private Node nextElement;
        private Object nodeData;

        private Node() {

        }
        private Node(Object element, Node nextNode) {
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
    protected void enqueueImpl(Object element) {
        tail.nextElement = new Node(element, null);
        tail = tail.nextElement;
    }

    @Override
    protected void clearImpl() {
        head = tail = new Node();
    }

    private boolean findElement(Object element, boolean deletion) {
        Node i = next(head);
        Node j = head; // j = prev(i)
        for (int pos = 0; pos < size; pos++) {
            if (element.equals(i.nodeData) && deletion) {
                delete(i, j);
                return true;
            }
            if (element.equals(i.nodeData)) {
                return true;
            }
            j = i;
            i = next(i);
        }
        return false;
    }

    @Override
    public boolean contains(Object element) {
        return findElement(element, false);
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        return findElement(element, true);
    }

    private Node next(Node element) {
        return element.nextElement;
    }

    private void delete(Node current, Node prev) {
        if (current.equals(tail)) {
            tail = prev;
        } else {
            prev.nextElement = next(current);
        }
        size--;
    }
}

