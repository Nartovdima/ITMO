package markup;

import java.util.List;

public class ListItem implements HtmlObject{
    private final List<ListElement> collection;

    public ListItem(List<ListElement> collection) {
        this.collection = collection;
    }

    @Override
    public void toHtml(StringBuilder str) {
        for (ListElement i : collection) {
            i.toHtml(str);
        }
    }
}
