import org.junit.jupiter.api.*;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DatabaseTests {

    private static final String url = "jdbc:mysql://localhost:3306/Movies?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "12345678";

    @BeforeAll
    public static void testConnection() {
        try (Connection c = getConnection(url, user, password)) {
            c.createStatement().executeQuery("SELECT * FROM Movies.Movies");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddingMovie() {
        try (Connection c = getConnection(url, user, password)) {
            int cnt1 = 0;
            int cnt2 = 0;
            try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Movies.Movies")) {
                while (rs.next()) {
                    cnt1++;
                }
            }
            c.createStatement().executeUpdate("INSERT INTO Movies.Movies(tytul) VALUES('')");
            ResultSet allMovies = c.createStatement().executeQuery("SELECT * FROM Movies.Movies");
            allMovies.last();
            Assertions.assertEquals("", allMovies.getString("tytul"));
            c.createStatement().execute("DELETE FROM Movies.Movies ORDER BY id DESC limit 1");
            try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Movies.Movies")) {
                while (rs.next()) {
                    cnt2++;
                }
            }
            Assertions.assertEquals(cnt1,cnt2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
