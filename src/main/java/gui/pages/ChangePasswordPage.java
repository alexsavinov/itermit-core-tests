package gui.pages;

import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


public class ChangePasswordPage extends PageTools {

    private final By title = By.id("change-password-title");
    private final By passwordInput = By.id("password");
    private final By confirmPasswordInput = By.id("confirm-password");
    private final By closeButton = By.id("password-close-button");
    private final By submitButton = By.id("password-submit-button");

    public String getTitleText() {
        return getElementText(title);
    }


    public void setPassword(String value) {
        type(value, passwordInput);
    }

    public void setConfirmPassword(String value) {
        type(value, confirmPasswordInput);
    }

    public void clickCloseButton() {
        click(closeButton);
    }

    public void clickSubmitButton() {
        click(submitButton);
    }

    public boolean checkSubmitButtonEnabled() {
        return $(submitButton).isEnabled();
    }
}
