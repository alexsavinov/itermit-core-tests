package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import gui.pojos.Article;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends PageTools {

    private final By htmlElement = By.xpath("//html");
    private final By topHeader = By.id("top");
    private final By showMoreButton = By.id("show-more-button");
    private final By article = By.xpath("//div[contains(@class, 'article-item')]");
    private final By articlePublishDate = By.xpath("//mat-chip[contains(@class, 'article-publish-date')]");
    private final By articleTitle = By.xpath("//div[contains(@class, 'article-title')]");


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
        ElementsCollection articles = getElements(article);

        List<String> articlePublishDateList = getElementsText(articlePublishDate);
        List<String> articleTitleList = getElementsText(articleTitle);

        return IntStream.range(0, articles.size())
                .mapToObj(i -> Article.builder()
                        .publishDate(articlePublishDateList.get(i))
                        .title(articleTitleList.get(i))
                        .build())
                .toList();
    }

}
