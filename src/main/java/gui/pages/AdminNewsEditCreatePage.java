package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


public class AdminNewsEditCreatePage extends PageTools {

    private final By articleTitle = By.id("article-title");
    private final By toastContainer = By.id("toast-container");
    private final By inputTitle = By.id("title");
    private final By errorTitle = By.id("title-error");
    private final By inputDescription = By.id("description");
    private final By inputAuthor = By.id("author");
    private final By iframe = By.xpath("//iframe[@title='Rich Text Area']");
    private final By inputContent = By.id("tinymce");
    private final By inputPublishDate = By.id("publish-date");
    private final By checkboxVisible = By.id("visible");
    private final By inputVisible = By.id("visible-input");
    private final By saveButton = By.id("save-button");
    private final By backButton = By.id("back-button");
    private final By stayButton = By.id("stay-button");
    private final By leaveButton = By.id("leave-button");

    public void waitForLoad() {
        Selenide.sleep(100);
        $(articleTitle).shouldBe(Condition.visible);
    }

    public void focus() {
        waitForLoad();
        click(articleTitle);
        Selenide.sleep(100);
    }

    public String getArticleTitleText() {
        return getElementText(articleTitle);
    }

    public String getToastContainerText() {
        return $(toastContainer).getText();
    }

    public void setInputTitleValue(String value) {
        type(value, inputTitle);
        focus();
    }

    public String getInputTitleValue() {
        return $(inputTitle).getValue();
    }

    public String getErrorTitleText() {
        return $(errorTitle).getText();
    }

    public String getInputAuthorValue() {
        return $(inputAuthor).getValue();
    }

    public String getInputContentValue() {
        Selenide.switchTo().frame(getIframeId());
        String value = getElementText(inputContent);
        Selenide.switchTo().defaultContent();
        return value;
    }

    public void setInputContentValue(String value) {
        Selenide.switchTo().frame(getIframeId());
        type(value, inputContent);
        Selenide.switchTo().defaultContent();
    }

    private String  getIframeId() {
        return $(iframe).getAttribute("id");
    }

    public String getInputPublishDateValue() {
        return $(inputPublishDate).getValue();
    }

    public void setInputPublishDateValue(String value) {
        type(value, inputPublishDate);
    }

    public void setInputVisibleTrue() {
       if (!$(inputVisible).is(Condition.checked)) {
           toggleInputVisibleValue();
       }
    }

    public void setInputVisibleFalse() {
       if ($(inputVisible).is(Condition.checked)) {
           toggleInputVisibleValue();
       }
    }

    public boolean getInputVisibleValue() {
        return $(inputVisible).is(Condition.checked);
    }

    public void toggleInputVisibleValue() {
        click(checkboxVisible);
    }


    public String getInputDescriptionValue() {
        return $(inputDescription).getValue();
    }

    public void setInputDescriptionValue(String value) {
        type(value, inputDescription);
        focus();
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public void clickBackButton() {
        click(backButton);
    }

    public void clickStayBackButton() {
        click(stayButton);
    }

    public void clickLeaveButton() {
        click(leaveButton);
    }

    public boolean isSaveButtonEnabled() {
        return $(saveButton).isEnabled();
    }

}
