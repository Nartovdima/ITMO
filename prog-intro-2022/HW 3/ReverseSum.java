import java.util.*;

public class ReverseSum {
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
		Scanner input = new Scanner(System.in);

		for (; input.hasNextLine(); realSize++) {
			Scanner currLine = new Scanner(input.nextLine());

			dataSheet = add(dataSheet, new int[1], realSize);

			int j = 0;
			for (; currLine.hasNextInt(); j++) {
				//dataSheet[i][j] = currLine.nextInt();
				dataSheet[realSize] = add(dataSheet[realSize], currLine.nextInt(), j);
			}

			endOfLines = add(endOfLines, j, realSize);
			secondDimension = Math.max(secondDimension, j);
		}

		int[] row = new int[realSize];
		int[] column = new int[secondDimension];

		for (int i = 0; i < realSize; i++) {
			for (int j = 0; j < endOfLines[i]; j++) {
				row[i] += dataSheet[i][j];
				column[j] += dataSheet[i][j];
			}
		}

		for (int i = 0; i < realSize; i++) {
			for (int j = 0; j < endOfLines[i]; j++) {
				System.out.print(row[i] + column[j] - dataSheet[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		
	}
}
