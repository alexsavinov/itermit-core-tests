package gui.pages;

import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Header extends PageTools {

    private final By homeButton = By.id("home-button");
    private final By currentUserSpan = By.id("current-user");
    private final By currentUserButton = By.id("current-user-button");
    private final By adminPanelButton = By.id("admin-panel-button");
    private final By toggleFullscreenButton = By.id("toggle-fullscreen-button");
    private final By toggleThemeButton = By.id("toggle-theme-button");
    private final By translateButton = By.id("translate-button");
    private final By logoutButton = By.id("logout-button");
    private final By profileEditButton = By.id("profile-edit-button");


    public boolean isAdminPanelButtonExists() {
        return isElementExists(adminPanelButton);
    }

    public String getCurrentUserText() {
        return $(currentUserSpan).getText();
    }

    public void clickHomeButton() {
        click(homeButton);
        Selenide.sleep(30);
    }

    public void clickCurrentUserButton() {
        click(currentUserButton);
        Selenide.sleep(30);
    }

    public void clickToggleFullscreenButton() {
        click(toggleFullscreenButton);
        Selenide.sleep(30);
    }

    public void clickToggleThemeButton() {
        click(toggleThemeButton);
        Selenide.sleep(30);
    }

    public void clickTranslateButton() {
        click(translateButton);
        Selenide.sleep(30);
    }

    public void clickLogoutButton() {
        click(logoutButton);
        Selenide.sleep(30);
    }

    public void clickAdminPanelButton() {
        click(adminPanelButton);
        Selenide.sleep(30);
    }

    public void clickProfileEditButton() {
        click(profileEditButton);
        Selenide.sleep(30);
    }
}
