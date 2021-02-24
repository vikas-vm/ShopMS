package sample.models;

public class VendorOrderModel {

    private final int id;
    private final int index;
    private final String title;
    private final int inStock;

    public VendorOrderModel(int index,int Id, String Title, int inStock){
        this.id = Id;
        this.index = index;
        this.title=Title;
        this.inStock=inStock;
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
    public String getInStock(){
        if (inStock==0){
            return "In Order";
        }
        else {
            return "In Stock";
        }
    }

}



