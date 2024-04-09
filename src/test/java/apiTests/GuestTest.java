package apiTests;

import api.config.BaseTest;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import api.pojos.Article;
import api.pojos.Profile;
import api.pojos.User;
import api.pojos.request.*;
import api.pojos.response.*;
import org.testng.annotations.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static api.utils.Specifications.*;
import static org.testng.Assert.*;

public class GuestTest extends BaseTest {

    @Test
    public void getInfo_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .when()
                .get("auth/me")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void register() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(202));

        String username = UUID.randomUUID() + "@mail.com";

        RegisterRequest request = new RegisterRequest(username, "password");

        User response = given()
                .body(request)
                .when()
                .post("auth/register")
                .then()
                .extract().as(User.class);

        assertTrue(response.getId() > 0);
        assertEquals(response.getUsername(), username);
        assertEquals(response.getRoles().get(0), "ROLE_USER");
        assertNull(response.getProfile().getName());
    }

    @Test
    public void changePassword_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        UpdatePasswordRequest request = new UpdatePasswordRequest(3L, "new_password");

        Response response = given()
                .body(request)
                .when()
                .patch("auth/password")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void updateAvatar_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        File file = new File("src/test/resources/avatar.png");

        MultiPartSpecification multiPartFile = new MultiPartSpecBuilder(file)
                .fileName("avatar.png")
                .controlName("avatar")
                .mimeType("image/png")
                .build();

        Response response = given()
                .formParam("id", "3")
                .multiPart(multiPartFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("auth/avatar")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void getUserById_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .when()
                .get("users/2")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void getAllUsers_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .when()
                .get("users?page=0&size=1&sort=id,asc")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void addUser_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        String username = UUID.randomUUID() + "@mail.com";

        Profile profile = Profile.builder()
                .name(username)
                .surname("S")
                .gender("MALE")
                .city("NY")
                .address("address1")
                .company("company1")
                .mobile("3432434")
                .tele("222332222")
                .website("http://itermit.com")
                .date("2000-01-01")
                .build();

        CreateUserRequest request = CreateUserRequest.builder()
                .username(username)
                .password("password")
                .role(Set.of("ROLE_ADMIN"))
                .profile(profile)
                .build();

        Response response = given()
                .body(request)
                .when()
                .post("users")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void updateUser_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        String name = UUID.randomUUID().toString();

        Profile profile = Profile.builder()
                .name(name)
                .surname("S")
                .gender("MALE")
                .city("NY")
                .address("address1")
                .company("company1")
                .mobile("3432434")
                .tele("222332222")
                .website("http://itermit.com")
                .date("2000-01-01")
                .build();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(3L)
                .role(Set.of("ROLE_ADMIN"))
                .profile(profile)
                .build();

        Response response = given()
                .body(request)
                .when()
                .patch("users")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void deleteUserById_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .when()
                .delete("users/4")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void getArticleById() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        Article response = given()
                .when()
                .get("articles/1")
                .then()
                .extract().as(Article.class);

        assertEquals(response.getId(), 1L);
        assertEquals(response.getTitle(), "title1");
        assertEquals(response.getContent(), "<p>content1</p>");
        assertEquals(response.getDescription(), "description1");
        assertEquals(response.getLogo(), "1.jpg");
        assertEquals(response.getAuthor().getId(), 2L);
        assertEquals(response.getPublishDate(), LocalDateTime.of(2021, 01, 17,
                15, 47, 44, 301554000));
        assertEquals(response.getCreatedDate(), LocalDateTime.of(2021, 01, 17,
                15, 47, 44, 301554000));
        assertEquals(response.getLastUpdateDate(), LocalDateTime.of(2021, 01, 23,
                9, 23, 21, 484039000));
    }

    @Test
    public void getAllArticles() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        PageableArticlesResponse response = given()
                .when()
                .get("articles?page=1&size=1&sort=id,asc")
                .then()
                .extract().as(PageableArticlesResponse.class);

        Article firstArticle = response.get_embedded().getArticles()[0];

        assertEquals(firstArticle.getId(), 2L);
        assertEquals(firstArticle.getTitle(), "title2");
        assertEquals(firstArticle.getContent(), "<p>content2</p>");
        assertEquals(firstArticle.getDescription(), "description2");
        assertEquals(firstArticle.getLogo(), "2.jpg");
        assertEquals(firstArticle.getAuthor().getId(), 2L);
        assertEquals(firstArticle.getPublishDate(), LocalDateTime.of(2022, 01, 17,
                15, 47, 45, 301554000));
        assertEquals(firstArticle.getCreatedDate(), LocalDateTime.of(2022, 01, 17,
                15, 51, 44, 301554000));
        assertEquals(firstArticle.getLastUpdateDate(), LocalDateTime.of(2022, 01, 23,
                9, 23, 22, 484039000));
    }

    @Test
    public void addArticle_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        String title = UUID.randomUUID().toString();

        CreateArticleRequest request = CreateArticleRequest.builder()
                .title(title)
                .logo("no logo yet")
                .description("description dddfsff")
                .content("content sdfdsfdsf sdf")
                .authorId(2L)
                .publishDate(null)
                .build();

        Response response = given()
                .body(request)
                .when()
                .post("articles")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void updateArticle_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        String title = UUID.randomUUID().toString();

        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .id(3L)
                .title(title)
                .logo("no logo yet")
                .description("description dddfsff")
                .content("content sdfdsfdsf sdf")
                .authorId(2L)
                .publishDate(null)
                .build();

        Response response = given()
                .body(request)
                .when()
                .patch("articles")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void deleteArticleById_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .when()
                .delete("articles/4")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void saveImage_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        File file = new File("src/test/resources/avatar.png");

        MultiPartSpecification multiPartFile = new MultiPartSpecBuilder(file)
                .fileName("avatar.png")
                .controlName("file")
                .mimeType("image/png")
                .build();

        Response response = given()
                .contentType(ContentType.MULTIPART)
                .multiPart(multiPartFile)
                .when()
                .post("articles/saveImage")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }
}
