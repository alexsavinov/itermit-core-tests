package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import gui.core.PageTools;
import gui.pojos.Article;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;


public class ArticlePage extends PageTools {

    private final By articlePublishDate = By.id("article-publish-date");
    private final By articleTitle = By.id("article-title");
    private final By articleLogo = By.id("article-logo");
    private final By articleAuthor = By.id("article-author");
    private final By articleDescription = By.id("article-description");
    private final By articleContent = By.id("article-content");
    private final By backButton = By.id("back-button");

    public Article getArticle() {
        Article article = Article.builder()
                .publishDate(getElementText(articlePublishDate))
                .title(getElementText(articleTitle))
                .author(getElementText(articleAuthor))
                .build();

        SelenideElement logo = getElement(articleLogo);
        if (logo.exists()) {
            article.setLogo(logo.getAttribute("src"));
        }

        SelenideElement description = getElement(articleDescription);
        if (description.exists()) {
            article.setDescription(getElementText(articleDescription));
        }

        SelenideElement content = getElement(articleContent);
        if (content.isDisplayed()) {
            article.setContent(getElementText(articleContent));
        }

        return article;
    }

    public void waitForLoad() {
        Selenide.sleep(100);
        $(articleTitle).shouldBe(Condition.visible);
    }
    public void clickBackButton() {
        click(backButton);
    }

}
