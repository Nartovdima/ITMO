package md2html.markup;

public class Text implements MarkupCollection {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public void toHtml(StringBuilder str) {
        str.append(text);
    }
}
