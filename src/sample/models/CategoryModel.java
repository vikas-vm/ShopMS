package sample.models;

public class CategoryModel {

    private final int id;
    private final String category;

    public CategoryModel(int Id, String category){
        this.id = Id;
        this.category=category;
    }

    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }

}



