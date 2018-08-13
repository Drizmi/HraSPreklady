import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseHandler {

    private static Connection getConnection() throws SQLException {
        Connection connection = null;
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "admin");
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", properties);
    }

    private List<String> getWords(Connection connection) throws SQLException {
        String querry = "select * from translationgame";
        List<String> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querry);
        while (resultSet.next()) {
            list.add(resultSet.getString("words"));
        }
        statement.close();
        connection.close();
        return list;
    }

    private List<Integer> getAnswers(Connection connection) throws SQLException {
        String querry = "select * from translationgame";
        List<Integer> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querry);
        while (resultSet.next()) {
            int result;
            int wrong = resultSet.getInt("wrong_answers");
            int right = resultSet.getInt("right_answers");
            result = wrong - right;
            if (result <= 1) {
                result = 1;
            }
            list.add(result);
        }
        statement.close();
        connection.close();
        return list;
    }

    private void updateAnswers(String word, boolean b) throws SQLException {
        String querry = null;
        if (b) {
            querry = "update translationgame set right_answers_answers += 1 where word = ?";
        } else {
            querry = "update translationgame set wrong_answers_answers += 1 where word = ?";
        }
        PreparedStatement preparedStatement = getConnection().prepareStatement(querry);
        preparedStatement.setString(1, word);
        int updatedAnswers = preparedStatement.executeUpdate();
        System.out.println(updatedAnswers);
        preparedStatement.close();
    }

    private void insertWord(Connection connection, String word) throws SQLException {
        String querry = "insert into translationgame (words) values (word = ?)";
        PreparedStatement preparedStatement = getConnection().prepareStatement(querry);
        preparedStatement.setString(1, word);
        int updatedAnswers = preparedStatement.executeUpdate();
        System.out.println(updatedAnswers);
        preparedStatement.close();
    }

    public static void main(String[] args) throws SQLException {
        getConnection();
    }


}
