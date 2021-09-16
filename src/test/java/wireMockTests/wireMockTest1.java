package wireMockTests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class wireMockTest1
{

    @Test
    public void test1() throws IOException {
        WireMockServer wmServer = new WireMockServer();

        wmServer.start();
        System.out.println("In wire mock");

        //Define the stub
        configureFor("localhost",8080);
        stubFor(get(urlEqualTo("/baeldung")).willReturn(aResponse().withBody("Welcome to Baeldung!")));

        //Make the request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/baeldung");
        HttpResponse httpResponse = httpClient.execute(request);

        //Get the response as string
        //String responseString = httpResponse.toString();
        String stringResponse = convertHttpResponseToString(httpResponse);


        //Check the response
        verify(getRequestedFor(urlEqualTo("/baeldung")));
        assertEquals("Welcome to Baeldung!", stringResponse);

        wmServer.stop();
    }

    private String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}
