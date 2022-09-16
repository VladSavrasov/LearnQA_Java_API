import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
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
}
