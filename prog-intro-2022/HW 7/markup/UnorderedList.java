package markup;

import java.util.List;

public class UnorderedList extends ListObject {

    public UnorderedList(List<ListItem> collection) {
        super(collection);
    }

    @Override
    public void toHtml(StringBuilder str){
        str.append("<ul>");
        super.toHtml(str);
        str.append("</ul>");
    }
}
