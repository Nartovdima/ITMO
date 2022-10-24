package markup;

public interface MarkupObject extends HtmlObject {
    void toMarkdown(StringBuilder str);
}
