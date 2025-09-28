package uk.gov.hmcts.reform.dev;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CaseSmokeTest {
	
	private static final Logger logger =  LoggerFactory.getLogger(CaseSmokeTest.class);
	
    protected static final String CONTENT_TYPE_VALUE = "application/json";

    @Value("${TEST_URL:http://localhost:8080}")
    private String testUrl;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @ParameterizedTest
    @MethodSource("provideIds")
    public void testResponse(Long id, int resultCode) {
    	RestAssured.baseURI = "http://localhost:4000/tasks";
    	Response response = RestAssured.given()
    			.pathParam("id", id)
    			.get("/{id}")
    			.then()
    			.statusCode(resultCode)
    			.extract().response();
    	
    	logger.info("response {0}", response);
    	
    }
    
    private static Stream<Arguments> provideIds(){
    	return Stream.of(
    			Arguments.of(1L, 200),
    			Arguments.of(5L, 404)
    			);
    }
}
