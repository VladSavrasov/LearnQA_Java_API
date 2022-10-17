package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @BeforeEach
    public void LoginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token") ;
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");
    }
    @Test
    @Description("this test authorizes user by Email and Password")
    @DisplayName("test positive auth")
    public void testAuthUser(){
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie);

       Assertions.assertJsonByName(responseCheckAuth,"user_id", this.userIdOnAuth);
    }
    @Description("test auth without cookie or headers")
    @DisplayName("test negative auth")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","headers"})
    public void testNegativeAuthUser(String condition) {

        if (condition.equals("cookie")) {
            Response responseFoCheck = apiCoreRequests.makeGetRequestWithCookie("https://playground.learnqa.ru/api/user/auth",
                    this.cookie);
            Assertions.assertJsonByName(responseFoCheck,"user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseFoCheck = apiCoreRequests.makeGetRequestWithToken( "https://playground.learnqa.ru/api/user/auth",
                this.header);
            Assertions.assertJsonByName(responseFoCheck,"user_id", 0);
        } else {
            throw new IllegalArgumentException("condition value is known: " + condition);
        }

    }

}
