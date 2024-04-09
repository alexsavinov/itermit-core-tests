package gui.config;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.PropertiesReader;

public class BaseTest {

    public static String LOGIN_URL;
    public static String REGISTER_URL;
    public static String HOME_URL;

    @BeforeClass(alwaysRun = true)
    public void configuration() {
        PropertiesReader reader = new PropertiesReader("application.properties");
        String baseUrl = reader.getProperty("GUI_BASE_URL");
        LOGIN_URL = baseUrl + "auth/login";
        REGISTER_URL = baseUrl + "auth/register";
        HOME_URL = baseUrl;

//        Configuration.webdriverLogsEnabled = true;
//        Configuration.browser = "chrome";
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x920";
        Configuration.holdBrowserOpen = false;
        Configuration.headless = true;
//        Configuration.headless = false;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 10000;
        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @BeforeMethod
    public void setUp() {

    }

    @AfterMethod(alwaysRun = true)
    public void cleanWebDrive() {
        Selenide.clearBrowserCookies();
        Selenide.refresh();
        Selenide.open("about:blank");
    }

    @AfterClass
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
