package gui.pages;

import com.codeborne.selenide.Condition;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


public class ChangeAvatarPage extends PageTools {

    private final By title = By.id("change-avatar-title");
    private final By file = By.id("file");
    private final By chooseAvatarButton = By.id("choose-avatar-button");
    private final By closeButton = By.id("avatar-close-button");
    private final By submitButton = By.id("avatar-submit-button");

    public void waitForLoad() {
        $(title).shouldBe(Condition.visible);
    }
    public String getTitleText() {
        return getElementText(title);
    }

    public void clickChooseAvatarButton() {
        click(chooseAvatarButton);
    }

    public void clickCloseButton() {
        click(closeButton);
    }

    public void clickSubmitButton() {
        click(submitButton);
    }

    public void setFile() {
        $(file).setValue("sfdsfdsf");
    }


}
