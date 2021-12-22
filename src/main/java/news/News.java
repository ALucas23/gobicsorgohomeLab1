package news;

public class News {
    private final String agency;
    private final String text;

    public String getAgency() {
        return agency;
    }

    public String getText() {
        return text;
    }

    public News(String agency, String text) {
        this.agency = agency;
        this.text = text;
    }
}
