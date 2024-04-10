package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private String clientusername, clientpassword, adminusername, adminpassword;
	private String clientToken, adminToken, invalidToken;
	private String titleName;
	private Long existingMovieId, nonExistingMovietId;

	@BeforeEach
	void seUp() throws JSONException {
		baseURI = "http://localhost:8080";
		adminusername = "maria@gmail.com";
		adminpassword = "123456";
		clientusername = "alex@gmail.com";
		clientpassword = "123456";
		titleName = "Guerra";

		clientToken = TokenUtil.obtainAccessToken(clientusername, clientpassword);
		adminToken = TokenUtil.obtainAccessToken(adminusername, adminpassword);
		invalidToken = adminToken + "xpto";

	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
				.get("/movies")
				.then()
				.statusCode(200)
				.body("content.title", hasItems("The Witcher", "Venom: Tempo de Carnificina"));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		given()
				.get("/movies?title={titleName}", titleName)
				.then()
				.statusCode(200)
				.body("content.id[0]", is(11))
				.body("content.title[0]", equalTo("Star Wars: A Guerra dos Clones"))
				.body("content.score[0]", is(0.0F))
				.body("content.count[0]", is(0))
				.body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/uK15I3sGd8AudO9z6J6vi0HH1UU.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		existingMovieId = 3L;

		given()
				.get("/movies/{id}", existingMovieId)
				.then()
				.statusCode(200)
				.body("id", is(3))
				.body("title", equalTo("O Espetacular Homem-Aranha 2: A Ameaça de Electro"))
				.body("score", is(0.0F))
				.body("count", is(0))
				.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/u7SeO6Y42P7VCTWLhpnL96cyOqd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		nonExistingMovietId = 50L;

		given()
				.get("/movies/{id}", nonExistingMovietId)
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"))
				.body("status", equalTo(404));
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
