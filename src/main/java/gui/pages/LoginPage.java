package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends PageTools {

    private final By loginTitle = By.id("login-title");
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login");
    private final By usernameError = By.id("username-error");
    private final By passwordError = By.id("password-error");

    public void waitForLoad() {
        Selenide.sleep(100);
        $(loginTitle).shouldBe(Condition.visible);
    }

    public void typeUsername(String username) {
        type(username, usernameInput);
    }

    public void typePassword(String password) {
        type(password, passwordInput);
    }

    public void clickLoginButton() {
        waitForLoad();
        click(loginButton);
    }

    public String getLoginTitleText() {
        return $(loginTitle).getText();
    }

    public String getUsernameErrorText() {
        return $(usernameError).getText();
    }

    public String getPasswordErrorText() {
        return $(passwordError).getText();
    }
}
