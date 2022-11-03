package markup;

import java.util.ArrayList;
import java.util.List;

public abstract class MarkupCollection implements PrimitiveMarkupObject{
    protected final List<PrimitiveMarkupObject> children;

    protected MarkupCollection(List<PrimitiveMarkupObject> children) {
        this.children = new ArrayList<>(children);
    }

    public void toMarkdown(StringBuilder str, String MarkupSyntaxSymbols) {
        str.append(MarkupSyntaxSymbols);
        for (PrimitiveMarkupObject child : children) {
            child.toMarkdown(str);
        }
        str.append(MarkupSyntaxSymbols);
    }

    public void toHtml(StringBuilder str, String MarkupSyntaxSymbols) {
        str.append("<").append(MarkupSyntaxSymbols).append(">");
        for (PrimitiveMarkupObject child : children) {
            child.toHtml(str);
        }
        str.append("</").append(MarkupSyntaxSymbols).append(">");
    }
}
