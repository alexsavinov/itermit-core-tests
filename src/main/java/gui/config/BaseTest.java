package gui.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.webdriver.WebDriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.ChromiumDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.PropertiesReader;

public class BaseTest {

    public static String LOGIN_URL;
    public static String REGISTER_URL;
    public static String HOME_URL;
    public static WebDriver webDriver;

    @BeforeClass(alwaysRun = true)
    public void configuration() {
        PropertiesReader reader = new PropertiesReader("application.properties");
        String baseUrl = reader.getProperty("GUI_BASE_URL");
        LOGIN_URL = baseUrl + "auth/login";
        REGISTER_URL = baseUrl + "auth/register";
        HOME_URL = baseUrl;

//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
//        System.setProperty("selenide.browser", "chrome");
//        System.setProperty("webdriver.firefox.marionette", "false");
//        System.setProperty(
//                "webdriver.gecko.driver",
//                "/usr/local/bin/geckodriver");

        Configuration.webdriverLogsEnabled = true;
        Configuration.browser = "chrome";
//        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x920";
        Configuration.holdBrowserOpen = false;
        Configuration.headless = true;
//        Configuration.headless = false;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 10000;
        Configuration.pageLoadStrategy = "normal";
//        Configuration.pageLoadStrategy = "eager";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.setBinary("/usr/bin/chromium");
//         = new ChromeDriver(options);
//        WebDriverRunner webDriverRunner = new WebDriverRunner();
//        webDriverRunner.setWebDriver(webDriver);

//        ChromeDriverManager.getInstance().setup();
//        ChromiumDriverManager.getInstance().setup();
        WebDriverManager instance = ChromiumDriverManager.getInstance();
        instance.setup();
        webDriver = instance.getWebDriver();
//        WebDriverManager.chromiumdriver().setup();
//        ChromeOptions chromeOptions = new ChromeOptions().setBinary("/usr/bin/chromium");
//        webDriver = new ChromeDriver(chromeOptions);

        WebDriverRunner webDriverRunner = new WebDriverRunner();
        webDriverRunner.setWebDriver(webDriver);

//        WebDriverFactory driverFactory = new WebDriverFactory();
//        driverFactory.createWebDriver()
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
        webDriver.close();
    }
}
