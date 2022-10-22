package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @BeforeEach
    public void LoginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vsvsvs08@rambler.ru");
        authData.put("password", "123");

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

    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/, 2 ")
    @Description("this test authorizes user by Email and Password")
    @DisplayName("test positive auth")
    public void GetInfoUser(String url, int userId){
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(url, userId);
        String[] fields = {"email, user_id, firstName, lastName"};
        Assertions.assertJsonHasField(responseCheckAuth,"username");
        Assertions.assertJsonHasNoFields(responseCheckAuth,fields);
    }
}

