package md2html;

import md2html.markup.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Md2Html {
    public static String text;
    public static List<String> tokens = new ArrayList<>();

    public static void main(String[] args) {
        String inputFileName = args[0], outputFileName = args[1];
        try {
            text = Files.readString(Paths.get(inputFileName));
        } catch (IOException e) {
            System.out.println("Unable to reach file: " + e.getMessage());
        }

        text = htmlObjectsShielding(text);
        tokens = Separator.split(text);

        List<MarkupElement> finalText = parse();
        outWrite(outputFileName, finalText);
    }

    public static String htmlObjectsShielding(String text) {
        StringBuilder tmpStr = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '<') {
                tmpStr.append("&lt;");
            } else if (text.charAt(i) == '>') {
                tmpStr.append("&gt;");
            } else if (text.charAt(i) == '&') {
                tmpStr.append("&amp;");
            } else {
                tmpStr.append(text.charAt(i));
            }
        }
        return tmpStr.toString();
    }
    public static List<MarkupElement> parse() {
        List<MarkupElement> ans = new ArrayList<>();
        for (String i : tokens) {
            ans.add(parseLine(i, 0, i.length()));
        }
        return ans;
    }

    public static MarkupElement parseLine(String text, int stPos, int endPos) {
        int currPos = 0;
        while (currPos < endPos && text.charAt(currPos) == '#') {
            currPos++;
        }
        if (
                currPos < endPos &&
                0 < currPos - stPos &&
                currPos - stPos <= 6 &&
                Character.isWhitespace(text.charAt(currPos))
        ) {
            return new Header(parseObject(text, currPos + 1, endPos), currPos - stPos);
        }
        return new Paragraph(parseObject(text, stPos, endPos));
    }

    public static int  isRightDelimiterExist(String text, int stPos, int endPos, String rightDelimiter) {
        for (int i = stPos; i < endPos - rightDelimiter.length() + 1; i++) {
            if (text.startsWith(rightDelimiter.repeat(2), i) || text.charAt(i) == '\\') {
                i += 1;
            }
            else if (text.startsWith(rightDelimiter, i)) {
                return i;
            }
        }
        return -1;
    }

    public static void makePlainText(String text, int stPos, int endPos, List<MarkupCollection> tmp) {
        if (endPos - stPos > 0) {
            tmp.add(new Text(text.substring(stPos, endPos)));
        }
    }

    public static List<MarkupCollection> parseObject(String text, int stPos, int endPos) {
        List<MarkupCollection> tmp = new ArrayList<>();
        int curPos = stPos, tmpEndPos = stPos;
        while (curPos < endPos) {
            if (text.charAt(curPos) == '\\') {
                makePlainText(text, tmpEndPos, curPos, tmp);
                makePlainText(text, curPos + 1, curPos + 2, tmp);
                tmpEndPos = curPos + 2;
                curPos += 2;
            } else if (
                    text.startsWith("__", curPos) &&
                    isRightDelimiterExist(text, curPos + 2, endPos, "__") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 2, endPos, "__");
                tmp.add(new Strong(parseObject(text, curPos + 2, tmpEndPos)));
                tmpEndPos += 2;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("**", curPos) &&
                    isRightDelimiterExist(text, curPos + 2, endPos, "**") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 2, endPos, "**");
                tmp.add(new Strong(parseObject(text, curPos + 2, tmpEndPos)));
                tmpEndPos += 2;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("_", curPos) &&
                    isRightDelimiterExist(text, curPos + 1, endPos, "_") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 1, endPos, "_");
                tmp.add(new Emphasis(parseObject(text, curPos + 1, tmpEndPos)));
                tmpEndPos += 1;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("*", curPos) &&
                    isRightDelimiterExist(text, curPos + 1, endPos, "*") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 1, endPos, "*");
                tmp.add(new Emphasis(parseObject(text, curPos + 1, tmpEndPos)));
                tmpEndPos += 1;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("--", curPos) &&
                    isRightDelimiterExist(text, curPos + 2, endPos, "--") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 2, endPos, "--");
                tmp.add(new Strikeout(parseObject(text, curPos + 2, tmpEndPos)));
                tmpEndPos += 2;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("`", curPos) &&
                    isRightDelimiterExist(text, curPos + 1, endPos, "`") != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmpEndPos = isRightDelimiterExist(text, curPos + 1, endPos, "`");
                tmp.add(new Code(parseObject(text, curPos + 1, tmpEndPos)));
                tmpEndPos += 1;
                curPos = tmpEndPos;
            } else if (
                    text.startsWith("![", curPos) &&
                    isRightDelimiterExist(text, curPos + 2, endPos, "](") != -1 &&
                    isRightDelimiterExist(
                            text,
                            isRightDelimiterExist(text, curPos + 2, endPos, "](") + 2,
                            endPos, ")"
                    ) != -1
            ) {
                makePlainText(text, tmpEndPos, curPos, tmp);
                tmp.add(new Picture(
                        text.substring(curPos + 2, isRightDelimiterExist(text, curPos + 2, endPos, "](")),
                        text.substring(isRightDelimiterExist(text, curPos + 2, endPos, "](") + 2, isRightDelimiterExist(text, isRightDelimiterExist(text, curPos, endPos, "]("), endPos, ")"))
                ));
                tmpEndPos = isRightDelimiterExist(
                        text,
                        isRightDelimiterExist(text, curPos + 2, endPos, "](") + 2,
                        endPos, ")"
                );
                tmpEndPos += 1;
                curPos = tmpEndPos;

            } else {
                curPos++;
            }
        }
        makePlainText(text, tmpEndPos, endPos, tmp);
        return tmp;
    }

    public static BufferedWriter openOutFile(String name) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(name),
                StandardCharsets.UTF_8
        ));
    }

    public static void outWrite(String fileName, List<MarkupElement> finalText) {
        try {
            try (BufferedWriter out = openOutFile(fileName)) {
                for (MarkupElement i : finalText) {
                    StringBuilder tmpStr = new StringBuilder();
                    i.toHtml(tmpStr);
                    out.write(tmpStr.toString());
                    out.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to reach file: " + e.getMessage());
        }
    }
}