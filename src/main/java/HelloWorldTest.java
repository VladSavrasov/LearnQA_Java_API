import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import java.sql.SQLOutput;

public class HelloWorldTest {
    @Test
    public void testHelloWorld(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }
    @Test
    public void testAgain(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
    @Test
    public void homeWorkTest(){
        JsonPath response = (JsonPath) RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String messages = response.get("messages.message[1]").toString();
        System.out.println(messages);
    }
    @Test
    public void homeWorkRedirectTest(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);

    }
    @Test
    public void homeWorkLongRedirectTest(){
        int i = 0;
        String baseUrl = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;


        while (statusCode != 200){

            i = ++i;
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(baseUrl)
                    .andReturn();

            String locationHeader = response.getHeader("Location");
            statusCode = response.getStatusCode();

            baseUrl = locationHeader;
            if(locationHeader != null) {
                System.out.println(locationHeader);
            }
        }
        i =--i;
        System.out.println(i);

    }

}
