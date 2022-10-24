package markup;

import java.util.ArrayList;
import java.util.List;

public abstract class MarkupCollection implements PrimitiveMarkupObject{
    protected final List<PrimitiveMarkupObject> collection;

    protected MarkupCollection(List <PrimitiveMarkupObject> collection) {
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
