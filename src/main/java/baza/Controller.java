package baza;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Movie mov1 =  Movie.create("a",1,"c");
//        Movie mov2 =  Movie.create("sd",14,"sdafsd");
//        Movie mov3 =  Movie.create("3f",10,"c231243");
//        movieTableView.getItems().addAll(mov1, mov2, mov3);


    }

    private void editFocusedCell() {

        TablePosition<Movie, ?> focusedCell = movieTableView.focusModelProperty().get().focusedCellProperty().get();
        movieTableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());

    }

    public void saveDataToDatabase() {

        // tutaj jakoś preparedStatements wchodzą?

    }

}
