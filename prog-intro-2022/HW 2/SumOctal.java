public class SumOctal { 
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 0; i < args.length; i++) {
            int charBegin = 0, charEnd = 0;
            while (charEnd <= args[i].length()) {
                if ((charEnd == args[i].length() || (Character.isWhitespace(args[i].charAt(charEnd)))) && charBegin < charEnd) {
                    if (args[i].charAt(charEnd - 1) == 'O' || args[i].charAt(charEnd - 1) == 'o' && charEnd - 1 > charBegin) {
                        sum += Integer.parseInt(args[i].substring(charBegin, charEnd - 1), 8);
                    } else {
                        sum += Integer.parseInt(args[i].substring(charBegin, charEnd), 10);
                    }
                    charBegin = charEnd;
                } else if (charBegin < args[i].length() && Character.isWhitespace(args[i].charAt(charBegin))) {
                    charBegin++;
                    charEnd = charBegin;
                } else {
                    charEnd++;
                }
            }
        }
        System.out.println(sum);
    }
}