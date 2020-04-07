package baza;

import com.sun.org.apache.bcel.internal.generic.ObjectType;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import object.Movie;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;
import java.util.ArrayList;

public class Controller {

    @FXML
    protected TableView<Movie> movieTableView;

    public void initialize() {

        TableColumn<Movie, String> tytulColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(0);
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        tytulColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tytulColumn.setOnEditCommit(edit -> {
            Movie editedCar = movieTableView.getEditingCell().getTableView().getItems().get(movieTableView.getEditingCell().getRow());
            editedCar.setTytul(edit.getNewValue());
        });

        TableColumn<Movie, Integer> ocenaColumn = (TableColumn<Movie, Integer>) movieTableView.getColumns().get(1);
        ocenaColumn.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        ocenaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ocenaColumn.setOnEditCommit(edit -> {
            Movie editedCar = movieTableView.getEditingCell().getTableView().getItems().get(movieTableView.getEditingCell().getRow());
            editedCar.setOcena(edit.getNewValue());
        });

        TableColumn<Movie, String> komentarzColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(2);
        komentarzColumn.setCellValueFactory(new PropertyValueFactory<>("komentarz"));
        komentarzColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        komentarzColumn.setOnEditCommit(edit -> {
            Movie editedCar = movieTableView.getEditingCell().getTableView().getItems().get(movieTableView.getEditingCell().getRow());
            editedCar.setKomentarz(edit.getNewValue());
        });

        movieTableView.setEditable(true);
        movieTableView.getSelectionModel().cellSelectionEnabledProperty().setValue(true);
        movieTableView.setOnMouseClicked(click -> {
                    if (click.getClickCount() > 1) {
                        editFocusedCell();
                    }
                });

        String url = "jdbc:mysql://localhost:3306/Movies?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "12345678";
        try {
            Connection myConn = DriverManager.getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT * FROM Movies.Movies";
            ResultSet allMovies = myStmt.executeQuery(sql);

            while (allMovies.next()) {

                Movie mov = Movie.create(allMovies.getString("tytul"), allMovies.getInt("ocena"), allMovies.getString("komentarz"));
                movieTableView.getItems().add(mov);

            }

            myConn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void editFocusedCell() {

        TablePosition<Movie, ?> focusedCell = movieTableView.focusModelProperty().get().focusedCellProperty().get();

        // https://stackoverflow.com/questions/21987552/how-to-make-a-javafx-tableview-cell-editable-without-first-pressing-enter/21988562#21988562 - może to zrobić?

        movieTableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());

    }


    // pozwala na dodanie do bazy wartości w podświetlonej (focused) komórce
    public void saveEditedCell() {

        TablePosition<Movie, ?> focusedCell = movieTableView.focusModelProperty().get().focusedCellProperty().get();

        Integer movieId = focusedCell.getRow();
        Integer columnId = focusedCell.getColumn();
        String columnName = "";

        // przypisuje nazwy odpowiednim kolumnom, żeby dobrze odnieść się do nich w bazie SQL
        switch(columnId) {

            case 0:
                columnName = "tytul";
                break;
            case 1:
                columnName = "ocena";
                break;
            case 2:
                columnName = "komentarz";
            default:
                break;

        }

        Object cellValue = (Object) focusedCell.getTableColumn().getCellObservableValue(movieId).getValue();
        System.out.println(cellValue);

        String finalColumnName = columnName; // zalecenie IntelliJ

        String url = "jdbc:mysql://localhost:3306/Movies?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "12345678";
        try {
            Connection myConn = DriverManager.getConnection(url, user, password);
            String sqlPart = finalColumnName.toString() + "=\"" + cellValue.toString() + "\"";
            movieId = movieId + 1;
            String sql = "UPDATE Movies.Movies SET " + sqlPart + " WHERE id=" + movieId;
            System.out.println(sql);
            PreparedStatement updateRow = myConn.prepareStatement(sql);

            updateRow.executeUpdate();

            myConn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}