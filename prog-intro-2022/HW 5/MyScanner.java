import java.io.*;
import java.util.*;

public class MyScanner {
	private BufferedReader in;
	private char[] buffer;
	private int brInsertPos;
	private int brEnd;
	private static final int brBufferSize = 1024;
	private boolean close;
	private String currToken, currLine;

	public MyScanner(InputStream source) {
		this(new BufferedReader(
			new InputStreamReader(
				source
			), 
			brBufferSize
		));
	}

	public MyScanner(InputStream source, String encoding) throws UnsupportedEncodingException {
		this(new BufferedReader(
			new InputStreamReader(
				source, 
				encoding
			), 
			brBufferSize
		));
	}

	public MyScanner(File source) 
		throws FileNotFoundException, 
			   SecurityException 
	{
		this(new BufferedReader(
			new InputStreamReader(
				new FileInputStream(source)
			), 
			brBufferSize
		));
	}

	public MyScanner(File source, String encoding) 
		throws FileNotFoundException, 
			   SecurityException, 
			   UnsupportedEncodingException 
	{
		this(new BufferedReader(
			new InputStreamReader(
				new FileInputStream(source), 
				encoding
			), 
			brBufferSize
		)); 
	}

	public MyScanner(String source) {
		this(new BufferedReader(
			new StringReader(source), 
			brBufferSize
		));
	}

	private MyScanner(BufferedReader source) {
		in = source;
		buffer = new char[1];
		brInsertPos = brEnd = 0;
		close = false;
		currToken = currLine = null;
	}

	public void close() throws IOException {
		in.close();
		close = true;
	}

	private void isClosed() {
		if (close)
			throw new IllegalStateException();
	} 

	private String getToken() {
		if (brInsertPos == brEnd) {
			fillBuffer();
		}
		if (brEnd < brInsertPos) {
			currToken = null;
			return null;
		}

		int tokenBeg = brInsertPos, tokenEnd = brInsertPos;

		while (true) {
			if (tokenEnd == brEnd) {
				brInsertPos = brEnd;
				fillBuffer();
			}
			if ((Character.isWhitespace(buffer[tokenEnd]) || brEnd < brInsertPos) && tokenBeg < tokenEnd) {
				currToken = String.valueOf(buffer, tokenBeg, tokenEnd - tokenBeg);
				brInsertPos = tokenEnd;
				return currToken;
			} else if (Character.isWhitespace(buffer[tokenBeg])) {
				tokenBeg++;
				tokenEnd = tokenBeg;
			} else {
				tokenEnd++;
			}
		}
	}			

	private String getLine() {
		if (brInsertPos == brEnd) {
			fillBuffer();
		}
		if (brEnd < brInsertPos) {
			currToken = null;
			return null;
		}

		int lineEnd = brInsertPos, lineBeg = brInsertPos;
		while (true) {
			if (lineEnd == brEnd) {
				brInsertPos = lineEnd;
				fillBuffer();
			}
			if (isLineSeparator(buffer[lineEnd]) || brEnd < brInsertPos) 
				break;	
			lineEnd++;
		}

		if (lineEnd + 1 == brEnd) {
			brInsertPos = lineEnd + 1;
			fillBuffer();
		}
		int sbstr = 0;
		if (brEnd >= brInsertPos && buffer[lineEnd] == '\r' && buffer[lineEnd + 1] == '\n') {
			lineEnd++;
			sbstr++;
		}
		currLine = String.valueOf(buffer, lineBeg, lineEnd - sbstr - lineBeg);
		brInsertPos = lineEnd + 1;
		return currLine;
	}

	public boolean hasNext() {
		isClosed();
		return (currToken != null || getToken() != null);
	}

	public String next() {
		isClosed();
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		String tmp = currToken;
		currToken = null;
		return tmp;
	}

	public boolean hasNextInt() {
		isClosed();

		if (currToken != null) {
			return isInt(currToken);
		}
		if (getToken() == null) {
			return false;
		}
		return isInt(currToken);
	}

	public int nextInt() {
		isClosed();
		if (!hasNextInt() && currToken != null) {
			throw new InputMismatchException();
		} else if (!hasNextInt()) {
			throw new NoSuchElementException();
		}

		String tmp = currToken;
		currToken = null;
		if (tmp.charAt(tmp.length() - 1) == 'o')
			return (Integer.parseInt(tmp.substring(0, tmp.length() - 1), 8));
		else
			return (Integer.parseInt(tmp, 10));
	}

	public boolean hasNextLine() {
		isClosed();
		return (currLine != null || getLine() != null);
	}

	public String nextLine() {
		if (!hasNextLine()) {
			throw new NoSuchElementException();
		}

		String tmp = currLine;
		currLine = null;
		return tmp;
	}

	private boolean isInt(String s) {

		int radix = 10, n = s.length();
		if (s.charAt(n - 1) == 'o') {
			radix = 8;
			n--;
		}
		
		for (int i = 0; i < n; i++) {
			System.err.println(i + " " + s.charAt(i) + " " + (isDigit(s.charAt(i), radix)));
			if (!(i == 0 && s.charAt(i) == '-') && (Character.forDigit(s.charAt(i), radix) == '\u0000'))
				return false;
		}
		return true;
	}

	private boolean isDigit(char d, int radix) {
		System.err.println(d + " " + radix);
		System.err.println((Character.forDigit(d, radix) == '\u0000'));

		return (Character.forDigit(d, radix) != '\u0000');
	}

	private boolean isLineSeparator(char ch) {
		return (ch == '\n' || ch == '\r' || ch == '\u2028' || ch == '\u2029' || ch == '\u0085');
	}

	public int fillBuffer() {
		if (brInsertPos == buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length * 2);
		}
		try	{
			int tmp = in.read(buffer, brInsertPos, Math.min(brBufferSize, buffer.length - brInsertPos));
			brEnd = brInsertPos + tmp;
			return tmp; 
		} catch (IOException e) {
			close = true;
			return -1;
		}
	}

	public static void main(String[] args) {
		String s = "123 456o";
		MyScanner a = new MyScanner(s);
		System.out.println("1121" + (Character.forDigit(1, 10) != '\u0000'));
		while (a.hasNextInt()) {
			System.out.println(a.nextInt());
		}
	}
}