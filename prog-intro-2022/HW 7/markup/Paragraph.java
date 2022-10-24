package markup;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements MarkupObject, ListElement {
    public final List<PrimitiveMarkupObject> collection;

    public Paragraph(List <PrimitiveMarkupObject> collection) {
        this.collection = new ArrayList<>(collection);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        for (PrimitiveMarkupObject i : collection) {
            i.toMarkdown(str);
        }
    }

    @Override
    public void toHtml(StringBuilder str) {
        for (PrimitiveMarkupObject i : collection) {
            i.toHtml(str);
        }
    }
}
