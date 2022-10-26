package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().get(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
    public static void assertJsonByNameHasNoValue(Response Response, String name, String unexpectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().get(name);
        assertNotEquals(unexpectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFileName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFileName);
        }
    }

    public static void assertJsonHasNoField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }
    public static void assertJsonHasNoFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFileName : expectedFieldNames) {
            Assertions.assertJsonHasNoField(Response, expectedFileName);
        }
    }


    public static void assertTextEquals(Response Response, String expectedText) {
        assertEquals(expectedText, Response.asString(), "Response text is not as expected");
    }

    public static void assertTextEqualsNew(Response Response, String expectedKey) {
        assertEquals("The following required params are missed: " + expectedKey, Response.asString(), "Response text is not as expected");
    }
    public static void assertResponseCodeEquals(Response response, int expectedCode){
        assertEquals(expectedCode,response.statusCode(),"Unexpected Status code");
    }

}