package sample.models;

public class ItemModel {

    private final int id;
    private final String title;
    private final String stock;
    private final String price;
    private final String mrp;
    private final int itemType;
    private final String cat;

    public ItemModel(int Id, String title, String stock, String price, String mrp, int itemType, String cat){
        this.id = Id;
        this.title=title;
        this.stock=stock;
        this.price=price;
        this.mrp=mrp;
        this.itemType=itemType;
        this.cat=cat;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getStock() { return stock; }
    public String getPrice() { return price; }
    public String getMrp() { return mrp; }
    public String getItemType(){
        if(itemType==0){
            return "By Quantity";
        }
        else {
            return "By Unit";
        }
    }
    public String getCategory(){
        return cat;
    }

}



