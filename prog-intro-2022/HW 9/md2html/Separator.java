package md2html;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Separator {
    public static final List<String> lineSeparators = new ArrayList<>(List.of(
            "\r\n",
            "\n",
            "\r",
            "\u2028",
            "\u2029",
            "\u0085"
    ));

    public static  List<String> split(String text) {
        int currPos = 0, bucketSt = 0, bigBucketSt = 0;
        List<String> ans = new ArrayList<>();
        List<String> tokens = new ArrayList<>();

        while (currPos <= text.length()) {
            for (String separator : lineSeparators) {
                if (
                        currPos == text.length() ||
                        (currPos + separator.length() - 1 < text.length() && text.startsWith(separator, currPos))
                ) {
                    ans.add(text.substring(bucketSt, currPos));
                    currPos += separator.length() - 1;
                    bucketSt = currPos + 1;
                    break;
                }
            }
            currPos++;
        }

        StringBuilder tmpStr = new StringBuilder();
        for (String i : ans) {
            if (Objects.equals(i, "") && !tmpStr.isEmpty()) {
                tokens.add(tmpStr.deleteCharAt(tmpStr.length() - 1).toString());
                tmpStr.setLength(0);
            } else if (!i.isEmpty()) {
                tmpStr.append(i).append('\n');
            }
        }
        if (!tmpStr.isEmpty()) {
            tokens.add(tmpStr.deleteCharAt(tmpStr.length() - 1).toString());
        }

        return tokens;
    }
}
