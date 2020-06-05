package br.com.humbertofernandes.aluno.api.controller;

import br.com.humbertofernandes.aluno.api.model.Aluno;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;

/**
 * @author Humberto Tadeu de Paiva Gomes Fernandes
 */
@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class AlunoControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_findAll_aluno() {
        given()
                .get("/aluno")
                .then()
                .log().body().and()
                .statusCode(HttpStatus.OK.value())
                .body("id", containsInAnyOrder(1, 2, 3, 4, 5),
                        "nome", containsInAnyOrder("Thais", "Lourdes", "Humberto", "Rubens", "Lucas"),
                        "idade", containsInAnyOrder(27, 28, 29, 57, 59));
    }

    @Test
    public void should_find_aluno_by_id() {
        given()
                .pathParam("id", 1L)
                .get("/aluno/{id}")
                .then()
                .log().body().and()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1),
                        "nome", equalTo("Humberto"),
                        "idade", equalTo(29));
    }

    @Test
    public void should_return_error_not_found_when_find_aluno_by_id() {
        given()
                .pathParam("id", 10L)
                .get("/aluno/{id}")
                .then()
                .log().body().and()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("statusCode", equalTo(404),
                        "reasonPhrase", equalTo("Not Found"),
                        "errors", hasItems(hasEntry("code", "aluno-5")));
    }

    @Test
    public void should_save_a_new_aluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("João");
        aluno.setIdade(10);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", equalTo("http://localhost:" + port + "/aluno/6"))
                .body("id", equalTo(6),
                        "nome", equalTo("João"),
                        "idade", equalTo(10));
    }

    @Test
    public void should_not_save_two_alunos_with_the_same_nome() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");
        aluno.setIdade(10);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-4")));
    }

    @Test
    public void should_not_save_a_aluno_with_the_same_blank_nome() {
        Aluno aluno = new Aluno();
        aluno.setNome("");
        aluno.setIdade(10);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1")));
    }

    @Test
    public void should_not_save_a_aluno_with_the_same_null_nome() {
        Aluno aluno = new Aluno();
        aluno.setIdade(10);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1")));
    }

    @Test
    public void should_not_save_a_aluno_with_the_same_null_idade() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-2")));
    }

    @Test
    public void should_not_save_a_aluno_under_the_idade_of_zero() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");
        aluno.setIdade(-1);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-3")));
    }

    @Test
    public void should_not_save_a_aluno_missing_nome_and_idade() {
        Aluno aluno = new Aluno();

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1"), hasEntry("code", "aluno-2")));
    }

    @Test
    public void should_return_generic_error_when_saving_a_aluno_when_value_entered_is_invalid() {
        String json = "{\"nome\": \"João\", \"idade\": \"19#\"}";

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(json)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "generic-1")));
    }

    @Test
    public void should_return_generic_error_when_saving_a_aluno_when_json_it_is_poorly_formatted() {
        String json = "{\"nome\"}";

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(json)
                .when()
                .post("/aluno")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "generic-2")));
    }

    @Test
    public void should_update_a_aluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("João");
        aluno.setIdade(10);

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1),
                        "nome", equalTo("João"),
                        "idade", equalTo(10));
    }

    @Test
    public void should_not_update_two_alunos_with_the_same_nome() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");
        aluno.setIdade(10);

        given()
                .pathParam("id", 2L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-4")));
    }

    @Test
    public void should_not_update_aluno_not_exist() {
        Aluno aluno = new Aluno();
        aluno.setNome("João");
        aluno.setIdade(10);

        given()
                .pathParam("id", 6L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("statusCode", equalTo(404),
                        "reasonPhrase", equalTo("Not Found"),
                        "errors", hasItems(hasEntry("code", "aluno-5")));
    }

    @Test
    public void should_not_update_a_aluno_with_the_same_blank_nome() {
        Aluno aluno = new Aluno();
        aluno.setNome("");
        aluno.setIdade(10);

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1")));
    }

    @Test
    public void should_not_update_a_aluno_with_the_same_null_nome() {
        Aluno aluno = new Aluno();
        aluno.setIdade(10);

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1")));
    }

    @Test
    public void should_not_update_a_aluno_with_the_same_null_idade() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-2")));
    }

    @Test
    public void should_not_update_a_aluno_under_the_idade_of_zero() {
        Aluno aluno = new Aluno();
        aluno.setNome("Humberto");
        aluno.setIdade(-1);

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-3")));
    }

    @Test
    public void should_not_update_a_aluno_missing_nome_and_idade() {
        Aluno aluno = new Aluno();

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(aluno)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "aluno-1"), hasEntry("code", "aluno-2")));
    }

    @Test
    public void should_return_generic_error_when_updating_a_aluno_when_value_entered_is_invalid() {
        String json = "{\"nome\": \"João\", \"idade\": \"19#\"}";

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(json)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "generic-1")));
    }

    @Test
    public void should_return_generic_error_when_updating_a_aluno_when_json_it_is_poorly_formatted() {
        String json = "{\"nome\"}";

        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(json)
                .when()
                .put("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400),
                        "reasonPhrase", equalTo("Bad Request"),
                        "errors", hasItems(hasEntry("code", "generic-2")));
    }

    @Test
    public void should_delete_a_aluno() {
        given()
                .pathParam("id", 1L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .when()
                .delete("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void should_not_delete_a_aluno_not_exist() {
        given()
                .pathParam("id", 6L)
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .when()
                .delete("/aluno/{id}")
                .then()
                .log().headers()
                .and()
                .log().body()
                .and()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("statusCode", equalTo(404),
                        "reasonPhrase", equalTo("Not Found"),
                        "errors", hasItems(hasEntry("code", "aluno-5")));
    }
}
