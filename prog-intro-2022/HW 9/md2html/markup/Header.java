package md2html.markup;

import java.util.List;

public class Header implements MarkupElement {
    private final int level;
    private final List<MarkupCollection> children;

    public Header(List<MarkupCollection> children, int level) {
        this.level = level;
        this.children = children;
    }
    @Override
    public void toHtml(StringBuilder str) {
        str.append("<h").append(level).append(">");
        for (MarkupCollection child : children) {
            child.toHtml(str);
        }
        str.append("</h").append(level).append(">");
    }
}
