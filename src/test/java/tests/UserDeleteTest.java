package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userIdOnAuth;
    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/")
    @Description("Try to DELETE Vinkotov user")
    @DisplayName(" negative DELETE case")
    public void DeleteVinkotovUser(String url) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url, userIdOnAuth, cookie, header);

        Assertions.assertTextEquals(responseDeleteUser,"Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @ParameterizedTest
    @Description("Create user")
    @CsvSource("https://playground.learnqa.ru/api/user/,User not found")
    public void createUser(String url,String expectedText){
        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData(userData);

        Response responsePostReg = apiCoreRequests
                .makePostRequest(url,userData);

        this.userIdOnAuth = this.getIntFromJson(responsePostReg, "id");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url+"login", userData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url, userIdOnAuth, cookie, header);
        Response responseCheck = apiCoreRequests
                .makeGetRequest(url,userIdOnAuth, header, cookie);

        Assertions.assertTextEquals(responseCheck,expectedText);
    }

    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/,2,User not found")
    @Description("Try to DELETE Vinkotov user")
    @DisplayName(" negative DELETE case")
    public void DeleteAnotherUser(String url, int id, String expectedText) {

        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData(userData);

        Response responsePostReg = apiCoreRequests
                .makePostRequest(url,userData);

        this.userIdOnAuth = this.getIntFromJson(responsePostReg, "id");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url+"login", userData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url, id, cookie, header);

        Response responseCheck = apiCoreRequests
                .makeGetRequest(url,userIdOnAuth, header, cookie);

        Assertions.assertTextEquals(responseCheck,expectedText);


    }
}
