package sample.models;

public class CartsModel {

    private final int id;
    private final int index;
    private final String title;
    private final float stock;
    private final float mrp;
    private final int itemType;
    private final String cat;
    private final int item_id;
    String stockStr;

    public CartsModel(int Index,int Id, int item_id, String title, float stock, float mrp, int itemType, String cat) {
        this.id = Id;
        this.index = Index;
        this.item_id=item_id;
        this.title = title;
        this.stock = stock;
        this.mrp = mrp;
        this.itemType = itemType;
        this.cat = cat;
    }

    public int getId() {
        return id;
    }
    public int getIndex() {
        return index;
    }
    public int getItem_id() {
        return item_id;
    }

    public String getTitle() {
        return title + " ("+cat+")";
    }

    public String getStock() {
        if (itemType == 0) {
            if(stock>1){
                stockStr = stock + "kgs";
            }
            else {
                stockStr = stock + "kg";
            }
        } else {
            if(stock>1){
                stockStr = stock + "pc";
            }
            else {
                stockStr = stock + "pcs";
            }
        }
        return stockStr;
    }
    public float getPayable(){
        return mrp*stock;
    }

    public String getMrp() {
        return "₹ "+ mrp;
    }
}