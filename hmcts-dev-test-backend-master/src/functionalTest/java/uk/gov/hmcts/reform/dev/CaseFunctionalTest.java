package uk.gov.hmcts.reform.dev;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CaseFunctionalTest {
	
	private static final Logger logger =  LoggerFactory.getLogger(CaseFunctionalTest.class);
	
    protected static final String CONTENT_TYPE_VALUE = "application/json";

    @Value("${TEST_URL:http://localhost:4000/tasks}")
    private String testUrl;

   // Setup RestAssured before each test to use base URL and relaxed HTTPS validation
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }
    
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testFeatchId(Long id, String message, int resultCode) {
    	RestAssured.baseURI = "http://localhost:4000/tasks";
    	Response response = RestAssured.given()
    			.pathParam("id", id)
    			.get("/{id}")
    			.then()
    			.statusCode(resultCode)
    			.extract().response();
    	
//    	String jsonResponse = response.asString();
//    	logger.info("JSON Response: {}", jsonResponse);

    	Map<String, Object> jsonMap = response.jsonPath().getMap("");
    	logger.info("Parsed JSON Map: {}", jsonMap);
    	
    	if(resultCode ==200) {
    		assertEquals(jsonMap.get("title"), message);
    	} else {
    		assertEquals(jsonMap.get("error"), message);
    	}
    	
    }
    
    private static Stream<Arguments> provideIds(){
    	return Stream.of(
    			Arguments.of(1L, "Title1", 200),
    			Arguments.of(5L, "Not Found", 404),
    			Arguments.of(0L, "Bad Request", 400)
    			);
    }
}
