package io.hellgren.wiremockdemo;

import io.restassured.RestAssured;
import org.apache.http.client.ClientProtocolException;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	WireMockConfig wireMockConfig;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = wireMockConfig.getPort();
	}

	@Test
	void testEndpoint() {
		RestAssured
				.given()
					.body("{\"my_made_up\":\"json\"}")
					.header("Content-Type", "application/json")
					.post("/notify")
				.then()
					.statusCode(200)
				.assertThat()
					.body("result.data.status", Matchers.equalTo("OK"));
	}

	@Test
	void testTransformerEndpoint() {
		RestAssured
				.given()
				.get("/notify/transformer")
				.then()
				.statusCode(200)
				.assertThat()
				.body("format", Matchers.equalTo("wrong"));
	}

	@Test
	void testDelay() {
		RestAssured
				.given()
				.get("/notify/delay")
				.then()
				.statusCode(200)
				.assertThat()
				.time(Matchers.greaterThanOrEqualTo(1000L));
	}

	@Test
	void testChunkError() {
		Throwable thrown = Assertions.catchThrowable(() -> {
			RestAssured
					.given()
					.get("/notify/chunk-error")
					.then()
					.statusCode(200);
		});

		Assertions.assertThat(thrown).isInstanceOf(ClientProtocolException.class);
	}
}
