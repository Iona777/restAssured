package pojo;

import java.util.List;

//Uses POJO (Plain Old Java Object) to define data
//You then use the Getter and Setter methods below for accessing the data
public class AddPlaceNoSetter
{
    //These are the data elements that make up an AddPlace object
    //Getters and setters apparently cause problems with new Gson().fromJson(), so have been removed
    //Since not Getters, have to make variables public so can be deserialized by jackson-databind
    public LocationNoSetter location; //Location is not a single string or int/double element, do define it as a POJO class.
    public int accuracy;
    public String name;
    public String phone_number;
    public String address;
    public List<String> types;
    public String website;
    public String language;
}
