package sample.models;

public class OrderModel {

    private final int id;
    private final int index;
    private final String name;
    private final float total;
    private final float paid;
    public OrderModel(int index,int Id, String name,float total, float paid ){
        this.id = Id;
        this.index = index;
        this.name=name;
        this.total=total;
        this.paid=paid;
    }

    public int getIndex() {
        return index;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getTotal(){ return "₹ "+total; }
    public String getPaid(){ return "₹ "+paid; }

}




