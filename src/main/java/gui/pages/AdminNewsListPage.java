package gui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import gui.core.PageTools;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;


public class AdminNewsListPage extends PageTools {

    private final By searchInput = By.id("search");
    private final By resetButton = By.id("reset-button");
    private final By createButton = By.id("create-button");
    private final By paginatorSelectPageSize1 = By.id("mat-option-0");
    private final By paginatorSelectPageSize5 = By.id("mat-option-1");
    private final By tableHead = By.xpath("//thead//span");
    private final By tableRow = By.xpath("//tbody//tr");
    private final By tableCell = By.xpath("//tbody//tr//td");
    private final By paginatorSelect = By.xpath("//mat-form-field[contains(@class, 'mat-mdc-paginator-page-size-select')]");
    private final By sortById = By.xpath("(//div[contains(@class, 'mat-sort-header-container')])[1]");
    private final By sortByTitle = By.xpath("(//div[contains(@class, 'mat-sort-header-container')])[3]");
    private final By editButton = By.xpath("(//td//mtx-grid-cell//button)[5]");
    private final By deleteButton = By.xpath("(//td//mtx-grid-cell//button)[8]");
    private final By deleteOkButton = By.xpath("(//mat-dialog-container//button)[1]");
    private final By deleteCloseButton = By.xpath("(//mat-dialog-container//button)[2]");


    public void waitForLoad() {
        Selenide.sleep(100);
        $(searchInput).shouldBe(Condition.visible);
    }

    public void typeSearchInput(String text) {
        type(text, searchInput);
    }

    public void clickResetButton() {
        click(resetButton);
    }

    public void clickCreateButton() {
        click(createButton);
    }

    public void clickPaginatorSelectPageSize1() {
        click(paginatorSelectPageSize1);
    }

    public void clickPaginatorSelectPageSize5() {
        click(paginatorSelectPageSize5);
    }

    public List<String> getTableHeadText() {
        return getElementsText(tableHead);
    }

    public String[][] getTable() {
        int columns = getElements(tableHead).size();
        int rows = getElements(tableRow).size();

        ElementsCollection elements = getElements(tableCell);

        String[][] table = new String[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                table[row][col] = elements.get(col + columns * row).getText();
            }
        }

        return table;
    }

    public String getPaginatorSelectValue() {
        return getElementText(paginatorSelect);
    }

    public void clickPaginatorSelect() {
        click(paginatorSelect);
    }

    public void clickSortById() {
        click(sortById);
    }

    public void clickSortByTitle() {
        click(sortByTitle);
    }

    public void clickEditButton() {
        click(editButton);
    }

    public void clickDeleteButton() {
        click(deleteButton);
    }

    public void clickDeleteOkButton() {
        click(deleteOkButton);
    }

    public void clickDeleteCloseButton() {
        click(deleteCloseButton);
    }

}
