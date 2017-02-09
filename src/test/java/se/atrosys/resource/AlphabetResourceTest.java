package se.atrosys.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.atrosys.model.CharAt;
import se.atrosys.model.ErrorMessage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

/**
 * TODO write documentation
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlphabetResourceTest {
	private ManualRestDocumentation restDocumentation =
			new ManualRestDocumentation("build/generated-snippets");

	private RequestSpecification spec;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp(TestInfo testInfo) {
		this.spec = new RequestSpecBuilder()
				.setPort(port)
				.addFilter(documentationConfiguration(this.restDocumentation))
				.build();

		Optional<Method> testMethod = testInfo.getTestMethod();

		if (!testMethod.isPresent()) {
			throw new IllegalArgumentException("Lacking method name");
		}

		this.restDocumentation.beforeTest(getClass(), testMethod.get().getName());
	}

	@AfterEach
	void tearDown() {
		this.restDocumentation.afterTest();
	}

	@Test
	void charAtShouldReturnChar() {
		RestAssured.given(this.spec)
				.accept("application/json")
				.filter(document("alphabet"))
				.given().param("pos", 1)
				.when().get("/alphabet/charAt")
				.then()
					.assertThat().statusCode(is(200))
					.assertThat().contentType(ContentType.JSON)
					.assertThat().body("lowerCase", equalTo("a"))
					.assertThat().body("upperCase", equalTo("A"));
	}

	private void parameterTest(int pos,
	                           int statusCode,
	                           Consumer<ResponseBodyExtractionOptions> bodyAsserter) {
		ResponseBodyExtractionOptions body = RestAssured.given(this.spec)
				.accept("application/json")
				.filter(document("alphabet"))
				.given().param("pos", pos)
				.when().get("/alphabet/charAt")
				.then()
				.contentType(ContentType.JSON)
				.statusCode(is(statusCode))
				.extract().body();

		bodyAsserter.accept(body);
	}

	private void bodyOK(ResponseBodyExtractionOptions body) {
		CharAt result = body.as(CharAt.class);

		Assertions.assertEquals('a', result.getLowerCase());
		Assertions.assertEquals('A', result.getUpperCase());
	}

	private void bodyParamValidationError(ResponseBodyExtractionOptions body) {
		String fromValue = body.asString();
		List<ErrorMessage> result = null;

		try {
			result = new ObjectMapper().readValue(fromValue, new TypeReference<List<ErrorMessage>>() { });
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());

		Assertions.assertEquals("pos", result.get(0).getField());
	}

	@TestFactory
	Iterator<DynamicTest> validateParameters() {
		return Arrays.asList(
				dynamicTest("Positive parameter", () -> parameterTest(1, 200, this::bodyOK)),
				dynamicTest("Negative parameter", () -> parameterTest(-1, 400, this::bodyParamValidationError)),
				dynamicTest("Positive parameter out-of-bounds", () -> parameterTest(30, 400, this::bodyParamValidationError))
		).iterator();
	}
}