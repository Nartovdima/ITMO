package queue;

/*
Model
n - size of queue
data[0]...data[n - 1] - queue
Immutable(left, right) - forall (i in [left, right - 1]) queue'[i] == queue[i]
Shift(left, right) - forall (i in [left, right)) data'[i] = data[i + 1]
isExist(element) - exist i in [0, n - 1] : data[i] == element
 */
public interface Queue {
    /*
    Contract
    Pre: element != null
    Post: Immutable(0, n) n' = n + 1 && data[n'] = element
     */
    void enqueue(Object element);

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && Immutable(0, n)
     */
    Object element();

    /*
    Contract
    Pre: n > 0
    Post: R = data[0] && n' = n - 1 && Shift(0, n')
     */
    Object dequeue();

    /*
    Contract
    Pre: true
    Post: R = n && Immutable(0, n)
     */
    int size();

    /*
    Contract
    Pre: true
    Post: R = n == 0 && Immutable(0, n)
     */
    boolean isEmpty();

    /*
    Contract
    Pre: true
    Post: n' = 0
     */
    void clear();

    /*
    Contract
    Pre: element != null
    Post: R = isExist(element) && Immutable(0, n)
     */
    boolean contains(Object element);

    // :NOTE: min?
    /*
    Contract
    Pre: n > 0
    Post: R = isExist(element) &&
    if (isExist(element))
        elementPos = min pos : data[pos] == element
        Immutable(0, pos) && n' = n - 1 && Shift(pos, n')
    else
        Immutable(0, n)
     */
    boolean removeFirstOccurrence(Object element);
}
