package resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.fail;
import static io.restassured.RestAssured.given;



public class Utilities extends  DataBaseUtilities
{
    //If you don't make this variable static, then it will be set o null in subsequent runs.
    public static RequestSpecification baseGivenReqSpec;

    //This returns the static class variable baseGivenReqSpec which is of type RequestSpecification.
    //This object contains the instructions that would be passed to the restAssured given() method
    //to set things like BaseUri and Query Parameters etc.
    //Further query parameters can be added, for example:
    //request =
    //given().spec(baseRequestSpecificationForGiven()).queryParam("place_id", place_id)
    //Adds a query parameter called "place_id", with the value from the place_id parameter.
    public RequestSpecification baseRequestSpecificationForGiven() throws IOException {
        if (baseGivenReqSpec == null) //Only need to create if not created already
        {
            //This PrintStream object is used later for the logging filters
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            baseGivenReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalPropsValue("baseUrl"))//This value is held in global.properties file
                    .addQueryParam("key", "qaclick123")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();

            return baseGivenReqSpec;
        }
        return baseGivenReqSpec;
    }

    //New methods
    //In order to use pojo for deserialization of a response (we have already done serialization of requuest
    //payload), see Section 11, lessions 59-63, especially 63, in RestAssured course
    //This will cover creating pojos for the response and getting the response back as object of the pojo
    //class type. Then you can use the getters of the pojo class for interrogating the response.
    //It is less generic than methods used here, but may have advantages once set up.


    public RequestSpecification baseRequestSpecificationForGiven(Map<String, String> params ) throws IOException {
        if (baseGivenReqSpec == null) //Only need to create if not created already
        {
            //This PrintStream object is used later for the logging filters
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            baseGivenReqSpec = new RequestSpecBuilder()
                    .setBaseUri(getGlobalPropsValue("baseUrl"))//This value is held in global.properties file
                    //.setBaseUri(????)//Get this via spring config
                    .addQueryParams(params)
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();

            return baseGivenReqSpec;
        }
        return baseGivenReqSpec;
    }

    //This will get the value for the given key parameters from the global.properties file.
    public String getGlobalPropsValue(String key) throws IOException {
        Properties prop = new Properties();
        //FileInputStream fis = new FileInputStream("C:\\Users\\gregm\\IdeaProjects\\APIFramework\\src\\test\\java\\resources\\global.properties");
        FileInputStream fis = new FileInputStream("src/test/java/resources/global.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }

    //This will get the value of the given node from the given response
    public String getNodeValueFromResponse(Response response, String node) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(node).toString();
    }

    //This will get the value of the given node from the given response, if response already a string
    public String getNodeValueFromResponse(String response, String node) {
        JsonPath js = new JsonPath(response);
        return js.get(node).toString();
    }

    //Get the size of the given node. Note: this ONLY works on ARRAYS
    public int getNodeArraySize(@NotNull Response response, String node)
    {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);

        String jsonPath = node + ".size()";
        int count = js.getInt(jsonPath);
        return count;
    }


    /*
    ###### Hashmap Methods ############
     */

    //Receives a HashMap in the form of <"node", some content, "value", some content> and
    //a response to search for this node and value pair
    //public void verifyAsubNode(Map<String, String> validationItem, String response)
    public void verifyAsubNode(Map<String, String> validationItem, Response response)
    {
        //The value of the 'node' column. This will be a json path, like header.invoice
        String path = validationItem.get("node");

        //Gets expected value
        String expectedItemValue = validationItem.get("value");

        //Gets actual value
        String itemActualValue = getNodeValueFromResponse(response, path);

        Assert.assertEquals("Expected value of node does not match actual", expectedItemValue, itemActualValue);

    }

    /*
    validationRow is a whole row with headers, e.g.
    | node                   | value   | status | message |
    | header.invoiceNumber   | 1928572 | VALID  | null    |
    keyset, is the set of keys, i.e. the header row, e.g.
    | item                   | value   | status | message |
     */

    //Not sure if these methods should be static or not
    //public void verifyJsonPathSubItems(Map<String, String> validationRow, String response)
    public void verifyJsonPathSubItems(Map<String, String> validationRow, Response response)
    {
        //Will create a new hashMap containing the path to each subnode in turn and its expected value
        //So in this case we would have:
        //header.invoiceNumber.value,1928572
        //header.invoiceNumber.status,VALID
        //header.invoiceNumber.message,null
        //Each key, value pair will be passed in turn to the verifyAsubNode() method for checking
        //expected value against actual value for each pair.

        Map<String, String> validationSet = new HashMap<>();
        Set<String> keySet = validationRow.keySet();

        //We loop foreach key in key set, so item, value, status, message (and any others that are present)
        //if the key is node then jump over it using the first if()
        for (String key : keySet)
        {
            if (!key.equals("node"))
            {
                //Checks that the contents of the "node" key is not null (header.invoiceNumber in this case)
                if (validationRow.get("node") != null)
                    //Then it will put the string "node" and the contents of the "node" key plus "." plus the current key from keyset
                    //into the first map entry of the new validationSet map. This is then a path to the subnode
                    //e.g. node.header.invoiceNumber.message
                    validationSet.put("node", validationRow.get("node") + "." + key);
                else
                    fail("'node' column not found in table");

                //In order to get the expected value for the subnode, it then puts the contents of the current key
                //(e.g. message) for validationRow and puts it into validationMapValue string
                String validationMapValue = validationRow.get(key);

                //Then it puts the string "value" and the contents of validationMapValue into the second map entry
                //of the new validationSet map
                // e.g. for message key it would be
                //value, null
                validationSet.put("value", validationMapValue);

                //It then passes this validationSet map to the verifyIndividualJsonItem() method so it can verify
                //(in this case) that in the response the header.invoiceNumber.value node contains 1928572:
                //It then repeats this for each key in keyset. So, the next one would add an entry in validationSet map of:
                //item, header.invoiceNumber.status
                //value, VALID

                //This method would be called again in each pass of the loop in the calling method
                //FollowingValuesArePresentInTheResponse(), so that the process is repeated for each line in the table

                verifyAsubNode(validationSet,response);
            }
        }
    }
}