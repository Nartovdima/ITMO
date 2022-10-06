import java.util.*;
import java.io.*;

public class WordStatWordsPrefix {
	public static int insertPos;
	public static String[] words;

	public static void main(String[] args) {
		String inpFileName = args[0], outFileName = args[1];
		words = new String[1];
		insertPos = 0;
		
		inpRead(inpFileName);
		int[] cnt = new int[insertPos];

		preCalc(cnt);

		outWrite(outFileName, cnt);
	}

	public static BufferedReader openInpFile(String name) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
			new FileInputStream(name), 
			"UTF8"
		));

		return reader;
	}

	public static void inpRead(String fileName) {
		try {
			BufferedReader in = openInpFile(fileName);
			char tmpStr[] = new char[1]; 
			int tmpStrSize = 0;
			try {
				int read = in.read();

				while (read != -1) {
					if (checkChar((char) read)) {
						tmpStr = add(tmpStr, tmpStrSize, (char) read);
						tmpStrSize++;
					} else if (tmpStrSize != 0) {
						String lowCaseStr = String.valueOf(tmpStr, 0, Math.min(3, tmpStrSize));
						
						words = add(words, insertPos, lowCaseStr.toLowerCase());

						insertPos++;
						tmpStrSize = 0;
					}
					read = in.read();
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			System.out.println("Error: Input " + e.getMessage());
		}
	}

	public static BufferedWriter openOutFile(String name) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(name), 
			"UTF8"
		));

		return writer;
	}

	public static void outWrite(String fileName, int[] cnt) {
		try {
			BufferedWriter out = openOutFile(fileName);
			try {
				for (int i = 0; i < insertPos; i++) {
					if (cnt[i] != 0) {
						out.write(words[i] + " " + cnt[i]);
						out.newLine();
					}
				}
			} finally {
				out.close();
			}
		} catch (IOException e) {
			System.out.println("Error: Out " + e.getMessage());
		}
	}

	public static char[] add(char[] arr, int pos, char element) {
		if (pos == arr.length) {
			arr = Arrays.copyOf(arr, arr.length * 2);
		}
		arr[pos] = element;
		return arr;
	}

	public static String[] add(String[] arr, int pos, String element) {
		if (pos == arr.length) {
			arr = Arrays.copyOf(arr, arr.length * 2);
		}
		arr[pos] = element;
		return arr;
	}

	public static boolean checkChar(char c) {
		return (Character.isLetter(c) || 
				Character.getType(c) == Character.DASH_PUNCTUATION || 
				c == '\'');
	}

	public static void preCalc(int[] cnt) { 
		Arrays.sort(words, 0, insertPos);

		int numOfEqual = 1;
		for (int i = 1; i <= insertPos; i++) {
			if (i != insertPos && words[i - 1].compareTo(words[i]) == 0) {
				numOfEqual++;
			} else {
				cnt[i - 1] = numOfEqual;
				numOfEqual = 1;
			}
		}
	}

}