import java.util.*;
import java.io.*;

public class Wspp { 
	public static void main(String[] args) {
		String inputFileName = args[0], outputFileName = args[1];
		Map <String, IntList> words = new LinkedHashMap<>();
		inpRead(inputFileName, words);
		outWrite(outputFileName, words);
	}

	public static void inpRead(String fileName, Map <String, IntList> words) {
		try {
			MyScanner in = new MyScanner(new File(fileName), "UTF8");
			try {
				int cnt = 0;
				while (in.hasNext()) {
					String lowCaseStr = in.next();
					int wordBeg = 0, wordEnd = 0;

					while (wordEnd <= lowCaseStr.length()) {
						if ((wordEnd == lowCaseStr.length() || (!checkChar(lowCaseStr.charAt(wordEnd)))) && wordEnd > wordBeg) {
							String tmpStr = lowCaseStr.substring(wordBeg, wordEnd);
							tmpStr = tmpStr.toLowerCase();

							if (!words.containsKey(tmpStr)) {
								words.put(tmpStr, new IntList());
							}
							(words.get(tmpStr)).add(cnt + 1);
							cnt++;

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

	public static void outWrite(String fileName, Map <String, IntList> words) {
		try {
			BufferedWriter out = openOutFile(fileName);
			try {
				for(Map.Entry i: words.entrySet()) {  

					out.write(i.getKey() + " " + words.get(i.getKey()).size() + " " + i.getValue());  
					out.newLine();
				}
			} finally {
				out.close();
			}
		} catch (IOException e) {
			System.out.println("Error: Out " + e.getMessage());
		}
	}
}