package resources;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class Utilities
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
    public RequestSpecification baseRequestSpecificationForGiven() throws  IOException
    {
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
        return  baseGivenReqSpec;
    }




    //This will get the value for the given key parameters from the global.properties file.
    public String getGlobalPropsValue(String key) throws IOException
    {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("C:\\Users\\gregm\\IdeaProjects\\APIFramework\\src\\test\\java\\resources\\global.properties");
        prop.load(fis);
        return  prop.getProperty(key);
    }

    //This will get the value of the given node from the given response
    public String getNodeValueFromResponse(Response response, String node)
    {
        String resp = response.asString();
        JsonPath js =new JsonPath(resp);
        return  js.get(node).toString();
    }

    //Get the size of the given node. Note: this ONLY works on ARRAYS
    public int getNodeArraySize(Response response, String node)
    {
         String resp = response.asString();
         JsonPath js = new JsonPath(resp);

         String jsonPath = node+".size()";
         int count = js.getInt(jsonPath);
         return count;
    }

}
