package markup;

import java.util.List;

public class Strong extends MarkupCollection {
    public Strong(List<PrimitiveMarkupObject> children) {
        super(children);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkdown(str, "__");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "strong");
    }
}
