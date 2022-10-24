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
}
