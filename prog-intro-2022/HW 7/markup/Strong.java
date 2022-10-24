package markup;

import java.util.List;

public class Strong extends MarkupCollection {
    public Strong(List<PrimitiveMarkupObject> collection) {
        super(collection);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        str.append("__");
        super.toMarkdown(str);
        str.append("__");
    }

    @Override
    public void toHtml(StringBuilder str) {
        str.append("<strong>");
        super.toHtml(str);
        str.append("</strong>");
    }
}
