package markup;

import java.util.List;

public class Strikeout extends MarkupCollection {
    public Strikeout(List<PrimitiveMarkupObject> children) {
        super(children);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkdown(str, "~");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "s");
    }
}
