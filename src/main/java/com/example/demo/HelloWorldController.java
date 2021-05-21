package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public class HelloWorldController implements RowCallbackHandler {

    @Value("classpath:demo.txt")
    private Resource resource;

    private final JdbcTemplate jdbcTemplate;

    private String postgresVersion = "";

    @Autowired
    DataSource dataSource;

    @Autowired
    public HelloWorldController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(value = "/", produces = "text/plain; charset=utf-8")
    public String helloWorld() throws IOException, SQLException {

        jdbcTemplate.query("SELECT version()", this);

        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        return FileCopyUtils.copyToString(reader) + "\n\n" + this.postgresVersion + "\n\n" + dataSource.getConnection().getMetaData().getURL();
    }

    public void processRow(ResultSet resultSet) throws SQLException
    {
        this.postgresVersion = resultSet.getString(1);
    }
}
