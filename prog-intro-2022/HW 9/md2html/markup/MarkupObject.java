package md2html.markup;

import java.util.List;

public abstract class MarkupObject implements MarkupCollection {
    private final List<MarkupCollection> children;

    public MarkupObject(List<MarkupCollection> children) {
        this.children = children;
    }

    protected void toHtml(StringBuilder str, String markupSymbol) {
        str.append("<").append(markupSymbol).append(">");
        for (MarkupCollection child : children) {
            child.toHtml(str);
        }
        str.append("</").append(markupSymbol).append(">");
    }
}
