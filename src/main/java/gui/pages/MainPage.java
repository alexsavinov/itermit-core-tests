package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import gui.core.PageTools;
import gui.pojos.Article;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends PageTools {

    private final SelenideElement logo = $(By.xpath("//div[@class='app_logo']"));
    private final By itemHeaders = By.xpath("//div[@class='inventory_item_name ']");
    private final By itemHeadersByID = By.xpath("(//div[@class='inventory_item_name '])[%d]");
    private final By itemHeadersByText = By.xpath("//*[text()='%s']");
    private final By itemDescriptions = By.xpath("//div[@class='inventory_item_desc']");
    private final By itemPrices = By.xpath("//div[@class='inventory_item_price']");
    private final SelenideElement addToCart = $(By.xpath("//button[text()='Add to cart']"));
    private final SelenideElement shoppingCartButton = $(By.xpath("//a[@class='shopping_cart_link']"));
    private final By burgerMenu = By.xpath("//button[@id='react-burger-menu-btn']");
    private final SelenideElement facebook = $(By.xpath("//a[text()='Facebook']"));
    private final SelenideElement twitter = $(By.xpath("//a[text()='Twitter']"));
    private final SelenideElement linkedIn = $(By.xpath("//a[text()='LinkedIn']"));

    private final By htmlElement = By.xpath("//html");
    private final By topHeader = By.id("top");
    private final By showMoreButton = By.id("show-more-button");
    private final By article = By.xpath("//div[contains(@class, 'article-item')]");
    private final By articlePublishDate = By.xpath("//mat-chip[contains(@class, 'article-publish-date')]");
    private final By articleTitle = By.xpath("//div[contains(@class, 'article-title')]");
    private final By articleLogo = By.xpath("//img[contains(@class, 'article-logo')]");
    private final By articleDescription = By.xpath("//div[contains(@class, 'article-description')]");


    public void waitForLoad() {
        Selenide.sleep(100);
        $(topHeader).shouldBe(Condition.visible);
    }

    public String getHtmlElementClass() {
        return $(htmlElement).getAttribute("class");
    }

    public String getNewsHeaderText() {
        return $(topHeader).getText();
    }

    public void clickShowMoreButton() {
        click(showMoreButton);
    }

    public ElementsCollection getArticleElementsFromMain() {
        return getElements(article);
    }

    public List<Article> getArticlesFromMain() {
////        List<Article> list = new  ArrayList<>();
////        List<Article> list =
//                Stream.of(getElements(article))
////                .map(n -> Article.ArticleBuilder().title(n.texts()).build())
//                .forEach(n -> System.out.println(n.size()));
////                .toList();
//        return getElements(article);

        ElementsCollection articles = getElements(article);

        List<String> articlePublishDateList = getElementsText(articlePublishDate);
        List<String> articleTitleList = getElementsText(articleTitle);
//        ElementsCollection articleLogoList = getElements(articleLogo);
//        List<String> articleDescriptionList = getElementsText(articleDescription);

        return IntStream.range(0, articles.size())
                .mapToObj(i -> Article.builder()
                        .publishDate(articlePublishDateList.get(i))
                        .title(articleTitleList.get(i))
//                        .logo(articleLogoList.get(i).getAttribute("src"))
//                        .description(articleDescriptionList.get(i))
                        .build())
                .toList();
    }


    public void waitMainPage() {
        logo.shouldBe(Condition.visible);
    }

    //    click methods
    public void clickAddToCartButton() {
        addToCart.shouldBe(Condition.and("clickable", Condition.visible, Condition.enabled)).click();
    }

    public void clickShoppingCartButton() {
        shoppingCartButton.shouldBe(Condition.and("clickable", Condition.visible, Condition.enabled)).click();
    }

    public void clickBurgerMenuButton() {
        click(burgerMenu);
    }

    public void clickTwitterButton() {
        twitter.shouldBe(Condition.and("clickable", Condition.visible, Condition.enabled)).click();
    }

    public void clickFacebookButton() {
        facebook.shouldBe(Condition.and("clickable", Condition.visible, Condition.enabled)).click();
    }

    public void clickLinkedInButton() {
        linkedIn.shouldBe(Condition.and("clickable", Condition.visible, Condition.enabled)).click();
    }
    //    return methods
//    type methods
//    public ElementsCollection getItemList() {
//        return headers.shouldBe(CollectionCondition.size(6));
//    }

    public List<Item> getItemListFromMainPage() {
        List<Item> itemList = new ArrayList<>();

        List<String> headersList = getElementsText(itemHeaders);
        List<String> desceriptionsList = getElementsText(itemDescriptions);
        List<String> pricesList = getElementsText(itemPrices);

        for (int i = 0; i < headersList.size(); i++) {
            Item item = new Item();

            item.setName(headersList.get(i));
            item.setDescription(desceriptionsList.get(i));
            item.setPrice(pricesList.get(i));

            itemList.add(item);
        }
        return itemList;
    }

    //    public List<SelenideElement> getElementsFromMain() {
    public ElementsCollection getElementsFromMain() {
        return getElements(itemHeaders);
    }

    public String getHeaderByID(int id) {
        return getElementText(itemHeadersByID, id);
    }

    public SelenideElement getItemByID(int id) {
        return getElement(itemHeadersByID, id);
    }

    public void clickElementByHeaderName(String name) {
        click(itemHeadersByText, name);
    }
}
