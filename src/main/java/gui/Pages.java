package gui;

import gui.pages.*;

public class Pages {

    private static LoginPage loginPage;
    private static RegisterPage registerPage;
    private static MainPage mainPage;
    private static ArticlePage articlePage;
    private static Header header;
    private static AdminPanelPage adminPanelPage;
    private static ProfilePage profilePage;
    private static ChangePasswordPage changePasswordPage;
    private static ChangeAvatarPage changeAvatarPage;
    private static AdminUsersListPage adminUsersListPage;
    private static AdminUsersCreatePage adminUsersCreatePage;
    private static AdminUsersEditPage adminUsersEditPage;
    private static AdminNewsListPage adminNewsListPage;
    private static AdminNewsEditCreatePage adminNewsEditCreatePage;

    public static LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    public static RegisterPage registerPage() {
        if (registerPage == null) {
            registerPage = new RegisterPage();
        }
        return registerPage;
    }

    public static MainPage mainPage() {
        if (mainPage == null) {
            mainPage = new MainPage();
        }
        return mainPage;
    }

    public static ArticlePage articlePage() {
        if (articlePage == null) {
            articlePage = new ArticlePage();
        }
        return articlePage;
    }

    public static Header header() {
        if (header == null) {
            header = new Header();
        }
        return header;
    }

    public static AdminPanelPage adminPanelPage() {
        if (adminPanelPage == null) {
            adminPanelPage = new AdminPanelPage();
        }
        return adminPanelPage;
    }

    public static ProfilePage profilePage() {
        if (profilePage == null) {
            profilePage = new ProfilePage();
        }
        return profilePage;
    }

    public static ChangePasswordPage changePasswordPage() {
        if (changePasswordPage == null) {
            changePasswordPage = new ChangePasswordPage();
        }
        return changePasswordPage;
    }

    public static ChangeAvatarPage changeAvatarPage() {
        if (changeAvatarPage == null) {
            changeAvatarPage = new ChangeAvatarPage();
        }
        return changeAvatarPage;
    }

    public static AdminUsersListPage adminUsersListPage() {
        if (adminUsersListPage == null) {
            adminUsersListPage = new AdminUsersListPage();
        }
        return adminUsersListPage;
    }

    public static AdminUsersCreatePage adminUsersCreatePage() {
        if (adminUsersCreatePage == null) {
            adminUsersCreatePage = new AdminUsersCreatePage();
        }
        return adminUsersCreatePage;
    }

    public static AdminUsersEditPage adminUsersEditPage() {
        if (adminUsersEditPage == null) {
            adminUsersEditPage = new AdminUsersEditPage();
        }
        return adminUsersEditPage;
    }

    public static AdminNewsListPage adminNewsListPage() {
        if (adminNewsListPage == null) {
            adminNewsListPage = new AdminNewsListPage();
        }
        return adminNewsListPage;
    }

    public static AdminNewsEditCreatePage adminNewsEditCreatePage() {
        if (adminNewsEditCreatePage == null) {
            adminNewsEditCreatePage = new AdminNewsEditCreatePage();
        }
        return adminNewsEditCreatePage;
    }
}
