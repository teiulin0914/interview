package tw.com.teiulin.pricecalc;

public class User {

    private String id;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Order prepareOrder(String time) {
        return new Order(getId(), time);
    }
}
