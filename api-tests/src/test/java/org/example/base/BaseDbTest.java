package org.example.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class BaseDbTest extends AbstractTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void clearTable(String tableName) {
        if (!tableName.matches("\\w+")) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        var sqlClearStatement = String.format("DELETE FROM %s", tableName);
        jdbcTemplate.execute(sqlClearStatement);
        log.info("Table {} cleared", tableName);
    }

    public int countInDbTable(String tableName) {
        if (!tableName.matches("\\w+")) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        var sqlStatement = String.format("SELECT COUNT(*) FROM %s", tableName);
        jdbcTemplate.execute(sqlStatement);
        var count = jdbcTemplate.queryForObject(sqlStatement, Integer.class);
        log.info("Orders count: {}", count);
        return count != null ? count : 0;
    }
}