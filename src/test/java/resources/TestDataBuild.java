package resources;

import pojo.AddPlace;
import pojo.Location;
import java.util.ArrayList;
import java.util.HashMap;
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

    public HashMap<String, Object> addPlacePayloadHashMap (String name, String language, String address)
    {
        HashMap<String, Object> place = new HashMap<>(); //this is a Hashmap to define data
        HashMap<String, Object> loc = new HashMap<>(); //this is a Hashmap to define data
        HashMap<String, Object> types = new HashMap<>(); //this is a Hashmap to define data

        List<String> myList = new ArrayList<>();
        myList.add("car park");
        myList.add("shop");

        //Using loc hashmap
        loc.put("Lat", "-38.383494");
        loc.put("Lng", "33.427362)");

        //Using place hashmap
        place.put("Location", loc); //Sets the location object to the loc Hashmap
        place.put("Accuracy","50");
        place.put("Name", name); //This is set from the input parameter
        place.put("Phone_Number", "0113 123546" );
        place.put("Address", address); //This is set from the input parameter



        //Very difficult to use hashamp with list.
        //place.setTypes(myList);

        place.put("Website","http://google.com");
        place.put("Language",language); //This is set from the input parameter

        return  place;
    }

    public HashMap<String, Object> deletePlacePayload(String placeId)
    {

        //Using Hashmap, return HashMap<String, Object> if using this
        HashMap<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("place_id", placeId);

        return jsonAsMap;


        //Using raw Json, return a String if using this
        //return "{\r\n \"place_id\": \""+placeId+"\" \r\n}";
    }
    
}
