package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;

//NOTE: you will need to import the above for your step defs to work

public class stepDefs extends Utilities //Using extends here means we do not have to reference Utils to access its methods
{
    //Define these variables at class level so that they are available to all methods
    RequestSpecification request;
    ResponseSpecification thenReqSpec;
    Response latestResponse;
    TestDataBuild data = new TestDataBuild();
    public static String place_id; //Using static means the variable will not be recreated between tests.
    // So value will now be shared between test cases in the same run. (Similar to test context?)


    @Given("we have a payload for the addPlaceAPI with {string}, {string} and {string}")
    public void we_have_a_payload_for_the_addPlaceAPI_with_and(String name, String language, String address) throws IOException
    {
        System.out.println("add place payload with variables");

        request = given().spec(baseRequestSpecificationForGiven()) //Get this method from Utilities
                .body(data.addPlacePayload(name,language,address));
        //Use instance of TestBuildData class to create payload data.
        //It accepts parameters from feature file to build up the payload data.
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String httpMethod) {
        System.out.println("User calls http request:" +httpMethod);

        //Enum constructor will be called with the value of the resouce that you pass here
        APIResources resourceAPI = APIResources.valueOf(resource);

        //Build the response spec for Then
        thenReqSpec =
                new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON)
                .build();

        //Maybe turn this into a Switch  and add the Delete request ?

        //Strictly speaking, the then() here is not required since the status is checked using
        //latestResponse.getStatusCode() and other attributes like Scope can be checked using JsonPath.
        //But it is included here as an example in case other tests need to use this approach.

        if (httpMethod.equalsIgnoreCase("POST"))
            latestResponse = request.when().post(resourceAPI.getResouce())
                    .then().spec(thenReqSpec).extract().response();
        else if (httpMethod.equalsIgnoreCase("GET"))
            latestResponse = request.when().get(resourceAPI.getResouce())
                    .then().spec(thenReqSpec).extract().response();
    }

    @Then("the API call is successful with status code {int}")
    public void the_API_call_is_successful_with_status_code(int expectedStatusCode)
    {
        System.out.println("Assert on status code");
        Assert.assertEquals(expectedStatusCode,latestResponse.getStatusCode());
    }

    @Then("{string} in the response bode is {string}")
    public void in_the_response_bode_is(String node, String expecteValue) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Assert on latestResponse node:" +node);

        Assert.assertEquals(expecteValue, getNodeValueFromResponse(latestResponse, node));
    }

    @Then("place_Id created maps correctely to {string} using {string}")
    public void place_id_created_maps_correctely_to_using(String expectedName, String resource) throws  IOException
    {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Check name equals: "+expectedName);
        //Prepare request spec
        place_id=  getNodeValueFromResponse(latestResponse,"place_id");
        request =
                given().spec(baseRequestSpecificationForGiven()) //Get this method from Utils
                        .queryParam("place_id",place_id );

        //Can reuse a method within the StepDefs calls in other methods, just as you would in any other class.
        user_calls_with_http_request(resource, "GET");
        String actualName=  getNodeValueFromResponse(latestResponse,"name");
        Assert.assertEquals(expectedName,actualName);

    }

    @Given("DeletePlace Payload")
    public void deleteplace_Payload() throws IOException
    {
        System.out.println("delete place payload with variables");

        request =
                given().spec(baseRequestSpecificationForGiven()) //Get this method from Utilites
                .body(data.deletePlacePayload(place_id));
    }

}
