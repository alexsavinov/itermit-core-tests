package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends PageTools {

    private final By registerTitle = By.id("register-title");
    private final By toastContainer = By.id("toast-container");
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By confirmPasswordInput = By.id("confirm-password");
    private final By readAndAgreeCheckbox = By.id("read-and-agree");
    private final By usernameError = By.id("username-error");
    private final By passwordError = By.id("password-error");
    private final By confirmPasswordError = By.id("confirm-password-error");
    private final By registerButton = By.id("register");


    public void waitForLoad() {
        Selenide.sleep(100);
        $(registerTitle).shouldBe(Condition.visible);
    }

    public void focus() {
        $(registerTitle).shouldBe(Condition.visible);
        click(registerTitle);
        Selenide.sleep(50);
    }

    public void typeUsername(String username) {
        type(username, usernameInput);
    }

    public void typePassword(String password) {
        type(password, passwordInput);
    }

    public void typeConfirmPassword(String password) {
        type(password, confirmPasswordInput);
    }

    public void clickReadAndAgreeCheckbox() {
        click(readAndAgreeCheckbox);
    }

    public void clickRegisterButton() {
        click(registerButton);
    }

    public boolean isRegisterButtonEnabled() {
        return $(registerButton).isEnabled();
    }

    public String getUsernameErrorText() {
        return $(usernameError).getText();
    }

    public String getPasswordErrorText() {
        return $(passwordError).getText();
    }

    public String getConfirmPasswordErrorText() {
        return $(confirmPasswordError).getText();
    }

    public String getToastContainerText() {
        return $(toastContainer).getText();
    }
}
