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
import org.json.simple.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static api.utils.Specifications.*;
import static org.testng.Assert.*;

public class UserTest extends BaseTest {

    private static final LoginRequest REQUEST_AUTH = new LoginRequest("user@mail.com", "password");
    private String authToken;
    private String refreshToken;

    @BeforeMethod
    public void setUp() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        LoginResponse response = given()
                .body(REQUEST_AUTH)
                .when()
                .post("auth/login")
                .then()
                .extract().as(LoginResponse.class);

        authToken = response.getAccess_token();
        refreshToken = response.getRefresh_token();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .post("auth/logout");
        authToken = null;
    }

    @Test
    public void login() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        LoginResponse response = given()
                .body(REQUEST_AUTH)
                .when()
                .post("auth/login")
                .then()
                .body("$", hasKey("access_token"))
                .body("any { it.key == 'access_token' }", is(notNullValue()))
                .extract().as(LoginResponse.class);

        assertEquals(response.getId(), 2L);
        assertEquals(response.getUsername(), "user@mail.com");
        assertEquals(response.getRoles().get(0), "ROLE_USER");
        assertNotNull(response.getAccess_token());
        assertNotNull(response.getRefresh_token());
    }

    @Test
    public void login_whenWrongUsername_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        JSONObject request = new JSONObject(Map.of(
                "username", "wrong_user",
                "password", "password"
        ));

        Response response = given()
                .body(request)
                .when()
                .post("auth/login")
                .then()
                .extract().response();

        String expectedErrorMessage = "User Not Found with username: wrong_user";
        String expectedErrorCode = "10403";

        assertEquals(response.jsonPath().getString("errorMessage"), expectedErrorMessage);
        assertEquals(response.jsonPath().getString("errorCode"), expectedErrorCode);
    }

    @Test
    public void login_whenWrongPassword_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        LoginRequest request = new LoginRequest("user@mail.com", "wrong_password");

        ErrorResponse response = given()
                .body(request)
                .when()
                .post("auth/login")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(), "Bad credentials");
        assertEquals(response.getErrorCode(), 10402);
    }

    @Test
    public void logout() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .post("auth/logout")
                .then()
                .log().ifError();
    }

    @Test
    public void getInfo() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        User response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .get("auth/me")
                .then()
                .extract().as(User.class);

        assertEquals(response.getId(), 2L);
        assertEquals(response.getUsername(), "user@mail.com");
        assertEquals(response.getRoles().get(0), "ROLE_USER");
        assertEquals(response.getProfile().getName(), "User");
        assertEquals(response.getProfile().getGender(), "MALE");
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
    public void register_whenExistedUser_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(409));

        ErrorResponse response = given()
                .header("Content-Type", ContentType.JSON)
                .body(REQUEST_AUTH)
                .when()
                .post("auth/register")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(),
                "Requested resource already exists (username = user@mail.com)");
        assertEquals(response.getErrorCode(), 40903);
    }

    @Test
    public void changePassword() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(202));

        UpdatePasswordRequest request = new UpdatePasswordRequest(2L, "password");

        given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .body(request)
                .when()
                .patch("auth/password");
    }

    @Test
    public void changePassword_whenAnotherId_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(409));

        UpdatePasswordRequest request = new UpdatePasswordRequest(3L, "password");

        ErrorResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .body(request)
                .when()
                .patch("auth/password")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(), "User id (3) belongs to another user");
        assertEquals(response.getErrorCode(), 41003);
    }

    @Test
    public void updateAvatar() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(202));

        File file = new File("src/test/resources/avatar.png");

        MultiPartSpecification multiPartFile = new MultiPartSpecBuilder(file)
                .fileName("avatar.png")
                .controlName("avatar")
                .mimeType("image/png")
                .build();

        UpdateAvatarResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Accept", ContentType.JSON)
                .formParam("id", "2")
                .multiPart(multiPartFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("auth/avatar")
                .then()
                .log().all()
                .extract().as(UpdateAvatarResponse.class);

        assertEquals(response.getAvatar(), "de46dd50f165c6514e2ef4b5d477bc19.png");
    }

    @Test
    public void updateAvatar_whenAnotherId_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(409));

        File file = new File("src/test/resources/avatar.png");

        MultiPartSpecification multiPartFile = new MultiPartSpecBuilder(file)
                .fileName("avatar.png")
                .controlName("avatar")
                .mimeType("image/png")
                .build();

        ErrorResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Accept", ContentType.JSON)
                .formParam("id", "3")
                .multiPart(multiPartFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("auth/avatar")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(), "User id (3) belongs to another user");
        assertEquals(response.getErrorCode(), 41003);
    }

    @Test
    public void refreshToken() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        TokenRefreshRequest request = new TokenRefreshRequest(refreshToken);

        RefreshTokenResponse response = given()
                .body(request)
                .when()
                .post("auth/refreshtoken")
                .then()
                .extract().as(RefreshTokenResponse.class);

        assertFalse(response.getAccess_token().isEmpty());
        assertEquals(response.getRefresh_token(), refreshToken);
    }

    @Test
    public void getUserById() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        User response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .get("users/2")
                .then()
                .extract().as(User.class);

        assertEquals(response.getId(), 2L);
        assertEquals(response.getUsername(), "user@mail.com");
        assertEquals(response.getRoles().get(0), "ROLE_USER");
        assertEquals(response.getProfile().getName(), "User");
        assertEquals(response.getProfile().getGender(), "MALE");
    }

    @Test
    public void getUserByAnotherId() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        User response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .get("users/1")
                .then()
                .extract().as(User.class);

        assertEquals(response.getId(), 1L);
        assertEquals(response.getUsername(), "admin@mail.com");
        assertEquals(response.getRoles().get(0), "ROLE_ADMIN");
        assertEquals(response.getProfile().getName(), "Admin");
        assertEquals(response.getProfile().getGender(), "MALE");
    }

    @Test
    public void getUserById_whenIdNotExists_shouldReturnErrorMessage() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(404));

        ErrorResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .get("users/999999")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(), "Requested resource not found (id = 999999)");
        assertEquals(response.getErrorCode(), 40403);
    }

    @Test
    public void getAllUsers() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        PageableUserResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .when()
                .get("users?page=0&size=1&sort=id,asc")
                .then()
                .extract().as(PageableUserResponse.class);

        User firstUser = response.get_embedded().getUsers()[0];

        assertEquals(firstUser.getId(), 1L);
        assertEquals(firstUser.getUsername(), "admin@mail.com");
        assertEquals(firstUser.getRoles().get(0), "ROLE_ADMIN");
        assertEquals(firstUser.getProfile().getName(), "Admin");
        assertEquals(firstUser.getProfile().getGender(), "MALE");
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
                .role(Set.of("ROLE_USER"))
                .profile(profile)
                .build();

        Response response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .body(request)
                .when()
                .post("users")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }

    @Test
    public void updateUser() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(200));

        Profile profile = Profile.builder()
                .name("User")
                .surname("S")
                .gender("MALE")
                .city("NY")
                .address("address1")
                .company("company1")
                .mobile("3333333333")
                .tele("3333333333")
                .website("http://itermit.com")
                .date("2000-01-01")
                .build();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(2L)
                .role(Set.of("ROLE_USER"))
                .profile(profile)
                .build();

        User response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .body(request)
                .when()
                .patch("users")
                .then()
                .extract().as(User.class);

        assertEquals(response.getId(), 2L);
        assertEquals(response.getUsername(), "user@mail.com");
        assertEquals(response.getRoles().get(0), "ROLE_USER");
        assertEquals(response.getProfile().getName(), "User");
        assertEquals(response.getProfile().getSurname(), "S");
        assertEquals(response.getProfile().getGender(), "MALE");
        assertEquals(response.getProfile().getCity(), "NY");
        assertEquals(response.getProfile().getMobile(), "3333333333");
        assertEquals(response.getProfile().getTele(), "3333333333");
        assertEquals(response.getProfile().getWebsite(), "http://itermit.com");
        assertEquals(response.getProfile().getDate(), "2000-01-01");
    }

    @Test
    public void updateUser_whenUpdateAnotherUser_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(409));

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
                .role(Set.of("ROLE_USER"))
                .profile(profile)
                .build();

        ErrorResponse response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .body(request)
                .when()
                .patch("users")
                .then()
                .extract().as(ErrorResponse.class);

        assertEquals(response.getErrorMessage(), "User id (3) belongs to another user");
        assertEquals(response.getErrorCode(), 41003);
    }

    @Test
    public void deleteUserById_shouldNotAuthorize() {
        getSpecifications(requestSpecification(BASE_URL), responseSpecification(401));

        Response response = given()
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
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
                .headers(
                        "Authorization", "Bearer " + authToken,
                        "Accept", ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .multiPart(multiPartFile)
                .when()
                .post("articles/saveImage")
                .then()
                .extract().response();

        assertEquals(response.jsonPath().getString("error"), "Unauthenticated");
    }
}
