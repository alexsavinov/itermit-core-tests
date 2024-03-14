package gui.config;

import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromiumDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.Nonnull;

public class SelenoidWebDriverProvider implements WebDriverProvider {

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
//        DesiredCapabilities browser = new DesiredCapabilities();
//        browser.setBrowserName("chrome");
//        browser.setVersion("59.0");
//        browser.setCapability("enableVNC", true);

        System.out.println("!!!!!!" + capabilities);
        try {
//            WebDriverManager instance = ChromiumDriverManager.getInstance();
            WebDriverManager instance = FirefoxDriverManager.getInstance();
            instance.setup();
            //            RemoteWebDriver driver = new RemoteWebDriver(
//                    URI.create("http://172.28.27.17:4444/wd/hub").toURL(),
//                    browser
//            );
//            driver.manage().window().setSize(new Dimension(1280, 1024));
            return instance.getWebDriver();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}