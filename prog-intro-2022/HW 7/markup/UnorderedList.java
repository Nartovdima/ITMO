package markup;

import java.util.List;

public class UnorderedList extends ListObject {

    public UnorderedList(List<ListItem> children) {
        super(children);
    }

    @Override
    public void toHtml(StringBuilder str){
        super.toHtml(str, "ul");
    }
}
