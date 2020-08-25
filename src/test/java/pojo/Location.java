package pojo;

//Uses POJO (Plain Old Java Object) to define data
//You then use the Getter and Setter methods below for accessing the data
public class Location //Location is not a single string or int/double element, do define it as a POJO class.
{
    //These are the data elements that make up a Location object
    double lat;
    double lng;

    //Getters
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    //Setters
    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
