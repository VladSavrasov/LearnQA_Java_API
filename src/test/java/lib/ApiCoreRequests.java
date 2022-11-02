package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("make GET request with token and cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("make GET request with cookie")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("make GET request with token")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("make Post request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    /*@Step("make Post request")
    public Response createPostRequest(String url,Map userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();

    }*/
    @Step("make GET request with token and cookie, and userId")
    public Response makeGetRequest(String url, int userId) {
        return given()
                .filter(new AllureRestAssured())
                .get(url + userId)
                .andReturn();
    }

    @Step("make GET request with token and cookie, and userId")
    public Response makePutRequest(String url, Map editData, int userId) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url + userId)
                .andReturn();
    }

    @Step("make PUT request with token and cookie, and userId")
    public Response makePutRequest(String url, Map editData, int userId, String cookie, String header) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .put(url + userId)
                .andReturn();
    }

    @Step("make GET request with token and cookie")
    public Response makeGetRequest(String url, int id, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url + id)
                .andReturn();
    }

    @Step("make DELETE request with token and cookie, and userId")
    public Response makeDeleteRequest(String url, int userId, String cookie, String header) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .delete(url + userId)
                .andReturn();
    }
}
