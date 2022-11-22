package md2html.markup;

import java.util.List;

public class Emphasis extends MarkupObject {
    public Emphasis(List<MarkupCollection> children) {
        super(children);
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "em");
    }
}
