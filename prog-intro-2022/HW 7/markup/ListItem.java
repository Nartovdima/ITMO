package markup;

import java.util.List;

public class ListItem implements HtmlObject{
    private final List<ListElement> children;

    public ListItem(List<ListElement> children) {
        this.children = children;
    }

    @Override
    public void toHtml(StringBuilder str) {
        str.append("<li>");
        for (ListElement child : children) {
            child.toHtml(str);
        }
        str.append("</li>");
    }
}
