package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

//NOTE: you will need to import the above for your step defs to work

public class stepDefs
{
    @Given("we have a payload for the addPlaceAPI with {string}, {string} and {string}")
    public void we_have_a_payload_for_the_addPlaceAPI_with_and(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("In step 1");
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("In step 2");
    }

    @Then("the API call is successful with status code {string}")
    public void the_API_call_is_successful_with_status_code(String string) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("In step 3");
    }

    @Then("{string} in the response bode is {string}")
    public void in_the_response_bode_is(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("In step 4");
    }

    @Then("place_Id created maps correctely to {string} using {string}")
    public void place_id_created_maps_correctely_to_using(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("In step 5");
    }




}
