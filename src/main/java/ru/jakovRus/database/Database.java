package ru.jakovRus.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jakov Rogov
 */
public class Database {
    private static Database instance;
    private Connection connection;

    Database() {
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:db/demodb", "SA", "");

            try (Reader reader = Resources.getResourceAsReader("init.sql")) {
                ScriptRunner runner = new ScriptRunner(connection);
                runner.setLogWriter(null);
                runner.setErrorLogWriter(null);
                runner.runScript(reader);
                connection.commit();
            } catch (IOException e) {
                System.err.println("[ERROR] IO exception");
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL exception");
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public int executeUpdate(String sql, Object... params) throws SQLIntegrityConstraintViolationException {
        int result = 0;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object param : params) {
                if (param instanceof String) {
                    stm.setString(i, (String) param);
                } else if (param instanceof Long) {
                    stm.setLong(i, (Long) param);
                } else if (param instanceof java.sql.Date) {
                    stm.setDate(i, (Date) param);
                } else if (param instanceof java.util.Date) {
                    LocalDate date = ((java.util.Date) param).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    stm.setDate(i, Date.valueOf(date));
                } else if (param instanceof Boolean) {
                    stm.setBoolean(i, (Boolean) param);
                } else if (param == null) {
                    stm.setNull(i, Types.NULL);
                }

                ++i;
            }
            result = stm.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet execute(String sql, Object... params) {
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object param : params) {
                if (param instanceof String) {
                    stm.setString(i, (String) param);
                } else if (param instanceof Long) {
                    stm.setLong(i, (Long) param);
                } else if (param instanceof Date) {
                    stm.setDate(i, (Date) param);
                } else if (param instanceof Boolean) {
                    stm.setBoolean(i, (Boolean) param);
                } else if (param == null) {
                    stm.setNull(i, Types.NULL);
                }

                ++i;
            }
            stm.execute();
            return stm.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
