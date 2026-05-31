package com.acidtangoinditex.productsorter.products.infrastructure.http;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SortProductsE2ETest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/";
    }

    @Test
    void returns_products_ranked_by_a_balanced_combination_of_criteria() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("weights", Map.of(
                        "sales_units", 0.8,
                        "stock_ratio", 0.2
                )))
        .when()
                .post("/products/sorted")
        .then()
                .statusCode(200)
                .body("items", hasSize(6))
                .body("items[0].id", equalTo(5))
                .body("items[0].name", equalTo("CONTRASTING LACE T-SHIRT"))
                .body("items[1].id", equalTo(3))
                .body("items[2].id", equalTo(2))
                .body("items[3].id", equalTo(1))
                .body("items[4].id", equalTo(6))
                .body("items[5].id", equalTo(4));
    }

    @Test
    void returns_products_ranked_by_stock_only_when_sales_weight_is_zero() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("weights", Map.of(
                        "sales_units", 0.0,
                        "stock_ratio", 1.0
                )))
        .when()
                .post("/products/sorted")
        .then()
                .statusCode(200)
                .body("items[0].id", equalTo(2))
                .body("items[1].id", equalTo(3))
                .body("items[2].id", equalTo(4))
                .body("items[3].id", equalTo(6))
                .body("items[4].id", equalTo(1))
                .body("items[5].id", equalTo(5));
    }

    @Test
    void rejects_a_request_with_all_zero_weights() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("weights", Map.of(
                        "sales_units", 0.0,
                        "stock_ratio", 0.0
                )))
        .when()
                .post("/products/sorted")
        .then()
                .statusCode(400)
                .body("code", equalTo("invalid_request"));
    }

    @Test
    void rejects_a_request_referencing_an_unknown_criterion() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("weights", Map.of("popularity", 0.5)))
        .when()
                .post("/products/sorted")
        .then()
                .statusCode(400)
                .body("code", equalTo("unknown_sorting_criterion"));
    }

    @Test
    void rejects_a_request_with_an_empty_weights_map() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("weights", Map.of()))
        .when()
                .post("/products/sorted")
        .then()
                .statusCode(400)
                .body("code", Matchers.anyOf(equalTo("validation_failed"), equalTo("invalid_request")));
    }
}
