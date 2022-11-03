import java.util.*;

public class zM {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		for (int q = 0; q < t; q++){

			Map <Integer, Integer> suitableNums = new HashMap<>();
			int n = in.nextInt();
			int[] a = new int[n];

			for (int i = 0; i < n; i++) {
				a[i] = in.nextInt();
			}
			int sum = 0;
			for (int j = n - 1; j >= 0; j--) {
				for (int i = 0; i < j; i++) {
					sum += suitableNums.getOrDefault(2 * a[j] - a[i], 0);
				}
				suitableNums.put(a[j], suitableNums.getOrDefault(a[j], 0) + 1);
			}
			System.out.println(sum);
		}
	}	
}