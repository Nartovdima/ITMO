package markup;

import java.util.List;

public class Emphasis extends MarkupCollection {
    public Emphasis(List<PrimitiveMarkupObject> children) {
        super(children);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        //str.append("*");
        super.toMarkdown(str, "*");
        //str.append("*");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "em");
    }
}
