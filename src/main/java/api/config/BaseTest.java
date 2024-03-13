package api.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.PropertiesReader;

public class BaseTest {

    public static String BASE_URL;

    @BeforeClass(alwaysRun = true)
    public void configuration() {
        PropertiesReader reader = new PropertiesReader("application.properties");
        BASE_URL = reader.getProperty("API_BASE_URL");
    }
}
