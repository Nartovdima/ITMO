import java.util.Scanner;

public class zA {
	public static void main(String[] args) {
		int a, b, n;
		Scanner in = new Scanner(System.in);
		a = in.nextInt(); 
		b = in.nextInt();
		n = in.nextInt();
		int tmp = (n - b) / (b - a);
		if ((n - b) % (b - a) != 0)
			tmp++;
		System.out.println(2 * tmp + 1);
	}	
}