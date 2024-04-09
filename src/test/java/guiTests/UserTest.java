package guiTests;

import com.codeborne.selenide.Selenide;
import gui.config.BaseTest;
import gui.pojos.Article;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static gui.Pages.*;

@Ignore
public class UserTest extends BaseTest {

    private final String USERNAME = "user@mail.com";
    private final String PASSWORD = "password";

    @BeforeMethod(onlyForGroups = {"needLogin"}, alwaysRun = true)
    public void setUp(Method method) {
        Selenide.open(LOGIN_URL);

        loginPage().typeUsername(USERNAME);
        loginPage().typePassword(PASSWORD);
        loginPage().clickLoginButton();
    }

    @AfterMethod(onlyForGroups = {"needLogin"}, alwaysRun = true)
    public void tearDown(Method method) {
        header().clickCurrentUserButton();
        header().clickLogoutButton();
    }

    /**
     * LOGIN
     */

    @Test(groups = {"needLogin"})
    public void login() {
        Assert.assertEquals(header().getCurrentUserText(), USERNAME);
        Assert.assertFalse(header().isAdminPanelButtonExists());
    }

    /**
     * NEWS
     */

    @Test(groups = {"needLogin"})
    public void news() {
        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickShowMoreButton_shouldListMoreNews() {
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();
        Assert.assertEquals(articles.size(), 2);

        mainPage().clickShowMoreButton();

        mainPage().waitForLoad();

        articles = mainPage().getArticlesFromMain();
        System.out.println(articles.get(0));

        Assert.assertEquals(articles.size(), 4);
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickArticle_shouldShowDetailedArticle() {
        mainPage().getArticleElementsFromMain().get(1).click();

        Article article = articlePage().getArticle();

        Assert.assertFalse(article.getPublishDate().isEmpty());
        Assert.assertFalse(article.getTitle().isEmpty());
        Assert.assertTrue(article.getAuthor().startsWith("Author: "));
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickBackButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        articlePage().clickBackButton();
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();

        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
        Assert.assertEquals(articles.size(), 2);
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickHomeButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        header().clickHomeButton();
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();

        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
        Assert.assertEquals(articles.size(), 2);
    }

    /**
     * PROFILE
     */

    @Test(groups = {"needLogin"}, priority = 9)
    public void profile_shouldNavigateAndDisplayPage() {
        header().clickCurrentUserButton();
        header().clickProfileEditButton();
        profilePage().waitForLoad();

        Map<String, String> inputs = profilePage().getInputsMap();

        Assert.assertEquals(profilePage().getEditProfileTitleText(), "Edit Profile");
        Assert.assertEquals(profilePage().getAdditionalSettingsTitleText(), "Additional settings");
        Assert.assertEquals(inputs.get("name"), "User");
        Assert.assertEquals(inputs.get("mobile"), "3333333333");
    }

    @Test(groups = {"needLogin"}, priority = 9, enabled = true)
    public void profile_whenEditProfile_shouldSaveChanges() {
        header().clickCurrentUserButton();
        header().clickProfileEditButton();
        profilePage().waitForLoad();

        /* change */
        profilePage().setInputNameValue("New name");
        profilePage().setInputSurnameValue("New surname");
        profilePage().setInputGenderFemale();
        profilePage().setInputCityValue("City1");
        profilePage().setInputCompanyValue("Company1");
        profilePage().setInputAddressValue("Address1");
        profilePage().setInputMobileValue("3333333333");
        profilePage().setInputTeleValue("5555555555");
        profilePage().setInputWebsiteValue("wwww");
        profilePage().setInputDateValue("2024-02-21");

        profilePage().clickSaveButton();
        Selenide.sleep(50);
        Selenide.refresh();
        Selenide.sleep(50);

        Assert.assertEquals(profilePage().getInputNameValue(), "New name");
        Assert.assertEquals(profilePage().getInputSurnameValue(), "New surname");
        Assert.assertEquals(profilePage().getInputGenderValue(), "Female");
        Assert.assertEquals(profilePage().getInputCityValue(), "City1");
        Assert.assertEquals(profilePage().getInputAddressValue(), "Address1");
        Assert.assertEquals(profilePage().getInputCompanyValue(), "Company1");
        Assert.assertEquals(profilePage().getInputMobileValue(), "3333333333");
        Assert.assertEquals(profilePage().getInputTeleValue(), "5555555555");
        Assert.assertEquals(profilePage().getInputWebsiteValue(), "wwww");
//        Assert.assertEquals(profilePage().getInputDateValue(), "2024-02-21");

        /* rollback */
        profilePage().setInputNameValue("User");
        profilePage().setInputSurnameValue("");
        profilePage().setInputGenderMale();
        profilePage().setInputCityValue("");
        profilePage().setInputCompanyValue("");
        profilePage().setInputAddressValue("");
        profilePage().setInputMobileValue("3333333333");
        profilePage().setInputTeleValue("");
        profilePage().setInputWebsiteValue("");
        profilePage().setInputDateValue("");

        profilePage().clickSaveButton();
        Selenide.sleep(50);
        Selenide.refresh();
        Selenide.sleep(50);

        Assert.assertEquals(profilePage().getInputNameValue(), "User");
        Assert.assertEquals(profilePage().getInputSurnameValue(), "");
        Assert.assertEquals(profilePage().getInputGenderValue(), "Male");
        Assert.assertEquals(profilePage().getInputCityValue(), "");
        Assert.assertEquals(profilePage().getInputAddressValue(), "");
        Assert.assertEquals(profilePage().getInputCompanyValue(), "");
        Assert.assertEquals(profilePage().getInputMobileValue(), "3333333333");
        Assert.assertEquals(profilePage().getInputTeleValue(), "");
        Assert.assertEquals(profilePage().getInputWebsiteValue(), "");
        Assert.assertEquals(profilePage().getInputDateValue(), "");
    }

    @Test(groups = {"needLogin"}, priority = 9, enabled = true)
    public void profile_shouldChangePassword() {
        header().clickCurrentUserButton();
        header().clickProfileEditButton();
        profilePage().waitForLoad();
        profilePage().clickChangePasswordButton();
        Selenide.sleep(50);

        Assert.assertEquals(changePasswordPage().getTitleText(), "Change password");

        changePasswordPage().setPassword("password");
        changePasswordPage().setConfirmPassword("password");

        Selenide.sleep(100);

        changePasswordPage().clickSubmitButton();
    }

    @Test(groups = {"needLogin"}, priority = 9)
    public void profile_whenPasswordMismatch_shouldDisableSubmit() {
        header().clickCurrentUserButton();
        header().clickProfileEditButton();
        profilePage().waitForLoad();
        profilePage().clickChangePasswordButton();
        Selenide.sleep(30);

        changePasswordPage().setPassword("password");
        changePasswordPage().setConfirmPassword("password1");

        Assert.assertFalse(changePasswordPage().checkSubmitButtonEnabled());

        changePasswordPage().clickCloseButton();
    }

    @Test(groups = {"needLogin"}, description = "not implemented yet", priority = 9)
    public void profile_shouldChangeAvatar() {
        header().clickCurrentUserButton();
        header().clickProfileEditButton();
        profilePage().waitForLoad();
        profilePage().clickChangeAvatarButton();
        changeAvatarPage().waitForLoad();

//        changeAvatarPage().clickChooseAvatarButton();
//        changeAvatarPage().setFile();
        Assert.assertEquals(changeAvatarPage().getTitleText(), "Choose your avatar image");

        changeAvatarPage().clickCloseButton();
    }
}
