package md2html.markup;

import java.util.List;

public class Code extends MarkupObject {
    public Code(List<MarkupCollection> children) {
        super(children);
    }
    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "code");
    }
}
