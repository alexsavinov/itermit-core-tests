package guiTests;

import com.codeborne.selenide.Selenide;
import gui.config.BaseTest;
import gui.pojos.Article;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static gui.Pages.*;

public class GuestTest extends BaseTest {

    @BeforeMethod()
    public void setUp(Method method) {
        Selenide.open(HOME_URL);
    }

    /**
     * NEWS
     */

    @Test()
    public void news() {
        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
    }

    @Test()
    public void news_whenClickShowMoreButton_shouldListMoreNews() {
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();
        Assert.assertEquals(articles.size(), 2);

        mainPage().clickShowMoreButton();

        mainPage().waitForLoad();

        articles = mainPage().getArticlesFromMain();
        System.out.println(articles.get(0));

        Assert.assertEquals(articles.size(), 4);
    }

    @Test(enabled = true)
    public void news_whenClickArticle_shouldShowDetailedArticle() {
        mainPage().getArticleElementsFromMain().get(1).click();

        Article article = articlePage().getArticle();

        Assert.assertFalse(article.getPublishDate().isEmpty());
        Assert.assertFalse(article.getTitle().isEmpty());
        Assert.assertTrue(article.getAuthor().startsWith("Author: "));
    }

    @Test(enabled = true)
    public void news_whenClickBackButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        articlePage().clickBackButton();
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();

        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
        Assert.assertEquals(articles.size(), 2);
    }

    @Test(enabled = true)
    public void news_whenClickHomeButton_shouldNavigateToMainPage() {
        mainPage().getArticleElementsFromMain().get(1).click();
        header().clickHomeButton();
        mainPage().waitForLoad();

        List<Article> articles = mainPage().getArticlesFromMain();

        Assert.assertEquals(mainPage().getNewsHeaderText(), "Last news");
        Assert.assertEquals(articles.size(), 2);
    }
}
