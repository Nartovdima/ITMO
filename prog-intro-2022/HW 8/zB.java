import java.util.Scanner;

public class zB {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int num = -710, startPoint = 25000;
		for (int i = 0; i < n; i++, startPoint--) {
			System.out.println(startPoint * num);
		}
	}	
}