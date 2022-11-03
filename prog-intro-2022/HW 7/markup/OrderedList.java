package markup;

import java.util.List;

public class OrderedList extends ListObject {

    public OrderedList(List<ListItem> children) {
        super(children);
    }

    @Override
    public void toHtml(StringBuilder str){
        super.toHtml(str, "ol");
    }
}
