package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import gui.core.PageTools;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;


public class AdminPanelPage extends PageTools {

    private final SelenideElement pageTitle = $(By.xpath("//h1[@class='page-title']"));
    private final By breadcrumb = By.xpath("//span[contains(@class, 'breadcrumb-link')]");
    private final By menuItems = By.xpath("//span[contains(@class, 'menu-name')]");
    private final By menuItemDashboard = By.xpath("//li[@ng-reflect-route='dashboard']");
    private final By menuItemUsers = By.xpath("//li[@ng-reflect-route='users']");
    private final By menuItemNews = By.xpath("//li[@ng-reflect-route='news']");

    public void waitForLoad() {
        Selenide.sleep(100);
        $(pageTitle).shouldBe(Condition.visible);
    }

    public String getPageTitleText() {
        return $(pageTitle).getText();
    }

    public List<String> getBreadcrumbText() {
        return getElementsText(breadcrumb);
    }

    public List<String> getMenuItemsText() {
        return getElementsText(menuItems);
    }

    public void clickMenuItemDashboard() {
        click(menuItemDashboard);
    }

    public void clickMenuItemUsers() {
        click(menuItemUsers);
    }

    public void clickMenuItemNews() {
        click(menuItemNews);
    }
}
