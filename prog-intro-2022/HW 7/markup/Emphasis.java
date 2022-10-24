package markup;

import java.util.List;

public class Emphasis extends MarkupCollection {
    public Emphasis(List<PrimitiveMarkupObject> collection) {
        super(collection);
    }

    @Override
    public void toMarkdown (StringBuilder str) {
        str.append("*");
        super.toMarkdown(str);
        str.append("*");
    }
}
