package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlData {

    @SneakyThrows
    public static String getVerificationCode(DataHelper.AuthInfo authInfo) {
        val selectSQL = "SELECT code FROM auth_codes ac left join users us on ac.user_id = us.id WHERE login = ?;";
        val runner = new QueryRunner();
        String code = null;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            code = runner.query(conn, selectSQL, new ScalarHandler<>(), authInfo.getLogin());
        }

        return code;
    }

    @SneakyThrows
    public static String getStatus(String login) {
        val selectSQL = "SELECT status FROM users WHERE login = ?;";
        val runner = new QueryRunner();
        String status = null;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"

                );
        ) {
            status = runner.query(conn, selectSQL, new ScalarHandler<>(), login);
        }
        return status;
    }

    @SneakyThrows
    public static String getUserLogin() {
        val faker = new Faker();
        val runner = new QueryRunner();
        String login = null;
        val dataSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";
        val loginSQL = "SELECT login FROM users WHERE id = '1';";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );

        ) {
            runner.update(conn, dataSQL, 1, faker.name().username(), "$2a$10$qokVgsL98O9Dlu24N7LSc.rRj1lgp7E37jFR90awgMI4RwxeCWwna");
            login = runner.query(conn, loginSQL, new ScalarHandler<>());
        }
        return login;
    }

    @SneakyThrows
    public static void cleanData() {
        val runner = new QueryRunner();
        val cleanUsers = "DELETE FROM users;";
        val cleanCards = "DELETE FROM cards;";
        val cleanAuth_Codes = "DELETE FROM auth_codes;";
        val cleanCard_Transactions = "DELETE FROM card_transactions;";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, cleanCards);
            runner.update(conn, cleanAuth_Codes);
            runner.update(conn, cleanUsers);
            runner.update(conn, cleanCard_Transactions);
        }
    }
}

