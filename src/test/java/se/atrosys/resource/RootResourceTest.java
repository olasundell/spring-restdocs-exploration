package se.atrosys.resource;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

/**
 * TODO write documentation
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RootResourceTest {
	@Rule
	public JUnitRestDocumentation restDocumentation =
			new JUnitRestDocumentation("build/generated-snippets");

	private RequestSpecification spec;

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		this.spec = new RequestSpecBuilder()
				.setPort(port)
				.addFilter(documentationConfiguration(this.restDocumentation))
				.build();
	}

	@Test
	public void shouldWork() {
		RestAssured.given(this.spec)
				.accept("application/json")
				.filter(document("index"))
				.when().get("/")
				.then().assertThat().statusCode(is(200));
	}
}
