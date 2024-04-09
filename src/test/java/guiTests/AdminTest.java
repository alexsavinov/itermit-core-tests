package guiTests;

import com.codeborne.selenide.Selenide;
import gui.config.BaseTest;
import gui.pojos.Article;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gui.Pages.*;

@Ignore
public class AdminTest extends BaseTest {

    private final String USERNAME = "admin@mail.com";
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
        Assert.assertTrue(header().isAdminPanelButtonExists());
    }

    @Test()
    public void login_whenUsernameIncorrect_shouldDisplayValidationMessage() {
        Selenide.open(LOGIN_URL);

        loginPage().typeUsername("Incorrect_username");
        loginPage().typePassword(PASSWORD);
        loginPage().clickLoginButton();

        Assert.assertEquals(loginPage().getUsernameErrorText(), "Username not found");
    }

    @Test()
    public void login_whenPasswordIncorrect_shouldDisplayValidationMessage() {
        Selenide.open(LOGIN_URL);

        loginPage().typeUsername(USERNAME);
        loginPage().typePassword("Incorrect_password");
        loginPage().clickLoginButton();

        Assert.assertEquals(loginPage().getPasswordErrorText(), "Password incorrect");
    }

    /**
     * REGISTER
     */

    @Test(priority = 9)
    public void register() {
        Selenide.open(REGISTER_URL);

        Assert.assertFalse(registerPage().isRegisterButtonEnabled());

        registerPage().typeUsername(UUID.randomUUID() + "@mail.com");
        registerPage().typePassword("pass");
        registerPage().typeConfirmPassword("pass");
        registerPage().clickReadAndAgreeCheckbox();
        registerPage().focus();

        Assert.assertTrue(registerPage().isRegisterButtonEnabled());

        registerPage().clickRegisterButton();
        loginPage().waitForLoad();

        Assert.assertEquals(loginPage().getLoginTitleText(), "Welcome Back!");
    }

    @Test()
    public void register_whenUserExists_thenDisplayErrorMessage() {
        Selenide.open(REGISTER_URL);

        registerPage().typeUsername(USERNAME);
        registerPage().typePassword("pass");
        registerPage().typeConfirmPassword("pass");
        registerPage().clickReadAndAgreeCheckbox();
        registerPage().focus();
        registerPage().clickRegisterButton();

        Assert.assertFalse(registerPage().isRegisterButtonEnabled());
        Assert.assertEquals(registerPage().getToastContainerText(),
                "Requested resource already exists (username = admin@mail.com)");
    }

    @Test()
    public void register_whenEmptyInputs_thenDisplayErrorMessage() {
        Selenide.open(REGISTER_URL);

        registerPage().typeUsername("");
        registerPage().typePassword("");
        registerPage().typeConfirmPassword("");
        registerPage().focus();

        Assert.assertEquals(registerPage().getUsernameErrorText(), "This field is required");
        Assert.assertEquals(registerPage().getPasswordErrorText(), "This field is required");
        Assert.assertEquals(registerPage().getConfirmPasswordErrorText(), "This field is required");
    }

    @Test()
    public void register_whenIncorrectInputs_thenDisplayErrorMessage() {
        Selenide.open(REGISTER_URL);

        registerPage().typeUsername("test");
        registerPage().typePassword("1");
        registerPage().typeConfirmPassword("1");
        registerPage().focus();

        Assert.assertEquals(registerPage().getUsernameErrorText(), "Invalid email");
        Assert.assertEquals(registerPage().getPasswordErrorText(), "Should have at least 3 characters");
        Assert.assertEquals(registerPage().getConfirmPasswordErrorText(), "Should have at least 3 characters");

        registerPage().typePassword("111");
        registerPage().typeConfirmPassword("222");
        registerPage().focus();

        Assert.assertEquals(registerPage().getConfirmPasswordErrorText(), "Inconsistent with field password");
    }

    /**
     * COMMON FUNCTIONALITY
     */

    @Test()
    public void toggleTheme() {
        Selenide.open(HOME_URL);

        Assert.assertEquals(mainPage().getHtmlElementClass(), "theme-dark");

        header().clickToggleThemeButton();

        Assert.assertEquals(mainPage().getHtmlElementClass(), "");

        header().clickToggleThemeButton();

        Assert.assertEquals(mainPage().getHtmlElementClass(), "theme-dark");
    }

    @Test(description = "when toggle fullscreen mode should change browser windows size")
    public void toggleFullscreen() {
        Selenide.open(HOME_URL);

        int initHeight = mainPage().getWindowSize().getHeight();

        mainPage().setWindowSize(1300, 1000);
        header().clickToggleFullscreenButton();

        int actualHeight = mainPage().getWindowSize().getHeight();

        Assert.assertTrue(actualHeight > initHeight);

        mainPage().setWindowSize(1280, 920);
        header().clickToggleFullscreenButton();

        actualHeight = mainPage().getWindowSize().getHeight();

        Assert.assertEquals(actualHeight, initHeight);
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
//        Assert.assertEquals(articles.get(0).getPublishDate(), "Jan 17, 2024");
//        Assert.assertEquals(articles.get(0).getTitle(), "title10");
//        String[] logoPath = articles.get(0).getLogo().split("/");
//        Assert.assertEquals(logoPath[logoPath.length - 1], "10.jpg");
//        Assert.assertEquals(articles.get(0).getDescription(), "description10");
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickArticle_shouldShowDetailedArticle() {
        mainPage().getArticleElementsFromMain().get(1).click();
        articlePage().waitForLoad();

        Article article = articlePage().getArticle();

        Assert.assertFalse(article.getPublishDate().isEmpty());
        Assert.assertFalse(article.getTitle().isEmpty());
        Assert.assertTrue(article.getAuthor().startsWith("Author: "));
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickBackButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        articlePage().waitForLoad();
        articlePage().clickBackButton();
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();

        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
        Assert.assertEquals(articles.size(), 2);
    }

    @Test(groups = {"needLogin"})
    public void news_whenClickHomeButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        articlePage().waitForLoad();
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
        Assert.assertEquals(inputs.get("name"), "Admin");
        Assert.assertEquals(inputs.get("mobile"), "2222222222");
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
        Selenide.sleep(30);
        Selenide.refresh();
        Selenide.sleep(30);

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
        profilePage().setInputNameValue("Admin");
        profilePage().setInputSurnameValue("");
        profilePage().setInputGenderMale();
        profilePage().setInputCityValue("");
        profilePage().setInputCompanyValue("");
        profilePage().setInputAddressValue("");
        profilePage().setInputMobileValue("2222222222");
        profilePage().setInputTeleValue("");
        profilePage().setInputWebsiteValue("");
        profilePage().setInputDateValue("");

        profilePage().clickSaveButton();
        Selenide.sleep(30);
        Selenide.refresh();
        Selenide.sleep(30);

        Assert.assertEquals(profilePage().getInputNameValue(), "Admin");
        Assert.assertEquals(profilePage().getInputSurnameValue(), "");
        Assert.assertEquals(profilePage().getInputGenderValue(), "Male");
        Assert.assertEquals(profilePage().getInputCityValue(), "");
        Assert.assertEquals(profilePage().getInputAddressValue(), "");
        Assert.assertEquals(profilePage().getInputCompanyValue(), "");
        Assert.assertEquals(profilePage().getInputMobileValue(), "2222222222");
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

        changePasswordPage().setPassword("password");
        changePasswordPage().setConfirmPassword("password");

        Assert.assertEquals(changePasswordPage().getTitleText(), "Change password");
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

    /**
     * ADMIN PANEL - USERS
     */

    @Test(groups = {"needLogin"}, description = "Should navigate by menu items")
    public void adminPanel() {
        header().clickAdminPanelButton();

        adminPanelPage().clickMenuItemDashboard();

        List<String> menuItemsText = adminPanelPage().getMenuItemsText();

        Assert.assertEquals(menuItemsText.get(0), "Dashboard");
        Assert.assertEquals(menuItemsText.get(1), "Users");
        Assert.assertEquals(menuItemsText.get(2), "News");

        Assert.assertEquals(adminPanelPage().getPageTitleText(), "Dashboard");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(0), "Admin");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(1), "Dashboard");

        adminPanelPage().clickMenuItemUsers();
        Selenide.sleep(50);

        Assert.assertEquals(adminPanelPage().getPageTitleText(), "Users");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(0), "Admin");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(1), "Users");

        adminPanelPage().clickMenuItemNews();

        Assert.assertEquals(adminPanelPage().getPageTitleText(), "News");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(0), "Admin");
        Assert.assertEquals(adminPanelPage().getBreadcrumbText().get(1), "News");
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldListUsers() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminPanelPage().waitForLoad();

        List<String> headText = adminUsersListPage().getTableHeadText();
        String[][] table = adminUsersListPage().getTable();

        Assert.assertEquals(headText.get(0), "id");
        Assert.assertEquals(headText.get(1), "avatar");
        Assert.assertEquals(headText.get(2), "username");
        Assert.assertEquals(headText.get(3), "name");
        Assert.assertEquals(headText.get(4), "roles");
        Assert.assertEquals(headText.get(5), "created");
        Assert.assertEquals(headText.get(6), "updated");
        Assert.assertEquals(headText.get(7), "Operation");

        Assert.assertEquals(table.length, 10);
        Assert.assertEquals(table[0].length, 8);

        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[0][1], "");
        Assert.assertEquals(table[0][2], "admin@mail.com");
        Assert.assertEquals(table[0][3], "Admin");
        Assert.assertEquals(table[0][4], "ROLE_ADMIN");
        Assert.assertEquals(table[1][5], "2024-01-10 16:35");
        Assert.assertFalse(table[1][6].isEmpty());
        Assert.assertEquals(table[2][0], "3");
        Assert.assertEquals(table[2][2], "user_to_update@mail.com");
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldPagedListUsers() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().waitForLoad();

        Assert.assertEquals(adminUsersListPage().getPaginatorSelectValue(), "10");

        adminUsersListPage().clickPaginatorSelect();
        adminUsersListPage().clickPaginatorSelectPageSize1();

        Selenide.sleep(100);

        String[][] table = adminUsersListPage().getTable();

        Assert.assertEquals(table.length, 1);
        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[0][1], "");
        Assert.assertEquals(table[0][2], "admin@mail.com");
        Assert.assertEquals(table[0][3], "Admin");
        Assert.assertEquals(table[0][4], "ROLE_ADMIN");
        Assert.assertEquals(table[0][5], "2024-01-10 16:35");
        Assert.assertFalse(table[0][6].isEmpty());

        /* rollback */
        adminUsersListPage().clickPaginatorSelect();
        adminUsersListPage().clickPaginatorSelectPageSize5();
        adminUsersListPage().waitForLoad();

        Assert.assertEquals(adminUsersListPage().getTable().length, 5);
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldSortedListUsers() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();

        /* descending sort by Id */
        adminUsersListPage().clickSortById();

        String[][] table = adminUsersListPage().getTable();

        Assert.assertEquals(table.length, 10);
        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[1][0], "2");
        Assert.assertEquals(table[2][0], "3");
        Assert.assertEquals(table[0][3], "Admin");
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldSearchListUsers() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();

        Assert.assertEquals(adminUsersListPage().getTable().length, 10);

        adminUsersListPage().typeSearchInput("Admin");

        Selenide.sleep(1000);
        adminUsersListPage().waitForLoad();

        String[][] table = adminUsersListPage().getTable();

        Assert.assertEquals(table.length, 1);
        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[0][3], "Admin");

        adminUsersListPage().clickResetButton();
        adminUsersListPage().waitForLoad();

        Assert.assertEquals(adminUsersListPage().getTable().length, 10);
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenDeleteWhileListUsers_shouldCancel() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().waitForLoad();

        Assert.assertEquals(adminUsersListPage().getTable().length, 10);

        adminUsersListPage().clickDeleteButton();
        adminUsersListPage().clickDeleteCloseButton();

        Assert.assertEquals(adminUsersListPage().getTable().length, 10);
    }

    @Test(groups = {"needLogin"}, priority = 8, enabled = true)
    public void adminPanel_whenDeleteInListUsers_shouldDelete() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();

        /* create user for deleting */
        adminUsersListPage().clickCreateButton();

        adminUsersCreatePage().waitForLoad();

        String username = UUID.randomUUID().toString();

        adminUsersCreatePage().setInputUsernameValue(username + "@mail.com");
        adminUsersCreatePage().setInputPasswordValue("testpass");
        adminUsersCreatePage().setInputConfirmPasswordValue("testpass");
        adminUsersCreatePage().setInputNameValue(username);
        adminUsersCreatePage().clickCreateButton();

        Selenide.sleep(100);

        adminUsersListPage().waitForLoad();
        adminUsersListPage().clickSortById();
        adminUsersListPage().clickSortById();

        /* deleting user */
        adminUsersListPage().clickDeleteButton();
        adminUsersListPage().clickDeleteOkButton();

        adminUsersListPage().waitForLoad();

        Assert.assertTrue(adminUsersEditPage().getToastContainerText().startsWith("You have deleted user with id"));
    }

    @Test(groups = {"needLogin"}, priority = 9)
    public void adminPanel_shouldCreateUser() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickCreateButton();

        adminUsersCreatePage().waitForLoad();

        String username = UUID.randomUUID().toString();

        adminUsersCreatePage().setInputUsernameValue(username + "@mail.com");
        adminUsersCreatePage().setInputPasswordValue("testpass");
        adminUsersCreatePage().setInputConfirmPasswordValue("testpass");
        adminUsersCreatePage().setInputNameValue(username);
        adminUsersCreatePage().setInputSurnameValue("user test surname");
        adminUsersCreatePage().setInputGenderFemale();
        adminUsersCreatePage().setInputCityValue("City 333");
        adminUsersCreatePage().setInputAddressValue("Address 333");
        adminUsersCreatePage().setInputCompanyValue("Company 333");
        adminUsersCreatePage().setInputMobileValue("1234555555");
        adminUsersCreatePage().setInputTeleValue("5432111111");
        adminUsersCreatePage().setInputWebsiteValue("https://test.com");
        adminUsersCreatePage().setInputDateValue("2000-01-01");

        Assert.assertEquals(adminUsersCreatePage().getCreateUserTitleText(), "Create user");

        adminUsersCreatePage().clickCreateButton();
        adminUsersListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenIncorrectInputOnCreateUser_shouldDisplayValidationErrors() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickCreateButton();
        adminUsersCreatePage().waitForLoad();

        /* Username */
        adminUsersCreatePage().setInputUsernameValue("");
        Assert.assertEquals(adminUsersCreatePage().getErrorUsernameText(), "This field is required");

        adminUsersCreatePage().setInputUsernameValue("wrong user");
        Assert.assertEquals(adminUsersCreatePage().getErrorUsernameText(), "Invalid email");

        /* Password */
        adminUsersCreatePage().setInputPasswordValue("");
        Assert.assertEquals(adminUsersCreatePage().getErrorPasswordText(), "This field is required");

        adminUsersCreatePage().setInputConfirmPasswordValue("");
        Assert.assertEquals(adminUsersCreatePage().getErrorConfirmPasswordText(), "This field is required");

        adminUsersCreatePage().setInputPasswordValue("11");
        Assert.assertEquals(adminUsersCreatePage().getErrorPasswordText(),
                "Should have at least 3 characters");

        adminUsersCreatePage().setInputPasswordValue("11121212121212121212121");
        Assert.assertEquals(adminUsersCreatePage().getErrorPasswordText(),
                "This value should be less than 20 characters");

        adminUsersCreatePage().setInputConfirmPasswordValue("11");
        Assert.assertEquals(adminUsersCreatePage().getErrorConfirmPasswordText(),
                "Should have at least 3 characters");

        adminUsersCreatePage().setInputConfirmPasswordValue("11121212121212121212121");
        Assert.assertEquals(adminUsersCreatePage().getErrorConfirmPasswordText(),
                "This value should be less than 20 characters");

        adminUsersCreatePage().setInputPasswordValue("111");
        adminUsersCreatePage().setInputConfirmPasswordValue("222");
        Assert.assertEquals(adminUsersCreatePage().getErrorConfirmPasswordText(),
                "Inconsistent with field password");

        /* Name */
        adminUsersCreatePage().setInputNameValue("");
        Assert.assertEquals(adminUsersCreatePage().getErrorNameText(), "This field is required");

        /* Mobile */
        adminUsersCreatePage().setInputMobileValue("111");
        Assert.assertEquals(adminUsersCreatePage().getErrorMobileText(), "Invalid phone");

        /* Tele */
        adminUsersCreatePage().setInputTeleValue("111");
        Assert.assertEquals(adminUsersCreatePage().getErrorTeleText(), "Invalid phone");

        /* Website */
        adminUsersCreatePage().setInputWebsiteValue("wrong-site");
        Assert.assertEquals(adminUsersCreatePage().getErrorWebsiteText(), "Invalid URL");

        Assert.assertFalse(adminUsersCreatePage().isCreateButtonEnabled());
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileCreateUser_shouldDisplayConfirmationDialogAndStay() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickCreateButton();
        adminUsersCreatePage().waitForLoad();

        adminUsersCreatePage().setInputUsernameValue("some user");

        adminUsersCreatePage().clickBackButton();
        adminUsersCreatePage().clickLeaveButton();

        adminUsersListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileCreateUser_shouldDisplayConfirmationDialogAndLeave() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickCreateButton();
        adminUsersCreatePage().waitForLoad();

        adminUsersCreatePage().setInputUsernameValue("some user");

        adminUsersCreatePage().clickBackButton();
        adminUsersCreatePage().clickStayBackButton();

        adminUsersCreatePage().waitForLoad();
    }

    @Test(groups = {"needLogin"}, priority = 8)
    public void adminPanel_shouldEditUser() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();

        adminUsersEditPage().waitForLoad();

        Assert.assertEquals(adminUsersEditPage().getEditUserTitleText(), "Edit user");
        Assert.assertFalse(adminUsersEditPage().isEditButtonEnabled());

        adminUsersEditPage().setInputNameValue("new username");
        adminUsersEditPage().setInputSurnameValue("user test surname");
        adminUsersEditPage().setInputGenderFemale();
        adminUsersEditPage().setInputCityValue("City 333");
        adminUsersEditPage().setInputAddressValue("Address 333");
        adminUsersEditPage().setInputCompanyValue("Company 333");
        adminUsersEditPage().setInputMobileValue("1234555555");
        adminUsersEditPage().setInputTeleValue("5432111111");
        adminUsersEditPage().setInputWebsiteValue("https://test.com");
        adminUsersEditPage().setInputDateValue("2000-01-01");

        Assert.assertEquals(adminUsersEditPage().getEditUserTitleText(), "Edit user *");
        Assert.assertTrue(adminUsersEditPage().isEditButtonEnabled());

        adminUsersEditPage().clickEditButton();
        Selenide.sleep(50);

        Assert.assertEquals(adminUsersEditPage().getToastContainerText(),
                "User user_to_update@mail.com updated!");

        Selenide.refresh();

        Assert.assertEquals(adminUsersEditPage().getInputNameValue(), "new username");
        Assert.assertEquals(adminUsersEditPage().getInputSurnameValue(), "user test surname");
        Assert.assertEquals(adminUsersEditPage().getInputGenderValue(), "Female");
        Assert.assertEquals(adminUsersEditPage().getInputCityValue(), "City 333");
        Assert.assertEquals(adminUsersEditPage().getInputAddressValue(), "Address 333");
        Assert.assertEquals(adminUsersEditPage().getInputCompanyValue(), "Company 333");
        Assert.assertEquals(adminUsersEditPage().getInputMobileValue(), "+38 (123) 455-5555");
        Assert.assertEquals(adminUsersEditPage().getInputTeleValue(), "+38 (543) 211-1111");
        Assert.assertEquals(adminUsersEditPage().getInputWebsiteValue(), "https://test.com");
        Assert.assertEquals(adminUsersEditPage().getInputDateValue(), "2000-01-01");

        adminUsersEditPage().clickBackButton();

        adminUsersListPage().waitForLoad();
    }


    @Test(groups = {"needLogin"}, priority = 9, enabled = true)
    public void adminPanel_whenClickChangePasswordWhileEditUser_shouldChangePassword() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();
        adminUsersEditPage().clickChangePasswordButton();

        changePasswordPage().setPassword("password");
        changePasswordPage().setConfirmPassword("password");

        Assert.assertEquals(changePasswordPage().getTitleText(), "Change password");

        changePasswordPage().clickSubmitButton();
    }

    @Test(groups = {"needLogin"}, priority = 9)
    public void adminPanel_whenPasswordMismatchWhileEditUser_shouldDisableSubmit() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();
        adminUsersEditPage().clickChangePasswordButton();

        changePasswordPage().setPassword("password");
        changePasswordPage().setConfirmPassword("password1");

        Assert.assertFalse(changePasswordPage().checkSubmitButtonEnabled());

        changePasswordPage().clickCloseButton();
    }

    @Test(groups = {"needLogin"}, description = "not implemented yet", priority = 9)
    public void adminPanel_whenClickChangeAvatarWhileEditUser_shouldChangeAvatar() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();
        adminUsersEditPage().clickChangeAvatarButton();
        changeAvatarPage().waitForLoad();

//        changeAvatarPage().clickChooseAvatarButton();
//        changeAvatarPage().setFile();
        Assert.assertEquals(changeAvatarPage().getTitleText(), "Choose your avatar image");

        changeAvatarPage().clickCloseButton();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenIncorrectInputWhileEditUser_shouldDisplayValidationErrors() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();

        /* Name */
        adminUsersEditPage().setInputNameValue("");
        Assert.assertEquals(adminUsersEditPage().getErrorNameText(), "This field is required");

        /* Mobile */
        adminUsersEditPage().setInputMobileValue("111");
        Assert.assertEquals(adminUsersEditPage().getErrorMobileText(), "Invalid phone");

        /* Tele */
        adminUsersEditPage().setInputTeleValue("111");
        Assert.assertEquals(adminUsersEditPage().getErrorTeleText(), "Invalid phone");

        /* Website */
        adminUsersEditPage().setInputWebsiteValue("wrong-site");
        Assert.assertEquals(adminUsersEditPage().getErrorWebsiteText(), "Invalid URL");

        Assert.assertFalse(adminUsersEditPage().isEditButtonEnabled());
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileEditUser_shouldDisplayConfirmationDialogAndStay() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();

        adminUsersEditPage().setInputNameValue("some user");

        adminUsersEditPage().clickBackButton();
        adminUsersEditPage().clickLeaveButton();

        adminUsersListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileEditUser_shouldDisplayConfirmationDialogAndLeave() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemUsers();
        adminUsersListPage().clickEditButton();
        adminUsersEditPage().waitForLoad();

        adminUsersEditPage().setInputNameValue("some user");

        adminUsersEditPage().clickBackButton();
        adminUsersEditPage().clickStayBackButton();

        adminUsersEditPage().waitForLoad();
    }

    /**
     * ADMIN PANEL - NEWS
     */

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldListNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminPanelPage().waitForLoad();

        List<String> headText = adminNewsListPage().getTableHeadText();
        String[][] table = adminNewsListPage().getTable();

        Assert.assertEquals(headText.get(0), "id");
        Assert.assertEquals(headText.get(1), "logo");
        Assert.assertEquals(headText.get(2), "title");
        Assert.assertEquals(headText.get(3), "visible");
        Assert.assertEquals(headText.get(4), "author");
        Assert.assertEquals(headText.get(5), "published");
        Assert.assertEquals(headText.get(6), "created");
        Assert.assertEquals(headText.get(7), "updated");
        Assert.assertEquals(headText.get(8), "Operation");

        Assert.assertEquals(table.length, 10);
        Assert.assertEquals(table[0].length, 9);

        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[0][1], "");
        Assert.assertEquals(table[0][2], "title1");
        Assert.assertEquals(table[0][3], "true");
        Assert.assertEquals(table[0][4], "user@mail.com");
        Assert.assertEquals(table[0][5], "2021-01-17 17:47");
        Assert.assertEquals(table[0][6], "2021-01-17 17:47");
        Assert.assertEquals(table[0][7], "2021-01-23 11:23");
        Assert.assertEquals(table[1][0], "2");
        Assert.assertEquals(table[1][2], "title2");
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldPagedListNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();

        Assert.assertEquals(adminNewsListPage().getPaginatorSelectValue(), "10");
        Selenide.sleep(30);

        adminNewsListPage().clickPaginatorSelect();
        adminNewsListPage().clickPaginatorSelectPageSize1();
        adminNewsListPage().waitForLoad();

        String[][] table = adminNewsListPage().getTable();

        Assert.assertEquals(table.length, 1);
        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[0][1], "");
        Assert.assertEquals(table[0][2], "title1");
        Assert.assertEquals(table[0][3], "true");
        Assert.assertEquals(table[0][4], "user@mail.com");
        Assert.assertEquals(table[0][5], "2021-01-17 17:47");
        Assert.assertEquals(table[0][6], "2021-01-17 17:47");
        Assert.assertEquals(table[0][7], "2021-01-23 11:23");

        /* rollback */
        adminNewsListPage().clickPaginatorSelect();
        adminNewsListPage().clickPaginatorSelectPageSize5();
        adminNewsListPage().waitForLoad();

        Assert.assertEquals(adminNewsListPage().getTable().length, 5);
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldSortedListNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();

        /* descending sort by Id */
        adminNewsListPage().clickSortById();
        adminNewsListPage().clickSortById();
        adminNewsListPage().clickSortById();

        Selenide.sleep(100);

        String[][] table = adminNewsListPage().getTable();

        Assert.assertEquals(table.length, 10);
        Assert.assertEquals(table[0][0], "1");
        Assert.assertEquals(table[1][0], "2");
        Assert.assertEquals(table[0][2], "title1");
        Assert.assertEquals(table[1][2], "title2");
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_shouldSearchListNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();

        Assert.assertEquals(adminNewsListPage().getTable().length, 10);
        Selenide.sleep(30);

        adminNewsListPage().typeSearchInput("title2");

        Selenide.sleep(1000);
        adminNewsListPage().waitForLoad();

        String[][] table = adminUsersListPage().getTable();

        Assert.assertEquals(table.length, 1);
        Assert.assertEquals(table[0][0], "2");
        Assert.assertEquals(table[0][2], "title2");

        adminNewsListPage().clickResetButton();
        adminNewsListPage().waitForLoad();

        Selenide.sleep(50);

        Assert.assertEquals(adminNewsListPage().getTable().length, 10);
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenDeleteWhileListNews_shouldCancel() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();

        Assert.assertEquals(adminNewsListPage().getTable().length, 10);
        Selenide.sleep(30);

        adminNewsListPage().clickDeleteButton();
        adminNewsListPage().clickDeleteCloseButton();

        Assert.assertEquals(adminNewsListPage().getTable().length, 10);
    }

    @Test(groups = {"needLogin"}, priority = 8, enabled = true)
    public void adminPanel_whenDeleteInListNews_shouldDelete() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();

        /* create article for deleting */
        adminNewsListPage().clickCreateButton();
        adminNewsEditCreatePage().waitForLoad();
        Selenide.sleep(100);

        String title = UUID.randomUUID().toString();

        Assert.assertFalse(adminNewsEditCreatePage().isSaveButtonEnabled());
        Selenide.sleep(30);

        adminNewsEditCreatePage().setInputTitleValue(title);

        adminNewsEditCreatePage().clickSaveButton();
        Selenide.sleep(50);
        adminNewsEditCreatePage().clickBackButton();

        adminNewsListPage().waitForLoad();
        adminNewsListPage().clickSortById();
        adminNewsListPage().clickSortById();

        /* deleting article */
        adminNewsListPage().clickDeleteButton();
        adminNewsListPage().clickDeleteOkButton();

        adminNewsListPage().waitForLoad();

        Assert.assertTrue(adminUsersEditPage().getToastContainerText().startsWith("You have deleted article with id"));
    }

    @Test(groups = {"needLogin"}, priority = 9)
    public void adminPanel_shouldCreateNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickCreateButton();
        adminNewsEditCreatePage().waitForLoad();
        Selenide.sleep(100);

        String title = UUID.randomUUID().toString();

        Assert.assertFalse(adminNewsEditCreatePage().isSaveButtonEnabled());
        Selenide.sleep(30);

        adminNewsEditCreatePage().setInputTitleValue(title);
        adminNewsEditCreatePage().setInputDescriptionValue("some description");
        adminNewsEditCreatePage().setInputContentValue("some content");
        adminNewsEditCreatePage().setInputPublishDateValue("");
        adminNewsEditCreatePage().setInputPublishDateValue("2000-11-22 00:44");
        adminNewsEditCreatePage().toggleInputVisibleValue();

        Assert.assertTrue(adminNewsEditCreatePage().isSaveButtonEnabled());
        Assert.assertEquals(adminNewsEditCreatePage().getArticleTitleText(), "Create article*");

        adminNewsEditCreatePage().clickSaveButton();
        Selenide.sleep(50);

        Assert.assertEquals(adminNewsEditCreatePage().getToastContainerText(), "Article created!");

        Selenide.refresh();

        Assert.assertEquals(adminNewsEditCreatePage().getArticleTitleText(), "Edit article");
        Assert.assertEquals(adminNewsEditCreatePage().getInputTitleValue(), title);
        Assert.assertEquals(adminNewsEditCreatePage().getInputAuthorValue(), "admin@mail.com");
        Assert.assertEquals(adminNewsEditCreatePage().getInputDescriptionValue(), "some description");
        Assert.assertEquals(adminNewsEditCreatePage().getInputContentValue(), "some content");
        Assert.assertEquals(adminNewsEditCreatePage().getInputPublishDateValue(), "2000-11-22 00:44");
        Assert.assertTrue(adminNewsEditCreatePage().getInputVisibleValue());
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenIncorrectInputOnCreateNews_shouldDisplayValidationErrors() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickCreateButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("");

        Assert.assertEquals(adminNewsEditCreatePage().getErrorTitleText(), "This field is required");
        Assert.assertFalse(adminNewsEditCreatePage().isSaveButtonEnabled());
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileCreateNews_shouldDisplayConfirmationDialogAndStay() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickCreateButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("title");

        adminNewsEditCreatePage().clickBackButton();
        adminNewsEditCreatePage().clickLeaveButton();

        adminNewsListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileCreateNews_shouldDisplayConfirmationDialogAndLeave() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickCreateButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("title");

        adminNewsEditCreatePage().clickBackButton();
        adminNewsEditCreatePage().clickStayBackButton();

        adminNewsEditCreatePage().waitForLoad();
    }

    @Test(groups = {"needLogin"}, priority = 8)
    public void adminPanel_shouldEditNews() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickEditButton();
        adminNewsEditCreatePage().waitForLoad();

//        Assert.assertEquals(adminNewsEditCreatePage().getArticleTitleText(), "Edit article");
//        Assert.assertFalse(adminNewsEditCreatePage().isSaveButtonEnabled());

        Selenide.sleep(100);

        adminNewsEditCreatePage().setInputTitleValue("title");
        adminNewsEditCreatePage().setInputDescriptionValue("some description");
        adminNewsEditCreatePage().setInputContentValue("some content");
        adminNewsEditCreatePage().setInputPublishDateValue("");
        adminNewsEditCreatePage().setInputPublishDateValue("2000-11-22 00:44");
        adminNewsEditCreatePage().setInputVisibleFalse();

        Assert.assertEquals(adminNewsEditCreatePage().getArticleTitleText(), "Edit article*");
        Assert.assertTrue(adminNewsEditCreatePage().isSaveButtonEnabled());

        adminNewsEditCreatePage().clickSaveButton();

        Selenide.sleep(50);

        Assert.assertEquals(adminUsersEditPage().getToastContainerText(),
                "Article id 3 updated!");

        Selenide.refresh();

        Assert.assertEquals(adminNewsEditCreatePage().getInputTitleValue(), "title");
        Assert.assertEquals(adminNewsEditCreatePage().getInputAuthorValue(), "user@mail.com");
        Assert.assertEquals(adminNewsEditCreatePage().getInputDescriptionValue(), "some description");
        Assert.assertEquals(adminNewsEditCreatePage().getInputContentValue(), "some content");
        Assert.assertEquals(adminNewsEditCreatePage().getInputPublishDateValue(), "2000-11-22 00:44");
        Assert.assertFalse(adminNewsEditCreatePage().getInputVisibleValue());

        adminNewsEditCreatePage().clickBackButton();

        adminNewsListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenIncorrectInputWhileEditNews_shouldDisplayValidationErrors() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickEditButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("");

        Assert.assertEquals(adminNewsEditCreatePage().getErrorTitleText(), "This field is required");
        Assert.assertFalse(adminNewsEditCreatePage().isSaveButtonEnabled());
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileEditNews_shouldDisplayConfirmationDialogAndStay() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickEditButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("title");

        adminNewsEditCreatePage().clickBackButton();
        adminNewsEditCreatePage().clickLeaveButton();

        adminNewsListPage().waitForLoad();
    }

    @Test(groups = {"needLogin"})
    public void adminPanel_whenFormModifiedWhileEditNews_shouldDisplayConfirmationDialogAndLeave() {
        header().clickAdminPanelButton();
        adminPanelPage().clickMenuItemNews();
        adminNewsListPage().clickEditButton();
        adminNewsEditCreatePage().waitForLoad();

        adminNewsEditCreatePage().setInputTitleValue("title");

        adminNewsEditCreatePage().clickBackButton();
        adminNewsEditCreatePage().clickStayBackButton();

        adminNewsEditCreatePage().waitForLoad();
    }

}
