package com.example.helloworld.Controller;

import javax.sql.DataSource;
import java.sql.Connection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

    private final DataSource dataSource;

    public DatabaseController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/db-test")
    public String testDatabase() {
        try (Connection connection = dataSource.getConnection()) {

            return "PostgreSQL connected successfully! Database: "
                    + connection.getCatalog();

        } catch (Exception e) {

            return "PostgreSQL connection failed: "
                    + e.getMessage();
        }
    }
}