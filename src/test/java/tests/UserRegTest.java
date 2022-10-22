package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

public class UserRegTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

   @ParameterizedTest
   @Description("Create user with invalid email")
   @CsvSource("https://playground.learnqa.ru/api/user/, vinkotovexample.com, Invalid email format")
   public void createUserWithWrongEmail(String url,String email, String expectedText){
       Map<String, String> userData = new HashMap<>();
       userData.put("email", email);
       userData = DataGenerator.getRegistrationData(userData);

        Response responsePostReg = apiCoreRequests
                .makePostRequest(url,userData);
       Assertions.assertTextEquals(responsePostReg, expectedText );
    }

    @ParameterizedTest
    @Description("Create user without one of params")
    @CsvSource("https://playground.learnqa.ru/api/user/,,")
    public void createUserWithOutParam(String url,String path){

        String[] keys = {"email","password", "username", "firstName", "lastName"};
        for(String key : keys) {
            Map<String, String> userData = new HashMap<>();
            userData.put(key, path);
            userData = DataGenerator.getRegistrationData(userData);

            Response responsePostReg = apiCoreRequests
                    .makePostRequest(url, userData);
            Assertions.assertTextEqualsNew(responsePostReg, key);
        }
    }

    @ParameterizedTest
    @Description("Create user with invalid email")
    @ValueSource(strings = {"v",
            "werwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwerwerwwerwyerwerw"})
    public void createUserWithDifferentUsername(String username){
        String url = "https://playground.learnqa.ru/api/user/";
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responsePostReg = apiCoreRequests
                .makePostRequest(url,userData);
        if(username.equals("v")){
            String expectedText = "The value of 'username' field is too short";
            Assertions.assertTextEquals(responsePostReg, expectedText );
        }
        else {
            String expectedText = "The value of 'username' field is too long";
            Assertions.assertTextEquals(responsePostReg, expectedText );
        }
    }
}
