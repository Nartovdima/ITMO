package search;

public class BinarySearch {

    /*
    Контракт
    Pre: array args && args.length > 0 && forall (i in args), i - int &&
        forall (i, j in [1, args.length)) i < j => args[i] >= args[j]
    Post: index, forall (i, a[i] <= x) index <= i
    */
    public static void main(String[] args) {
        // args.length > 0
        int x = Integer.parseInt(args[0]);
        // Пусть a[-1] < forall (i in a) < a[a.length] && x - int
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int
        System.out.println(recursiveBinarySearch(-1, a.length, a, x));
        // index, forall (i, a[i] <= x) index <= i
    }

    /*
    Контракт
    Pre: firstElement - int, secondElement - int
    Post: firstElement > secondElement && true || firstElement <= secondElement && false
    */
    public static boolean comparator(int firstElement, int secondElement) {
        return firstElement > secondElement;
    }

    /*
    Контракт
    Pre: array a && a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int && forall (i, j in [1, args.length)) i < j => args[i] >= args[j]
    Post: index, forall (i, a[i] <= x) index <= i && right - left == 1
    */
    public static int iterativeBinarySearch(int[] a, int x) {
        // array a && a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int
        int left = -1, right = a.length;
        // array a && a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int && left == -1 && right == a.length

        // -1 <= left < right <= a.length && index, forall (i, a[i] <= x) index <= i && index in [left, right]
        while (right - left > 1) {
            int mid = (left + right) / 2;
            // -1 <= left < mid < right <= a.length

            // Докажем, что left < mid < right
            // 1) left < mid
            // 2 * mid == left + right + (left + right) % 2
            // left v mid
            // 2 * left v 2 * mid
            // 2 * left v left + right + (left + right) % 2
            // left v right + (left + right) % 2
            // left < right => left < mid

            // 2) mid < right
            // 2 * mid == (left + right) + (left + right) % 2
            // 2 * mid v 2 * right
            // left + right + (left + right) % 2 v 2 * right
            // left + (left + right) % 2 v right
            // left < right && (left + right) % 2 == 0 || 1 =>
            // условие mid < right может не выполнится, только если left + (left + right) % 2 == right
            // а такое произойдет только, если left будет на единицу меньше либо будет равен right, но имея такое условие мы уже выйдем из цикла =>
            // mid < right

            // left < mid < right && a[mid] - int && x - int
            if (comparator(a[mid], x)) {
                // a[mid] > x && left <= index <= right && forall (i, j in [1, args.length)) i < j => args[i] >= args[j]
                left = mid;
                // left' == mid
                // right' == right

                // Докажем, что left' <= index <= right'
                // left <= index <= right
                // Преобразуем выражение, которое нужно доказать left' <= index && index <= right' =>
                // => mid <= index && index <= right, в силу forall (i, j in [1, args.length)) i < j => args[i] >= args[j], a[mid] > x
                // и единственности index, то можно сказать, что искомого index не содержиться в [left, mid) => следовательно он содержиться в [mid, right].
                // left' <= index <= right'
            } else {
                // mid <= x && left <= index <= right
                right = mid;
                // left' == left
                // right' == mid

                // Докажем, что left' <= index <= right'
                // left <= index <= right
                // Преобразуем выражение, которое нужно доказать left' <= index && index <= right' =>
                // => left <= index && index <= mid, в силу forall (i, j in [1, args.length)) i < j => args[i] >= args[j], a[mid] <= x
                // и единственности index, то можно сказать, что искомого index не содержиться в (mid, right] => следовательно он содержиться в [left, mid].
                // left' <= index <= right'
            }
            // [left', right'] является подмножеством [left, right], следовательно наш отрезок уменьшился.
            // в силу того, что mid находится в [left, right], то мы в конце концов придем к left + 1 == right
        }
        // left + 1 == right && a[left] > x && x <= a[right] - это следует из того, при каком условии мы двигаем left и right
        return right;
        // index, forall (i, a[i] <= x) index <= i
    }

    /*
    Контракт
    Pre: array a && a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int && left <= index <= right && -1 <= left < right <= a.length
    Post: index, forall (i, a[i] <= x) index <= i && right - left == 1
    */
    public static int recursiveBinarySearch(int left, int right, int[] a, int x) {
        // -1 <= left < right <= a.length &&
        if (right - left == 1) {
            // -1 <= left < right <= a.length && right - left == 1
            return right;
            // index, forall (i, a[i] <= x) index <= i
        }


        // -1 <= left < right <= a.length
        int mid = (left + right) / 2;
        // -1 <= left < mid < right <= a.length

        // Докажем, что left < mid < right
        // 1) left < mid
        // 2 * mid == left + right + (left + right) % 2
        // left v mid
        // 2 * left v 2 * mid
        // 2 * left v left + right + (left + right) % 2
        // left v right + (left + right) % 2
        // left < right => left < mid

        // 2) mid < right
        // 2 * mid == (left + right) + (left + right) % 2
        // 2 * mid v 2 * right
        // left + right + (left + right) % 2 v 2 * right
        // left + (left + right) % 2 v right
        // left < right && (left + right) % 2 == 0 || 1 =>
        // условие mid < right может не выполнится, только если left + (left + right) % 2 == right
        // а такое произойдет только, если left будет на единицу меньше либо будет равен right, но имея такое условие мы уже выйдем из цикла =>
        // mid < right

        // left < mid < right && a[mid] - int && x - int
        if (comparator(a[mid], x)) {
            // a[mid] > x && left <= index <= right && forall (i, j in [1, args.length)) i < j => args[i] >= args[j]
            left = mid;
            // left' == mid
            // right' == right

            // Докажем, что left' <= index <= right'
            // left <= index <= right
            // Преобразуем выражение, которое нужно доказать left' <= index && index <= right' =>
            // => mid <= index && index <= right, в силу forall (i, j in [1, args.length)) i < j => args[i] >= args[j], a[mid] > x
            // и единственности index, то можно сказать, что искомого index не содержиться в [left, mid) => следовательно он содержиться в [mid, right].
            // left' <= index <= right'
        } else {
            // mid <= x && left <= index <= right
            right = mid;
            // left' == left
            // right' == mid

            // Докажем, что left' <= index <= right'
            // left <= index <= right
            // Преобразуем выражение, которое нужно доказать left' <= index && index <= right' =>
            // => left <= index && index <= mid, в силу forall (i, j in [1, args.length)) i < j => args[i] >= args[j], a[mid] <= x
            // и единственности index, то можно сказать, что искомого index не содержиться в (mid, right] => следовательно он содержиться в [left, mid].
            // left' <= index <= right'
        }

        // array a && a[-1] < forall (i in a) < a[a.length] && forall (i in a) i - int && x - int && left' <= index <= right' && -1 <= left' < right' <= a.length

        // [left', right'] является подмножеством [left, right], следовательно наш отрезок уменьшился.
        // в силу того, что mid находится в [left, right], то мы в конце концов придем к left + 1 == right
        return recursiveBinarySearch(left, right, a, x);
        // index, forall (i, a[i] <= x) index <= i
    }
}

