package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


public class AdminUsersEditPage extends PageTools {

    private final By editUserTitle = By.id("edit-user-title");
    private final By toastContainer = By.id("toast-container");
    private final By inputUsername = By.id("username");
    private final By inputName = By.id("name");
    private final By errorName = By.id("name-error");
    private final By inputSurname = By.id("surname");
    private final By inputGender = By.id("gender");
    private final By optionGenderMale = By.xpath("//mat-option[@value='MALE']");
    private final By optionGenderFemale = By.xpath("//mat-option[@value='FEMALE']");
    private final By inputCity = By.id("city");
    private final By inputAddress = By.id("address");
    private final By inputCompany = By.id("company");
    private final By inputMobile = By.id("mobile");
    private final By errorMobile = By.id("mobile-error");
    private final By inputTele = By.id("tele");
    private final By errorTele = By.id("tele-error");
    private final By inputWebsite = By.id("website");
    private final By errorWebsite = By.id("website-error");
    private final By inputDate = By.id("date");
    private final By editButton = By.id("edit-button");
    private final By backButton = By.id("back-button");
    private final By stayButton = By.id("stay-button");
    private final By leaveButton = By.id("leave-button");
    private final By changePasswordButton = By.id("change-password-button");
    private final By changeAvatarButton = By.id("change-avatar-button");

    public void waitForLoad() {
        Selenide.sleep(100);
        $(editUserTitle).shouldBe(Condition.visible);
    }

    public void focus() {
        $(editUserTitle).shouldBe(Condition.visible);
        click(editUserTitle);
        Selenide.sleep(50);
    }

    public String getToastContainerText() {
        return $(toastContainer).getText();
    }

    public String getEditUserTitleText() {
        return getElementText(editUserTitle);
    }

    public void clickEditButton() {
        click(editButton);
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

    public boolean isEditButtonEnabled() {
        return $(editButton).isEnabled();
    }

    public String getInputNameValue() {
        return $(inputName).getValue();
    }

    public String getErrorNameText() {
        return $(errorName).getText();
    }

    public void setInputNameValue(String value) {
        type(value, inputName);
        focus();
    }

    public String getInputSurnameValue() {
        return $(inputSurname).getValue();
    }

    public void setInputSurnameValue(String value) {
        type(value, inputSurname);
        focus();
    }

    public String getInputGenderValue() {
        return $(inputGender).getText();
    }

    public void setInputGenderMale() {
        click(inputGender);
        click(optionGenderMale);
    }

    public void setInputGenderFemale() {
        click(inputGender);
        click(optionGenderFemale);
    }

    public String getInputCityValue() {
        return $(inputCity).getValue();
    }

    public void setInputCityValue(String value) {
        type(value, inputCity);
        focus();
    }

    public String getInputAddressValue() {
        return $(inputAddress).getValue();
    }

    public void setInputAddressValue(String value) {
        type(value, inputAddress);
        focus();
    }

    public String getInputCompanyValue() {
        return $(inputCompany).getValue();
    }

    public void setInputCompanyValue(String value) {
        type(value, inputCompany);
        focus();
    }

    public String getInputMobileValue() {
        return $(inputMobile).getValue();
    }

    public void setInputMobileValue(String value) {
        type(value, inputMobile);
        focus();
    }

    public String getErrorMobileText() {
        return $(errorMobile).getText();
    }

    public String getInputTeleValue() {
        return $(inputTele).getValue();
    }

    public void setInputTeleValue(String value) {
        type(value, inputTele);
        focus();
    }

    public String getErrorTeleText() {
        return $(errorTele).getText();
    }

    public String getInputWebsiteValue() {
        return $(inputWebsite).getValue();
    }

    public void setInputWebsiteValue(String value) {
        type(value, inputWebsite);
        focus();
    }

    public String getErrorWebsiteText() {
        return $(errorWebsite).getText();
    }

    public String getInputDateValue() {
        return $(inputDate).getValue();
    }

    public void setInputDateValue(String value) {
        type(value, inputDate);
        focus();
    }

    public void clickChangePasswordButton() {
        click(changePasswordButton);
    }

    public void clickChangeAvatarButton() {
        click(changeAvatarButton);
    }
}
