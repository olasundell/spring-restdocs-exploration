package se.atrosys.resource;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

/**
 * TODO write documentation
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlphabetResourceTest {
	@Rule
	public JUnitRestDocumentation restDocumentation =
			new JUnitRestDocumentation("build/generated-snippets");

	private RequestSpecification spec;

	@LocalServerPort
	int port;

	@Before
	public void setUp() {
		this.spec = new RequestSpecBuilder()
				.setPort(port)
				.addFilter(documentationConfiguration(this.restDocumentation))
				.build();
	}

	@Test
	public void charAtShouldReturnChar() {
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

	@Test
	public void shouldValidateParameter() {
		RestAssured.given(this.spec)
				.accept("application/json")
				.filter(document("alphabet"))
				.given().param("pos", -1)
				.when().get("/alphabet/charAt")
				.then()
					.assertThat().contentType(ContentType.JSON)
					.assertThat().statusCode(is(400))
					.assertThat().body("field", equalTo("a"))
					.assertThat().body("message", equalTo("a"))
					.assertThat().body("code", equalTo("A"));

	}
}