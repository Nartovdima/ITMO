import java.util.Scanner;

public class Reverse {

	static int getNumOfChars(String currLine) {
		Scanner tmpLine = new Scanner(currLine);
		int numOfChars = 0;

		while (tmpLine.hasNextInt()) {
			int a = tmpLine.nextInt();
			numOfChars++;
		}

		return numOfChars;
	}

	public static void main(String args[]) {
		int MAX_SIZE = 1_000_000;
		int realSize = 0;
		
		int[][] dataSheet = new int[MAX_SIZE][];
		Scanner input = new Scanner(System.in);

		for (int i = 0; input.hasNextLine(); i++) {
			String strCurrLine = input.nextLine();
			Scanner currLine = new Scanner(strCurrLine);
			dataSheet[i] = new int[getNumOfChars(strCurrLine)];

			for (int j = 0; currLine.hasNextInt(); j++) {
				dataSheet[i][j] = currLine.nextInt();
			}
			realSize++;
		}

		for (int i = realSize - 1; i >= 0; i--) {
			for (int j = dataSheet[i].length - 1; j >= 0; j--) {
				System.out.print(dataSheet[i][j]); 
				System.out.print(' ');
			}
			System.out.println();
		}
		
	}
}