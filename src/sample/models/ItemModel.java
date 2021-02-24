package sample.models;

import java.util.Objects;

public class ItemModel {

    private final int id;
    private final int index;
    private final String title;
    private final String stock;
    private final String price;
    private final String mrp;
    private final int itemType;
    private final String cat;
    private final String initial;

    public ItemModel(int index,int Id, String title, String stock, String price, String mrp, int itemType, String cat, String initial){
        this.id = Id;
        this.index = index;
        this.title=title;
        this.stock=stock;
        this.price=price;
        this.mrp=mrp;
        this.itemType=itemType;
        this.cat=cat;
        this.initial=initial;
    }

    public int getIndex() {
        return index;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getStock() { return stock; }
    public String getInitial() { return Objects.requireNonNullElse(initial, "null"); }
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



