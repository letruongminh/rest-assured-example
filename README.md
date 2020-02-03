---
description: >-
  RestAssured is a DSL Java library which is built on HTTP Builder platform,
  allowing sending and checking response.
---

# Testing with Restful API

### GET Request

Firstly, we need to create setUp\(\) function to set baseURI and basePath. 

```java
@BeforeTest
public void setUp(){
    RestAssured.baseURI = "..."; 
    RestAssured.basePath = "..."; 
}
```

```java
@Test
public void testGetRequest() {
Response resp = RestAssured.given()  
                           .when() 
                           .get("/link_to_path",... params); 
resp.prettyPrint();
resp.then.statusCode(200); # Verify the received status code;                         
}
```

### POST Request

Once you're strong enough, save the world by creating new resources using POST request. Process to create POST request:

Create model \(use **lombok** to create automatically getter and setter function\) 

Convert Java object to JSON \(use **jackson-databind** to convert\). 



{% code title="Student.java" %}
```java
@Data
public void Student(){
    private String strName; 
    private String strJob; 
    
    // Constructor to return new object
    public Student(){}
    
    public static Student getStudent(){
        return new Student(); 
    }
}
```
{% endcode %}

```java
@Test 
public void testPostRequest(){
    // Create new Student object to pass into body
    Student student = Student.getStudent(); 
    student.setStrName("LE Minh"); 
    student.stStrJob("Automation Test Lead"); 
    
    // Call API 
    Response res = RestAssured.given() 
                              .contentType(ContentType.JSON)
                              .body(student)
                              .post(); 
    res.prettyPrint(); 
    res.then().statusCode(201); # Verify that student is created; 
}
```

**Order to test POST request:** 

1. Create a new object. 
2. Set values for fields of above object. 
3. Call API. 
4. Check response after calling API. 

### PUT Request

Like POST request: 

```java
@Test
public void testPutRequest(){
    Response res = RestAssured.given() 
                              .contentType(ContentType.JSON)
                              .body( student )
                              .put( "id/1" ); 
                              
    JsonPath jsonPath = RestAssured.given()
                                   .when()
                                   .get("/list")
                                   .then()
                                   .extract()
                                   .body()
                                   .jsonPath(); 
                                   
    // Get List student from the received JSON; 
    List <Student> lsStudents = jsonPath.getList("", Student.class); 
    RestAssured.assertThat(lsStudents.get(0)
                                     .getFirstName()
                                     , equalTo("LE Minh")); 
    res.prettyPrint(); 
    res.then().statusCode(200); 
}
```

On creating body for POST or PUT request, we need to use Serialization to convert from POJO &gt; JSON. By contrast, conveting from POJO &gt; JSON will use de-Serialization. 

Tips: When we want to verify the response containing JSON, we should firstly return a list of user and then use `((List) lsStudent).get(index)` to get desired object. 

Sometimes, you can use GET request to get the information and use `res.getBody().as(Student.class);` 

In order to verify received results of JSON updated, we have steps as below: 

1. Use GET request to get a list of users. 
2. Get body of JSON object. 
3. Parse it into JSON form. 
4. Get data with structure `key` - `value.`

```java
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
```

> Syntax: as\(ClassName.class\)

Based on the API's user story we will handle differently. In this example, API getStudent\(\) return one list of Student so that I will use JsonPath to de-serialization to List &lt;Student&gt;. 

### DELETE Request

> If we want to log all information of request, use **given\(\).log\(\).all\(\);** 
>
> If we want to log all information of response, use **then\(\).log\(\).all\(\);**

```java
@Test
public void testDeleteAnExistingStudent(){
    // Arrange: Setup data
    Student student = Student.getStudent(); 
    RestAssured.given().log().all(); // log all information of request sent
    
    // Act: call API to delete student 
    Response res = RestAssured.given()
                              .contentType(ContentType.JSON)
                              .when()
                              .delete("/{id}", student.getId()); 
                              
    // Assert: 
    res.prettyPrint(); 
    res.then().statusCode(204);  
}
```

**Example code:**  [https://github.com/letruongminh/rest-assured-example](https://github.com/letruongminh/rest-assured-example)

