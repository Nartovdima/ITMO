package markup;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements MarkupObject, ListElement {
    private final List<PrimitiveMarkupObject> children;

    public Paragraph(List<PrimitiveMarkupObject> children) {
        this.children = new ArrayList<>(children);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        for (PrimitiveMarkupObject child : children) {
            child.toMarkdown(str);
        }
    }

    @Override
    public void toHtml(StringBuilder str) {
        for (PrimitiveMarkupObject child : children) {
            child.toHtml(str);
        }
    }
}
