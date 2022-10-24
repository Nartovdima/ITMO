import java.util.*;
import java.io.*;

class Pair {
	public IntList numOfLines;
	public IntList positions;

	public Pair() {
		numOfLines = new IntList();
		positions = new IntList();
	}
}

public class WsppLastL { 
	public static void main(String[] args) {
		String inputFileName = args[0], outputFileName = args[1];
		Map <String, Pair> words = new LinkedHashMap<>();

		inpRead(inputFileName, words);
		outWrite(outputFileName, words);
	}

	public static void inpRead(String fileName, Map <String, Pair> words) {
		try {
			MyScanner in = new MyScanner(new File(fileName), "UTF8");
			try {
				int cnt = 0;
				while (in.hasNextLine()) {
					String line = in.nextLine();
					parseLine(cnt, line, words);	
					cnt++;
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

	public static boolean checkChar(char c) {
		return (Character.isLetter(c) || 
				Character.getType(c) == Character.DASH_PUNCTUATION || 
				c == '\'');
	}

	public static BufferedWriter openOutFile(String name) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(name), 
			"UTF8"
		));

		return writer;
	}

	public static void outWrite(String fileName, Map <String, Pair> words) {
		try {
			BufferedWriter out = openOutFile(fileName);
			try {
				for(String i: words.keySet()) {  
					print(out, i, words);
				}
			} finally {
				out.close();
			}
		} catch (IOException e) {
			System.out.println("Error: Out " + e.getMessage());
		}
	}

	public static void print(BufferedWriter out, String curr, Map <String, Pair> words) throws IOException {
		Pair arr = words.get(curr);
		out.write(curr + " " + arr.positions.size() + " ");
		
		for (int i = 1; i <= arr.numOfLines.size(); i++) {
			if (i == arr.numOfLines.size()) {
				out.write(String.valueOf(arr.positions.get(i - 1)));
			} else if (arr.numOfLines.get(i - 1) != arr.numOfLines.get(i)) {
				out.write(String.valueOf(arr.positions.get(i - 1)) + " ");
			}
		}
		out.newLine();
	}

	public static void parseLine(int lineNum, String line, Map <String, Pair> words) { // parse Line
		int wordBeg = 0, wordEnd = 0, cnt = 0;
		while (wordEnd <= line.length()) {
			if ((wordEnd == line.length() || !checkChar(line.charAt(wordEnd))) && wordBeg < wordEnd) {
				String tmpStr = line.substring(wordBeg, wordEnd);
				tmpStr = tmpStr.toLowerCase();

				if (!words.containsKey(tmpStr)) {
					words.put(tmpStr, new Pair());
				}

				(words.get(tmpStr)).numOfLines.add(lineNum);
				(words.get(tmpStr)).positions.add(cnt + 1);

				wordBeg = wordEnd;
				cnt++;
			} else if (wordBeg < line.length() && !checkChar(line.charAt(wordBeg))) {
				wordBeg++;
				wordEnd = wordBeg;
			} else {
				wordEnd++;
			}
		}
	}
}	