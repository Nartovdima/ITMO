import java.util.*;
 
public class zH {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int n = in.nextInt();
		int[] a = new int[n];
		int mxA = -1;
		int sumA = 0;
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
			mxA = Math.max(mxA, a[i]);
			sumA += a[i];
		}

		int[] buckets = new int[sumA];
		int[] startOfBucket = new int[n];
		int currNum = 0;
		for (int i = 0; i < n; i++) {
			startOfBucket[i] = currNum;
			for (int j = 0; j < a[i]; j++) {
				buckets[currNum + j] = i;
			}
			currNum += a[i];
		}

		int q = in.nextInt();
		int[] numsOfbatches = new int[sumA];
		
		for (int i = 0; i < q; i++) {
			int t = in.nextInt();
			if (numsOfbatches[t - 1] == 0) {
				currNum = 0;
				int ans = 0;

				for (;;ans++) {
					int endNum = currNum + t;
					if (endNum >= sumA) {
						numsOfbatches[t - 1] = ans;
						break;
					} else if (buckets[currNum] == buckets[endNum]) {
						numsOfbatches[t - 1] = -1;
						break;
					} else {
						currNum = startOfBucket[buckets[endNum]];
					}
				}
			}

			if (numsOfbatches[t - 1] == -1) {
				System.out.println("Impossible");
			} else {
				System.out.println(numsOfbatches[t - 1] + 1);
			}
		}
	}	
}