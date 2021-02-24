package sample.models;

public class CategoryModel {

    private final int id;
    private final int index;
    private final String category;

    public CategoryModel(int Index,int Id, String category){
        this.index = Index;
        this.id = Id;
        this.category=category;
    }

    public int getIndex() {
        return id;
    }

    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }

}



