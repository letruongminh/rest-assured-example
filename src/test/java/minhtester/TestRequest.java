package minhtester;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Student;

import model.Student;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

public class TestRequest {
    @Test
    public void testGetListUser() throws URISyntaxException {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Response resp = RestAssured.given()
                                    .when()
                                    .get("/users?page={id}", 3);
        int code = resp.getStatusCode();
        System.out.println(code);
        resp.prettyPrint();
    }

    @Test
    public void testGetSingleUser(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/users";
        Response resp = RestAssured.given()
                                    .when()
                                    .get("/{id}", 2);
        int status = resp.statusCode();
        resp.prettyPrint();
        resp.then().statusCode(200);
        RestAssured.responseSpecification = new ResponseSpecBuilder().expectStatusCode(200)
                .build();

    }

    @Test
    public void testGet404Error(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/users";
        Response resp = RestAssured.given()
                .when()
                .get("/{id}", 23);
        int status = resp.statusCode();
        resp.prettyPrint();
        resp.then().statusCode(404);
    }

    @Test
    public void testGetListResources(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Response resp = RestAssured.given()
                                    .when()
                                    .get("/unknown");
        resp.prettyPrint();
        resp.then().statusCode(200);
    }

    @Test
    public void testGetSingleResources(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Response resp = RestAssured.given()
                .when()
                .get("/unknown/{id}", 2);
        resp.prettyPrint();
        resp.then().statusCode(200);
    }

    @Test
    public void testGetSingleResourceNotFound(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Response resp = RestAssured.given()
                .when()
                .get("/unknown/{id}", 23);
        resp.prettyPrint();
        resp.then().statusCode(404);
    }

    @Test
    public void testCreateNewUser(){
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api/users/";
        Student student = Student.getStudent();
        student.setName( "morpheus" );
        student.setJob( "leader" );

        Response resp = RestAssured.given()
                                    .contentType(ContentType.JSON)
                                    .when()
                                    .body(student)
                                    .post();
        resp.getBody();
        resp.prettyPrint();
        resp.then().statusCode(201);
    }
}
