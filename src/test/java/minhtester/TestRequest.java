package minhtester;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Student;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import javax.print.attribute.ResolutionSyntax;
import java.awt.*;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

        Response resp = RestAssured.given()
                                    .contentType(ContentType.JSON)
                                    .when()
                                    .body(student)
                                    .post();
        resp.getBody();
        resp.prettyPrint();
        resp.then().statusCode(201);
    }

    @Test
    public void testDeleteInformation(){
        Response res = RestAssured.given()
                                    .contentType(ContentType.JSON)
                                    .when()
                                    .delete("https://reqres.in/api/users/2");
        res.prettyPrint();
        res.then().statusCode(204);
    }

    @Test
    public void testDelayResponse(){
        Response res = RestAssured.given().contentType(ContentType.JSON)
                                            .when()
                                            .get("https://reqres.in/api/users?delay={id}", 3);
        res.prettyPrint();
        res.then().statusCode(200);
    }

    @Test
    public void testUpdateExistingUser() throws IOException, ParseException {
        // 1. Check information of the current user
        Response res;

        Student student = Student.getStudent();
        student.setId(2)
                .setFirstName("LE")
                .setLastName("Minh")
                .setEmail("student1_test@sharklasers.com")
                .setAvatar("https://github.com/letruongminh/rest-assured-example/tree/master/src/test/java/minhtester");

        res = RestAssured.given()
                        .when()
                        .body( student )
                        .put("https://reqres.in/api/users/2");
        res = RestAssured.given()
                .when()
                .get("https://reqres.in/api/users/");
        res.prettyPrint();
        String str = res.body().asString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(str);
        JSONArray array = (JSONArray) jsonObj.get("data");
        System.out.println(((JSONObject) array.get(1)).get("email"));

    }
}
