package stepDefinitions;


import io.cucumber.java.Before;
import java.io.IOException;
//import static stepDefinitions.StepDefs.place_id; //WON'T WORK UNTIL WE FINISH STEPDEFINTIONS CLASS

public class Hooks
{
    @Before("@DeletePlace") //This only applies to the scenarios with this tag.
    public void beforeScenario() throws IOException
    {
        //Write code that will give you place Id, in case you call DeletePlace without having first
        //called AddPlace. This is to make the DeletePlace independent of the other API calls.
        //Execute this only when place_Id is null
        System.out.println("In Hooks");

        stepDefs dsd = new stepDefs();
//        if (place_id==null) //If problems with this, call instead using class name, i.e. StepDefs.place_id
//        {
//            //WON'T WORK UNTIL WE FINISH STEPDEFINTIONS CLASS
//        }

    }

}
