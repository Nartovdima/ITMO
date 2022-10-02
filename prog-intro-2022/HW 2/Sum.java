public class Sum { 
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 0; i < args.length; i++) {
            int charBegin = 0, charEnd = 0;
            args[i] += " ";
            while (charEnd != args[i].length()) {
                if (Character.isWhitespace(args[i].charAt(charEnd)) && charBegin != charEnd) {
                    sum += Integer.parseInt(args[i].substring(charBegin, charEnd));
                    charBegin = charEnd;
                } else if (Character.isWhitespace(args[i].charAt(charBegin))) {
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