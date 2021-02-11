package sample.models;

public class ItemModel {

    private final int id;
    private final String title;
    private final float stock;
    private final float price;
    private final float mrp;

    public ItemModel(int Id, String title, float stock, float price, float mrp){
        this.id = Id;
        this.title=title;
        this.stock=stock;
        this.price=price;
        this.mrp=mrp;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public float getStock() { return stock; }
    public float getPrice() { return price; }
    public float getMrp() { return mrp; }

}



