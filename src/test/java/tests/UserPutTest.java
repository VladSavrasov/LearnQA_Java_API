package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

public class UserPutTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/, 2 ")
    @Description("Try to Edit User without autorithation")
    @DisplayName(" negative Put case")
    public void putUserData (String url, int userId){
    Map<String, String> userData = new HashMap<>();
    userData.put("username","Vlados");

        Response response = apiCoreRequests
                .makePutRequest(url, userData,userId);

        Assertions.assertTextEquals(response,"Auth token not supplied");

}
    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void LoginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vsvsvs08@rambler.ru");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

        @ParameterizedTest
        @CsvSource("https://playground.learnqa.ru/api/user/, 2, username, Vladius")
        @Description("Try to Edit User with different autorithation")
        @DisplayName(" negative Put case")
        public void putUserDataWithAuth (String url, int userId, String username, String value) {
        Map<String, String> userData = new HashMap<>();
        userData.put(username,value);

        Response response = apiCoreRequests
                .makePutRequest(url, userData, userId, cookie, header);
        Assertions.assertResponseCodeEquals(response,200);

        Response responseCheck = apiCoreRequests
                .makeGetRequest(url,userIdOnAuth, header, cookie);
        String[] fields = {"email","id", "username", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseCheck, fields);
        Assertions.assertJsonByName(responseCheck,username,value);

        Response responseCheckNew = apiCoreRequests
                .makeGetRequest(url,userId, header, cookie);
        Assertions.assertJsonByName(responseCheckNew,username,"Vitaliy");

    }

    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/,  email, vsvsvs08rambler.ru, Invalid email format")
    @Description("Try to Edit User with wrong email")
    @DisplayName(" negative Put email case")
    public void putUserDataWithAuth (String url, String email, String value, String expectedText) {
        Map<String, String> userData = new HashMap<>();
        userData.put(email, value);

        Response response = apiCoreRequests
                .makePutRequest(url, userData, userIdOnAuth, cookie, header);
        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertTextEquals(response,expectedText);

        Response responseCheck = apiCoreRequests
                .makeGetRequest(url, userIdOnAuth, header, cookie);
        String[] fields = {"email", "id", "username", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseCheck, fields);
        Assertions.assertJsonByNameHasNoValue(responseCheck, email, value);

    }
    @ParameterizedTest
    @CsvSource("https://playground.learnqa.ru/api/user/, firstName,V,error, Too short value for field firstName")
    @Description("Try to Edit User username with onr word name")
    @DisplayName(" negative Put case with one word name")
    public void putUserDataWithAuthOneWordName (String url, String firstName, String value, String key, String string) {
        Map<String, String> userData = new HashMap<>();
        userData.put(firstName,value);

        Response response = apiCoreRequests
                .makePutRequest(url, userData, userIdOnAuth, cookie, header);
        Assertions.assertResponseCodeEquals(response,400);
        Assertions.assertJsonByName(response,key,string);


        Response responseCheck = apiCoreRequests
                .makeGetRequest(url,userIdOnAuth, header, cookie);
        String[] fields = {"email","id", "username", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseCheck, fields);
        Assertions.assertJsonByNameHasNoValue(responseCheck, firstName, value);

    }
}
