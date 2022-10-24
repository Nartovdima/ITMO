package markup;

import java.util.List;

public class Strikeout extends MarkupCollection {
    public Strikeout(List<PrimitiveMarkupObject> collection) {
        super(collection);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        str.append("~");
        super.toMarkdown(str);
        str.append("~");
    }

    @Override
    public void toHtml(StringBuilder str) {
        str.append("<s>");
        super.toHtml(str);
        str.append("</s>");
    }
}
