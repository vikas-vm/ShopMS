package sample.models;

public class Vendors {

    private int id;
    private String title;
    private String contact;
    private String email;
    private String city;
    private String address;

    public Vendors(int Id, String Title, String Contact, String Email, String City, String Address){
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



