package stepDefinitions;


import io.cucumber.java.Before;
import java.io.IOException;

import static stepDefinitions.stepDefs.place_id;

public class Hooks
{
    @Before("@DeletePlace") //This only applies to the scenarios with this tag.
    public void beforeScenario() throws IOException
    {
        //Write code that will give you place Id, in case you call DeletePlace without having first
        //called AddPlace. This is to make the DeletePlace independent of the other API calls.
        //Execute this only when place_Id is null
        System.out.println("In Hooks");

        stepDefs sd = new stepDefs();
        if (place_id==null) //If problems with this, call instead using class name, i.e. StepDefs.place_id
        {
            sd.we_have_a_payload_for_the_addPlaceAPI_with_and("Fred","French", "Uk");
            sd.user_calls_with_http_request("addPlaceAPI", "POST");
            sd.place_id_created_maps_correctly_to_using("Fred", "getPlaceAPI");
        }

    }

}
