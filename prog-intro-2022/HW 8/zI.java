import java.util.*;
 
public class zI {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		final int INF = 100_000_000;
		long xL = INF, xR = -INF, yL = INF, yR = -INF;
		for (int i = 0; i < n; i++) {
			int x = in.nextInt(), y = in.nextInt(), h = in.nextInt();
			xL = Math.min(x - h, xL);
			xR = Math.max(x + h, xR);
			yL = Math.min(y - h, yL);
			yR = Math.max(y + h, yR);
		}
		System.out.println(((xL + xR) / 2) + " " + ((yL + yR) / 2) +  " " + 
		(Math.max(xR - xL, yR - yL) / 2 + Math.max(xR - xL, yR - yL) % 2));
	}	
}