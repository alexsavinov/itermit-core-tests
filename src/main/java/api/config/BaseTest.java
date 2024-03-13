package api.config;

import org.testng.annotations.BeforeClass;
import utils.PropertiesReader;

public class BaseTest {

    public static String BASE_URL;

    @BeforeClass(alwaysRun = true)
    public void configuration() {
        PropertiesReader reader = new PropertiesReader("application.properties");
        BASE_URL = reader.getProperty("API_BASE_URL");
    }
}
