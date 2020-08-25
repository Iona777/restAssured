package resources;

import pojo.AddPlace;
import pojo.Location;
import java.util.ArrayList;
import java.util.List;

//This class contains methods that are used to return test data, in this case payloads
public class TestDataBuild
{

    //Accepts input parametgers to set values in the addPlace object, then returns that object
    public AddPlace addPlacePayload(String name, String language, String address)
    {
        AddPlace place = new AddPlace(); //this is a PJO class to define data
        List<String> myList = new ArrayList<>();
        myList.add("car park");
        myList.add("shop");

        Location loc = new Location(); //This si a POJO class to define data
        loc.setLat(-38.383494);
        loc.setLng(33.427362);
        place.setLocation(loc);
        place.setAccuracy(50);
        place.setName(name); //This is set from the input parameter rather than hard codiing.
        place.setPhone_number("0113 123546");
        place.setAddress(address); //This is set from the input parameter rather than hard codiing.
        place.setTypes(myList);
        place.setWebsite("http://google.com");
        place.setLanguage(language); //This is set from the input parameter rather than hard codiing.

        return  place;
    }

    public String deletePlacePayload(String placeId)
    {
        return "{\\r\\n \\\"place_id\\\": \\\"\"+placeId+\"\\\" \\r\\n}";
    }
    
}
