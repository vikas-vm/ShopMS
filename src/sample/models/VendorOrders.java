package sample.models;

public class VendorOrders {

    private int id;
    private String title;

    public VendorOrders(int Id, String Title){
        this.id = Id;
        this.title=Title;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

}



