package pojo;

import java.util.List;

//Uses POJO (Plain Old Java Object) to define data
//You then use the Getter and Setter methods below for accessing the data
public class AddPlace
{
    //These are the data elements that make up an AddPlace object
    private Location location; //Location is not a single string or int/double element, do define it as a POJO class.
    private int accuracy;
    private String name;
    private String phone_number;
    private String address;
    private List<String> types;
    private String website;
    private String language;

    //Getters
    public Location getLocation() {
        return location;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getWebsite() {
        return website;
    }

    public String getLanguage() {
        return language;
    }


    //Setters
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
