package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataManager {

    private static Connection con;

    public static void connectToDatabase(String filename) {
        String url = "jdbc:sqlite:" + filename;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            con = dataSource.getConnection();
            if (con.isValid(5)) {
                createTable();
            }
        } catch (SQLException e) {
            System.out.println("Error: Connection not valid.");
            e.printStackTrace();
        }
    }

    private static void createTable() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT NOT NULL," +
                    "pin TEXT NOT NULL," +
                    "balance INTEGER DEFAULT 0)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(Account a) {
        try {
            String num = a.getCard().getCardNumber();
            String pin = a.getCard().getPin();
            String insert = String.format("INSERT INTO card (number, pin) VALUES ('%s', '%s')", num, pin);
            Statement statement = con.createStatement();
            statement.executeUpdate(insert);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int readBalance(String number) {
        int balance = 0;
        try {
            Statement statement = con.createStatement();
            String query = String.format("SELECT balance FROM card WHERE number = %s", number);
            ResultSet results = statement.executeQuery(query);
            balance = results.getInt("balance");
            statement.close();
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public static void addIncome(int income, String number) {
        try {
            String update = String.format("UPDATE card SET balance = balance + %d WHERE number = %s", income, number);
            Statement statement = con.createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeAccount(String number) {
        try {
            String delete = String.format("DELETE FROM card WHERE number = %s", number);
            Statement statement = con.createStatement();
            statement.executeUpdate(delete);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCorrect(String number, String pin) {
        boolean num = false;
        boolean pIn = false;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT number, pin FROM card");
            while (resultSet.next()) {
                String strNum = resultSet.getString("number");
                String strPin = resultSet.getString("pin");
                if (strNum.equals(number) && strPin.equals(pin)) {
                    num = true;
                    pIn = true;
                    break;
                }
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (num && pIn) {
            return true;
        }
        return false;
    }

    public static void doTransfer(int amount, String receiver) {
        addIncome(amount, receiver);
    }

    public static boolean isPresent(String number) {
        boolean num = false;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT number FROM card");
            while (resultSet.next()) {
                String strNum = resultSet.getString("number");
                if (strNum.equals(number)) {
                    num = true;
                    break;
                }
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public static void reduceBalance(int amount, String number) {
        try {
            String update = String.format("UPDATE card SET balance = balance - %d WHERE number = %s", amount, number);
            Statement statement = con.createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAvailable(int amount, String number) {
        int availableBalance = readBalance(number);
        return availableBalance >= amount;
    }

    public static void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
