package markup;

import java.util.ArrayList;
import java.util.List;

public abstract class ListObject implements ListElement{
    protected final List<ListItem> children;

    protected ListObject(List<ListItem> children) {
        this.children = new ArrayList<>(children);
    }

    public void toHtml(StringBuilder str, String MarkupSyntaxSymbols) {
        str.append("<").append(MarkupSyntaxSymbols).append(">");
        for (ListItem child : children) {
            child.toHtml(str);
        }
        str.append("</").append(MarkupSyntaxSymbols).append(">");
    }
}
