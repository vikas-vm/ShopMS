package sample.models;

public class ThemesModel {

    private final int id;
    private final String title;

    public ThemesModel(int Id, String Title){
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



