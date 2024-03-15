package gui.config;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.webdriver.WebDriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.ChromiumDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.PropertiesReader;

import javax.annotation.Nonnull;

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
//        System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium");
//        System.setProperty("selenide.browser", "chrome");
//        System.setProperty("webdriver.firefox.marionette", "false");
//        System.setProperty(
//                "webdriver.gecko.driver",
//                "/usr/local/bin/geckodriver");

        Configuration.webdriverLogsEnabled = true;
//        Configuration.browser = "chrome";
        Configuration.browser = "firefox";
//        Configuration.browser = SelenoidWebDriverProvider.class.getName();
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

//        WebDriverManager firefoxdriver = WebDriverManager.firefoxdriver();
//        firefoxdriver.setup();
//        FirefoxOptions opt = new FirefoxOptions().setBinary("/usr/bin/firefox");
//        webDriver = new FirefoxDriver(opt);
//        System.out.println("webDriver -- " + webDriver);
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary("/usr/bin/chromium");

//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromium");

//        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

//        WebDriverManager wdm = WebDriverManager.chromiumdriver()
//                        .browserInDocker()
//                .enableVnc()
//                .enableRecording()
                ;

//        webDriver = firefoxdriver.create();

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.setBinary("/usr/bin/chromium");
//         = new ChromeDriver(options);


//        ChromeDriverManager.getInstance().setup();
//        ChromiumDriverManager.getInstance().setup();

//        WebDriverManager instance = ChromiumDriverManager.getInstance();
//        WebDriverManager instance = ChromeDriverManager.getInstance();
//        WebDriverManager.chromedriver().setup();
//        WebDriverManager instance = WebDriverManager.chromedriver();
//        WebDriverManager instance = WebDriverManager.chromiumdriver();
//        instance.setup();
//        webDriver = new ChromiumDriver();
//        webDriver = new ChromeDriver();

//        WebDriverRunner.setWebDriver(webDriver);

//        WebDriverManager.chromiumdriver().setup();
//        ChromiumOptions chromeOptions = new ChromiumOptions().setBinary("/usr/bin/chromium");
//        webDriver = new ChromiumDriver(chromeOptions);
//
//        WebDriverRunner.setWebDriver(webDriver);


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
//        webDriver.close();
    }
}
