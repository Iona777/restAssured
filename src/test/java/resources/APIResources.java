package resources;

//enum is a special class in java which has a collectoin of constants or methods
public enum APIResources
{
    //this is invoked using valueOf()
    //E.G> APUResources.valueOf(resource);
    //If resource = addPlaceAPI, then it will pass the value of addPlaceAPI to the constructor.
    //Then the constructor sets the class variable 'resource' to the same value.
    //Finally, that resource value is returned by the getResource() method
    //E.G.
    //If resource is addPlaceAPI then APIResource.valueOf(resource) = "/maps/api/place/add/json"
    //This is passed to the APIResources() constructor
    //private String resource is set to this value
    //getResource() then returns value "/maps/api/place/add/json"

    //The syntax for the enum method declaration is to separate them with commas and then have
    //semi-colon at the end of the method declarations.

    //This is a method that will return the string value "/maps/api/place/add/json"
    addPlaceAPI("/maps/api/place/add/json"),
    //This is a method that will return the string value "/maps/api/place/get/json"
    getPlaceAPI("/maps/api/place/get/json"),
    //This is a method that will return the string value "/maps/api/place/delete/json"
    deletePlaceAPI("/maps/api/place/delete/json");

    //This is the class variable that gets returned by getResource() method
    private String resouce;

    APIResources(String resouce)
    {
        //Sets the class level resource variable to the resouce parameter passed in.
        this.resouce = resouce;
    }

    public String getResouce()
    {
        //Seems that the constructed resouce cannot be returned directly as its scope is local to the constructor,
        //so you need ot assign it to another variable at class level and return that variable instead.
        return resouce;
    }



}
