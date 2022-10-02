import java.util.*;
import java.io.*;

public class WordStatInput {
	public static int insertPos;
	public static String[] words;
	public static int[] wordsId; 

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

	public static String[] add(String[] arr, String element, int pos) {
		if (pos == arr.length) {
			String[] tmp = new String[arr.length * 2];
			System.arraycopy(arr, 0, tmp, 0, arr.length);
			arr = tmp;
		}
		arr[pos] = element;
		return arr;
	}

	public static boolean checkChar(char c) {
		return (Character.isLetter(c) || 
				Character.getType(c) == Character.DASH_PUNCTUATION || 
				c == '\'');
	}

	public static void parseString(String line) {
		int wordBeg = 0, wordEnd = 0;
		while (wordEnd <= line.length()) {
			if ((wordEnd == line.length() || !checkChar(line.charAt(wordEnd))) && wordBeg < wordEnd) {
				String lowCaseStr = line.substring(wordBeg, wordEnd);
				lowCaseStr = lowCaseStr.toLowerCase();
				words = add(words, lowCaseStr, insertPos);
				insertPos++;
				wordBeg = wordEnd;
			} else if (wordBeg < line.length() && !checkChar(line.charAt(wordBeg))) {
				wordBeg++;
				wordEnd = wordBeg;
			} else {
				wordEnd++;
			}
		}
	}

	public static void swap (String[] words, int pointer1, int pointer2) {
		String tmp = words[pointer1];
		words[pointer1] = words[pointer2];
		words[pointer2] = tmp; 
	}

	public static void swap (int[] words, int pointer1, int pointer2) {
		int tmp = wordsId[pointer1];
		wordsId[pointer1] = wordsId[pointer2];
		wordsId[pointer2] = tmp; 
	}

	public static void quickSort(int l, int r) {
		if (r - l <= 1) {
			return;
		}
		Random randomNum = new Random();
		String mid = words[l + randomNum.nextInt(r - l)];
		int pntr1 = l;
		for (int i = l; i < r; i++) {
			if (mid.compareTo(words[i]) >= 0) {
				swap(words, i, pntr1);
				swap(wordsId, i, pntr1);
				pntr1++;
			}
		}
		int pntr2 = pntr1;
		for (int i = l; i < pntr2; i++) {
			if (mid.compareTo(words[i]) == 0) {
				pntr2--;
				swap(words, i, pntr2);
				swap(wordsId, i, pntr2);
			}
		}
		quickSort(l, pntr2);
		quickSort(pntr1, r);
	}

	public static void preCalc(int[] cnt, int[] finalId) { // need fixing
		wordsId = new int[insertPos];
		for (int i = 0; i < insertPos; i++) {
			wordsId[i] = i;
		}

		quickSort(0, insertPos);
		int mn = wordsId[0], numOfEqual = 1;
		for (int i = 1; i <= insertPos; i++) {
			if (i != insertPos && words[i - 1].compareTo(words[i]) == 0) {
				mn = Math.min(mn, wordsId[i]);
				numOfEqual++;
			} else {
				finalId[mn] = i;
				cnt[i - 1] = numOfEqual;
				numOfEqual = 1;
				if (i != insertPos) {
					mn = wordsId[i];
				}
			}
		}
	}

	public static BufferedReader openInpFile(String name) throws IOException {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(name), "UTF8")
		);

		return reader;
	}


	public static void inpRead(String fileName) {
		try {
			BufferedReader in = openInpFile(fileName);
			try {
				while (true) {
					String line = in.readLine();
					if (line == null)
						break;
					parseString(line);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			System.out.println("Input file not found: " + e);
		}
	}

	public static BufferedWriter openOutFile(String name) throws IOException {
		BufferedWriter writer = new BufferedWriter(
			new OutputStreamWriter(new FileOutputStream(name), "UTF8")
		);

		return writer;
	}

	public static void outWrite(String fileName, int[] finalId, int[] cnt) {
		try {
			BufferedWriter out = openOutFile(fileName);
			try {
				for (int i = 0; i < insertPos; i++) {
					if (finalId[i] != 0) {
						out.write(words[finalId[i] - 1] + " " + cnt[finalId[i] - 1]);
						out.newLine();
					}
				}
			} finally {
				out.close();
			}
		} catch (IOException e) {
			System.out.println("Output file not found " + e);
		}
	}


}