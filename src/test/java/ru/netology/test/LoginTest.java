package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SqlData;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private LoginPage loginPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void shouldLoginWithExistUser() {

        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = SqlData.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldAddNewLogin() {

        val authInfo = DataHelper.getUser();
        val verificationPage = loginPage.validLogin(authInfo);
        sleep(100);
        val verificationCode = SqlData.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBlockIfLoginWithErrorPassword() {

        val authInfo = DataHelper.getErrorPassword();
        loginPage.validLogin(authInfo);
        loginPage.errorMessage();
        loginPage.clearFields();
        loginPage.validLogin(authInfo);
        loginPage.errorMessage();
        loginPage.clearFields();
        loginPage.validLogin(authInfo);
        val status = SqlData.getStatus(authInfo.getLogin());
        assertEquals("blocked", status);
    }

    @AfterAll
    static void clean() {
        SqlData.cleanData();
    }
}
