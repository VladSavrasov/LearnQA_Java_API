import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



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
    @Test
    public void homeWorkTokenTest() throws InterruptedException {
        JsonPath response = (JsonPath) RestAssured
                .get(" https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = response.get("token");
        int seconds = response.get("seconds");


        JsonPath responseOne = RestAssured
                .given()
                .queryParam("token", token)
                .get(" https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = responseOne.get("status");
        if(status.equals("Job is NOT ready")){

            TimeUnit time = TimeUnit.SECONDS;

            time.sleep(seconds);
                ValidatableResponse responseNew = RestAssured
                        .given()
                        .queryParam("token", token)
                        .get(" https://playground.learnqa.ru/ajax/api/longtime_job")
                        .then()
                        .body("status",equalTo("Job is ready"))
                        .body("$",hasKey("result"));

    }

    }
    @ParameterizedTest
    @ValueSource(strings = "SSixTeenCapitals" )
    public void homeWorkFifteenCapitalsCheck(String name){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", name);
        JsonPath response =RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName =(name.length()>15) ? name : "badrequest";
        assertEquals("Hello, "+ expectedName, answer, "parametr less then 15 capitals");
    }
    @Test
    public void homeWorkCookiesCheck(){
        Response response =RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String, String> cookie = response.getCookies();
        String key = cookie.keySet().toString().replace("[", "")
                .replace("]", "");
        String value = cookie.values().toString().replace("[", "")
                .replace("]", "");
        Map<String, String> expectedData = Collections.singletonMap(key, value);
            assertTrue(cookie.containsKey(key),"response doesn't contain such cookie");
            assertTrue(cookie.containsValue(value),"response doesn't contain such value");
        assertEquals(expectedData,response.getCookies(),"cookie doesn't exist");
    }
    @Test
    public void homeWorkHeadersCheck(){
        Response response =RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers header = response.getHeaders();
        System.out.println(header);
        String secretHeader = response.getHeader("x-secret-homework-header");

        assertTrue(header.hasHeaderWithName("x-secret-homework-header"),"response doesn't contain such Header");
        assertEquals(secretHeader,response.getHeader("x-secret-homework-header"),"response header does not exist");
    }
}
