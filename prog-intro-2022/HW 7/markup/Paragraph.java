package markup;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements MarkupObject{
    public List<PrimitiveMarkupObject> collection;

    public Paragraph(List <PrimitiveMarkupObject> collection) {
        this.collection = new ArrayList<>(collection);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        for (PrimitiveMarkupObject i : collection) {
            i.toMarkdown(str);
        }
    }
}
