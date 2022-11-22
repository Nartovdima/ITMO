package md2html.markup;

import java.util.List;

public class Paragraph implements MarkupElement {
    private final List<MarkupCollection> children;

    public Paragraph(List<MarkupCollection> children) {
        this.children = children;
    }

    @Override
    public void toHtml(StringBuilder str) {
        str.append("<p>");
        for (MarkupCollection child : children) {
            child.toHtml(str);
        }
        str.append("</p>");
    }
}
