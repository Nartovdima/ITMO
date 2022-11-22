package md2html.markup;

import java.util.List;

public class Strong extends MarkupObject {
    public Strong(List<MarkupCollection> children) {
        super(children);
    }
    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "strong");
    }
}
