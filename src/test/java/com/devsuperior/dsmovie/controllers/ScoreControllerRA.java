package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerRA {

	private String clientusername, clientpassword, adminusername, adminpassword;
	private String clientToken, adminToken, invalidToken;
	private Map<String, Object> putMovieIdInstance;

	@BeforeEach
	void seUp() throws JSONException {
		baseURI = "http://localhost:8080";
		adminusername = "maria@gmail.com";
		adminpassword = "123456";
		clientusername = "alex@gmail.com";
		clientpassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientusername, clientpassword);
		adminToken = TokenUtil.obtainAccessToken(adminusername, adminpassword);
		invalidToken = adminToken + "xpto";

		putMovieIdInstance = new HashMap<>();
		putMovieIdInstance.put("score", 5);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		putMovieIdInstance.put("movieId", 30);
		JSONObject newScore = new JSONObject(putMovieIdInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.body("error", equalTo("Recurso não encontrado"))
				.body("status", equalTo(404));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putMovieIdInstance.put("score", 5);
		JSONObject newScore = new JSONObject(putMovieIdInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("errors.message[0]", equalTo("Campo requerido"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		putMovieIdInstance.put("movieId", 3);
		putMovieIdInstance.put("score", -1);
		JSONObject newScore = new JSONObject(putMovieIdInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("errors.message[0]", equalTo("Valor mínimo 0"));
	}
}
