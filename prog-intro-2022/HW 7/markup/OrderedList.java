package markup;

import java.util.List;

public class OrderedList extends ListObject {

    public OrderedList(List<ListItem> collection) {
        super(collection);
    }

    @Override
    public void toHtml(StringBuilder str){
        str.append("<ol>");
        super.toHtml(str);
        str.append("</ol>");
    }
}
