package stepDefinitions;

import io.cucumber.core.internal.gherkin.deps.com.google.gson.Gson;
import io.cucumber.core.internal.gherkin.deps.com.google.gson.stream.JsonReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import pojo.AddPlaceNoSetter;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utilities;
import resources.DataBaseUtilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

//NOTE: you will need to import the above for your step defs to work

public class stepDefs extends Utilities //Using extends here means we do not have to reference Utils to access its methods
{
    //Define these variables at class level so that they are available to all methods
    Map<String, String> parameters;
    String resourceURL;
    RequestSpecification request;
    ResponseSpecification thenReqSpec;
    Response latestResponse;
    AddPlaceNoSetter addPlaceObject;
    TestDataBuild data = new TestDataBuild();
    public static String place_id; //Using static means the variable will not be recreated between tests.
    // So value will now be shared between test cases in the same run. (Similar to test context?)

    //DB Variables
    Connection MySQlConnection;    ResultSet dbResults;

    @Given("we have a payload for the addPlaceAPI with {string}, {string} and {string}")
    public void we_have_a_payload_for_the_addPlaceAPI_with_and(String name, String language, String address) throws IOException {
        System.out.println("add place payload with variables");

        //Use instance of TestBuildData class to create payload data.
        //It accepts parameters from feature file to build up the payload data.
                request = given().spec(baseRequestSpecificationForGiven(parameters)) //Get this method from Utilities
                .body(data.addPlacePayload(name, language, address));
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String httpMethod) {
        System.out.println("User calls http request:" + httpMethod);

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

    //New steps
    @Given("I create a GET request")
    public void i_create_a_GET_request() throws IOException
    {
        request =
                given().spec(baseRequestSpecificationForGiven(parameters)); //Get this method from Utils
    }


    @When("user calls given URL with {string} http request")
    public void user_calls_given_URL_with_http_request(String httpMethod) {
        System.out.println("User calls http request:" + httpMethod + "for given url:" +resourceURL);

        if (httpMethod.equalsIgnoreCase("POST"))
            latestResponse = request.when().post(resourceURL).then().extract().response();
        else if (httpMethod.equalsIgnoreCase("GET"))
            latestResponse = request.when().get(resourceURL)
                    .then().extract().response();
    }

    @Then("the API call is successful with status code {int}")
    public void the_API_call_is_successful_with_status_code(int expectedStatusCode) {
        System.out.println("Assert on status code");
        Assert.assertEquals(expectedStatusCode, latestResponse.getStatusCode());
    }

    @Then("{string} in the response bode is {string}")
    public void in_the_response_bode_is(String node, String expecteValue)
    {
        System.out.println("Assert on latestResponse node:" + node);
        Response checkresp = latestResponse;
        System.out.println("latestResponse is" + checkresp.toString());


        Assert.assertEquals(expecteValue, getNodeValueFromResponse(latestResponse, node));
    }

    @Then("place_Id created maps correctly to {string} using {string}")
    public void place_id_created_maps_correctly_to_using(String expectedName, String resource) throws IOException
    {
        System.out.println("Check name equals: " + expectedName);
        //Prepare request spec
        place_id = getNodeValueFromResponse(latestResponse, "place_id");
        request =
                given().spec(baseRequestSpecificationForGiven()) //Get this method from Utils
                        .queryParam("place_id", place_id);

        //Can reuse a method within the StepDefs calls in other methods, just as you would in any other class.
        user_calls_with_http_request(resource, "GET");

        String actualName = getNodeValueFromResponse(latestResponse, "name");
        Assert.assertEquals(expectedName, actualName);

    }

    @Given("DeletePlace Payload")
    public void deleteplace_Payload() throws IOException {
        System.out.println("delete place payload with variables");

        request =
                given().spec(baseRequestSpecificationForGiven()) //Get this method from Utilites
                        .body(data.deletePlacePayload(place_id));
    }

    @Then("the following values are checked using hashmaps:")
    public void theFollowingValuesAreCheckedUsingHashmaps(List<Map<String, String>> validationItems) {
        for (Map<String, String> item : validationItems) {
            //Takes a list of hashmaps as input parameter.
            //The represents a table. The map representing a series of  column header, value pairs and each line
            //represented by a new list item
            for (Map<String, String> validationRow : validationItems)
            {
                verifyJsonPathSubItems(validationRow, latestResponse);
            }
        }

    }

    @Given("I set parameters")
    public void i_set_parameters(Map<String, String> params) {
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or

        parameters = params;
    }

    @Given("I set resource URL to {string}")
    public void i_set_URL(String url)
    {
        resourceURL = url;
    }

    @Given("I set a payload using file {string}")
    public void i_set_a_payload_using_file(String inputFilename) throws IOException
    {
        //Will take the given file and turn the contents into an addPlaceObject based on
        //AddPlaceNoSetter class (i.e. this class is a model of how the data should look)
        JsonReader reader = new JsonReader(new FileReader((inputFilename)));
        addPlaceObject = new Gson().fromJson(reader, AddPlaceNoSetter.class);
    }

    @Given("we have a payload loaded from a file")
    public void we_have_a_payload_loaded_from_a_file() throws IOException {
        System.out.println("add place payload from file");

        request = given().spec(baseRequestSpecificationForGiven(parameters)) //Get this method from Utilities
                .body(addPlaceObject);

    }

//###DB Steps##

    @Given("I connect to DB with {string}, {string} and {string}")
    public void i_connect_to_DB_with_and(String URL, String username, String password)  throws SQLException
    {
        MySQlConnection = getDBconnection(URL,username,password);

    }


    @When("I execute the SQL query {string}")
    public void i_execute_the_SQL_query(String queryString) throws SQLException
    {
        PreparedStatement selectStatement = MySQlConnection.prepareStatement(queryString);
        dbResults = selectStatement.executeQuery();
    }

    @When("I execute the SQL query in file {string}")
    public void i_execute_the_SQL_query_in_file(String fileName) throws FileNotFoundException, SQLException {
        String SQLquery = readSQLFile(fileName);

        PreparedStatement selectStatement = MySQlConnection.prepareStatement(SQLquery);
        dbResults = selectStatement.executeQuery();
    }

    @When("I execute the SQL query in file src\\/test\\/java\\/resources\\/sqlFiles\\/sql1.txt")
    public void i_execute_the_SQL_query_in_file_src_test_java_resources_sqlFiles_sql1_txt() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Then("I print out the names")
    public void i_print_out_the_names() throws SQLException {
        // will traverse through all found rows
        while (dbResults.next()) {
            String firstName = dbResults.getString("first_name");
            String lastName = dbResults.getString("last_name");
            System.out.println("firstName = " + firstName + "," + "lastName= " + lastName );
        }
    }
}

