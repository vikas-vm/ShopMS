package sample.models;

public class VendorOrderModel {

    private final int id;
    private final String title;
    private int inStock;

    public VendorOrderModel(int Id, String Title, int inStock){
        this.id = Id;
        this.title=Title;
        this.inStock=inStock;
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
    public void setInStock(){
        this.inStock=0;
    }

}



