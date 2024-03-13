package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;


public class ProfilePage extends PageTools {

    private final By editProfileTitle = By.id("edit-profile-title");
    private final By additionalSettingsTitle = By.id("additional-settings-title");
    private final By input = By.cssSelector("input");
    private final By inputName = By.id("name");
    private final By inputSurname = By.id("surname");
    private final By inputGender = By.id("gender");
    private final By optionGenderMale = By.xpath("//mat-option[@value='MALE']");
    private final By optionGenderFemale = By.xpath("//mat-option[@value='FEMALE']");
    private final By inputCity = By.id("city");
    private final By inputAddress = By.id("address");
    private final By inputCompany = By.id("company");
    private final By inputMobile = By.id("mobile");
    private final By inputTele = By.id("tele");
    private final By inputWebsite = By.id("website");
    private final By inputDate = By.id("date");
    private final By saveButton = By.id("save-button");
    private final By changePasswordButton = By.id("change-password-button");
    private final By changeAvatarButton = By.id("change-avatar-button");

    public void waitForLoad() {
        Selenide.sleep(100);
        $(editProfileTitle).shouldBe(Condition.visible);
    }

    public String getEditProfileTitleText() {
        return getElementText(editProfileTitle);
    }
    public String getAdditionalSettingsTitleText() {
        return getElementText(additionalSettingsTitle);
    }

    public Map<String, String> getInputsMap() {
        Map<String, String> result = new HashMap<>();
        ElementsCollection elements = getElements(input);

        IntStream.range(0, elements.size())
                .forEach(i -> result.put(
                        elements.get(i).getAttribute("formcontrolname"),
                        elements.get(i).getValue()
                ));

        return result;
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public void clickChangePasswordButton() {
        click(changePasswordButton);
    }

    public void clickChangeAvatarButton() {
        click(changeAvatarButton);
    }

    public String getInputNameValue() {
        return $(inputName).getValue();
    }

    public void setInputNameValue(String value) {
        type(value, inputName);
    }

    public String getInputSurnameValue() {
        return $(inputSurname).getValue();
    }

    public void setInputSurnameValue(String value) {
        type(value, inputSurname);
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
    }

    public String getInputAddressValue() {
        return $(inputAddress).getValue();
    }

    public void setInputAddressValue(String value) {
        type(value, inputAddress);
    }

    public String getInputCompanyValue() {
        return $(inputCompany).getValue();
    }

    public void setInputCompanyValue(String value) {
        type(value, inputCompany);
    }

    public String getInputMobileValue() {
        return $(inputMobile).getValue();
    }

    public void setInputMobileValue(String value) {
        type(value, inputMobile);
    }

    public String getInputTeleValue() {
        return $(inputTele).getValue();
    }

    public void setInputTeleValue(String value) {
        type(value, inputTele);
    }

    public String getInputWebsiteValue() {
        return $(inputWebsite).getValue();
    }

    public void setInputWebsiteValue(String value) {
        type(value, inputWebsite);
    }

    public String getInputDateValue() {
        return $(inputDate).getValue();
    }

    public void setInputDateValue(String value) {
        type(value, inputDate);
    }
}
