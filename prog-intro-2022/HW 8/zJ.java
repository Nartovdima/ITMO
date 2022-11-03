import java.util.*;
 
public class zJ {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int[][] a = new int[n][n];
		final int MOD = 10;

		for (int i = 0; i < n; i++) {
			String tmp = in.next();
 			for (int j = 0; j < n; j++) {
 				a[i][j] = Character.digit(tmp.charAt(j), 10);
 			}
		}

		for (int k = 0; k < n; k++) {
			for (int j = k + 1; j < n; j++) {

				if (a[k][j] == 0) {
					continue;
				}

				for (int i = j + 1; i < n; i++) {
					a[k][i] = (a[k][i] - a[j][i] + MOD) % MOD;
				}
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(a[i][j]);
			}
			System.out.println();
		}
	}	
}