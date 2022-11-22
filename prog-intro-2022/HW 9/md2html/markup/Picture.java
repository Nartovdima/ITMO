package md2html.markup;

public class Picture implements MarkupCollection {
    private String name, link;

    public Picture(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public void toHtml(StringBuilder str) {
        str.append("<img alt='").append(name).append("' src='").append(link).append("'>");
    }

}
