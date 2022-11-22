package md2html.markup;

import java.util.List;

public class Strikeout extends MarkupObject {
    public Strikeout(List<MarkupCollection> children) {
        super(children);
    }
    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "s");
    }
}
