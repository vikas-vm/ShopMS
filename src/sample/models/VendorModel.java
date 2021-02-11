package sample.models;

public class VendorModel {

    private final int id;
    private final String title;
    private final String contact;
    private final String email;
    private final String city;
    private final String address;

    public VendorModel(int Id, String Title, String Contact, String Email, String City, String Address){
        this.id = Id;
        this.title=Title;
        this.contact=Contact;
        this.email=Email;
        this.city=City;
        this.address=Address;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getContact() {
        return contact;
    }
    public String getEmail() {
        return email;
    }
    public String getCity() {
        return city;
    }
    public String getAddress() {
        return address;
    }

}



