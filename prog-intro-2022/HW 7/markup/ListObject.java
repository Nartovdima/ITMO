package markup;

import java.util.ArrayList;
import java.util.List;

public abstract class ListObject implements ListElement{
    protected final List<ListItem> collection;

    protected ListObject(List <ListItem> collection) {
        this.collection = new ArrayList<>(collection);
    }
    @Override
    public void toHtml(StringBuilder str) {
        for (ListItem i : collection) {
            i.toHtml(str);
        }
    }
}
