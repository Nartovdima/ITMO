import java.util.*;

public class ReverseOctDec {
	public static int[][] add(int[][] arr, int[] element, int pos) {
		if (pos == arr.length) {
			// Arrays.copyOf
			arr = Arrays.copyOf(arr, arr.length * 2);
		}
		arr[pos] = element;
		return arr;
	}

	public static int[] add(int[] arr, int element, int pos) {
		if (pos == arr.length) {
			// Arrays.copyOf
			arr = Arrays.copyOf(arr, arr.length * 2);
		}
		arr[pos] = element;
		return arr;
	}

	public static void main(String args[]) {
		int realSize = 0, secondDimension = 0;
		int[][] dataSheet = new int[1][];
		int[] endOfLines = new int[1];
		MyScanner input = new MyScanner(System.in);

		for (; input.hasNextLine(); realSize++) {
			MyScanner currLine = new MyScanner(input.nextLine());

			dataSheet = add(dataSheet, new int[1], realSize);

			int j = 0;
			for (; currLine.hasNextInt(); j++) {
				//dataSheet[i][j] = currLine.nextInt();
				dataSheet[realSize] = add(dataSheet[realSize], currLine.nextInt(), j);
			}

			endOfLines = add(endOfLines, j, realSize);
			secondDimension = Math.max(secondDimension, j);
		}
		/*for (int i = 0; i < realSize; i++) {
			for (int j = 0; j < endOfLines[i]; j++) {
				System.err.print(dataSheet[i][j] + " ");
			}
			System.err.println();
		}*/

		for (int i = realSize - 1; i >= 0; i--) {
			for (int j = endOfLines[i] - 1; j >= 0; j--) {
				System.out.print(dataSheet[i][j]); 
				System.out.print(' ');
			}
			System.out.println();
		}
	}
}
