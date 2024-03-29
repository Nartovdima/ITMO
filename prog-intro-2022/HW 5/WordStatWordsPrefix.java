import java.util.*;
import java.io.*;

public class WordStatWordsPrefix {
	public static int insertPos;
	public static String[] words;

	public static void main(String[] args) {
		String InpFileName = args[0], OutFileName = args[1];
		words = new String[1];
		insertPos = 0;
		
		inpRead(InpFileName);
		int[] cnt = new int[insertPos];
		int[] finalId = new int[insertPos];
		

		preCalc(cnt, finalId);
		outWrite(OutFileName, finalId, cnt);
	}

	public static void inpRead(String fileName) {
		try {
			MyScanner in = new MyScanner(new File(fileName), "UTF8");
			try {
				while (in.hasNext()) {
					String lowCaseStr = in.next();
					int wordBeg = 0, wordEnd = 0;

					while (wordEnd <= lowCaseStr.length()) {
						if ((wordEnd == lowCaseStr.length() || (!checkChar(lowCaseStr.charAt(wordEnd)))) && wordEnd > wordBeg) {
							String tmpStr = lowCaseStr.substring(wordBeg, wordBeg + Math.min(wordEnd - wordBeg, 3));
							words = add(words, insertPos, tmpStr.toLowerCase());
							insertPos++;
							wordBeg = wordEnd;
						} else if (wordBeg < lowCaseStr.length() && !checkChar(lowCaseStr.charAt(wordBeg))) {
							wordBeg++;
							wordEnd = wordBeg;
						} else {
							wordEnd++;
						}
					}
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			System.out.println("Input error: " + e.getMessage());
		} catch (SecurityException e) {
			System.out.println("Unable to reach access: " + e.getMessage());
		} 
	}

	public static BufferedWriter openOutFile(String name) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(name), 
			"UTF8"
		));

		return writer;
	}

	public static void outWrite(String fileName, int[] finalId, int[] cnt) {
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

	public static void preCalc(int[] cnt, int[] finalId) { 
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