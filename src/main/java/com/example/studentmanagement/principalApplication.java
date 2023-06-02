package com.example.studentmanagement;
//realiser par Bilal Yachou,Omari Mohamed,Mouad Makkour
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;



public class principalApplication extends Application {
    TableView<student> table;
    @Override
    public void start(Stage stage) throws IOException {
        //first colomn (CNE)
        TableColumn<student,Integer> cneCol = new TableColumn<>("CNE");
        cneCol.setMinWidth(200);
        cneCol.setCellValueFactory(new PropertyValueFactory<>("CNE"));

        //second colomn (firstName)
        TableColumn<student,String> firstNameCol = new TableColumn<>("firstName");
        firstNameCol.setMinWidth(200);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        //third colomn (lastName)
        TableColumn<student,String> lastNameCol = new TableColumn<>("lastName");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        //fourth colomn (email)
        TableColumn<student,String> emailCol = new TableColumn<>("email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        //fifth colomn (birthDay)
        TableColumn<student,String> birthDayCol = new TableColumn<>("birthDay");
        birthDayCol.setMinWidth(200);
        birthDayCol.setCellValueFactory(new PropertyValueFactory<>("birthDay"));

        //6 and 7 colomn (delete or update)
        TableColumn<student,Void> deleteButtonColumn = new TableColumn<>("Delete");
        TableColumn<student,Void> updateButtonColumn = new TableColumn<>("Update");

        deleteButtonColumn.setCellFactory(param -> new TableCell<student, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    student student = getTableRow().getItem();
                    if (student != null) {
                        int studentCNE = student.getCNE();
                        deleteStudent(studentCNE);
                    }
                });
                deleteButton.setStyle("-fx-background-color:red;-fx-text-fill:white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Set the cell factory for the update button column
        updateButtonColumn.setCellFactory(param -> new TableCell<student, Void>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    student student = getTableRow().getItem();
                    if (student != null) {
                        openUpdateStudentDialog(student);
                    }
                });
                updateButton.setStyle("-fx-background-color:orange;-fx-text-fill:white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
        //8 colomn profile
        TableColumn<student,Void> profileBtnCol = new TableColumn<>("Profile");
        // Set the cell factory for the profile button column
        profileBtnCol.setCellFactory(param -> new TableCell<student, Void>() {
            private final Button profileButton = new Button("Profile");

            {
                profileButton.setOnAction(event -> {
                    student student = getTableRow().getItem();
                    if (student != null) {
                       openProfileStudentDialog(student);
                    }
                });
                profileButton.setStyle("-fx-background-color:blue;-fx-text-fill:white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(profileButton);
                }
            }
        });

        table = new TableView<>();
        table.setItems(getStudent());
        table.getColumns().addAll(cneCol,firstNameCol,lastNameCol,emailCol,birthDayCol,deleteButtonColumn,
                updateButtonColumn,profileBtnCol);
        StackPane stackPane = new StackPane();
        Text Title = new Text("EnsaCription");
        Title.setFont(Font.font("Billabong", FontWeight.BOLD,30));
        Title.setFill(Color.WHITE);
        stackPane.getChildren().addAll(Title);
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10,10,10,10));
        Color backgroundColor = Color.valueOf("#28252e");
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor,null,null);
        Background background = new Background(backgroundFill);
        layout.setBackground(background);

        //Student Information
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        //--------------Labels and textfields-------------------
        Label cneLabel = new Label("CNE:");
        cneLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(cneLabel,0,0);
        TextField cneTextField = new TextField();
        cneTextField.setPromptText("please enter integer value");
        cneTextField.setPrefWidth(300);
        GridPane.setConstraints(cneTextField,1,0);

        Label firstNameLabel = new Label("firstName:");
        firstNameLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(firstNameLabel,0,1);
        TextField firstNameTextField = new TextField();
        GridPane.setConstraints(firstNameTextField,1,1);

        Label lastNameLabel = new Label("lastName:");
        lastNameLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(lastNameLabel,0,2);
        TextField lastNameTextField = new TextField();
        GridPane.setConstraints(lastNameTextField,1,2);

        Label emailLabel = new Label("Email:");
        emailLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(emailLabel,0,3);
        TextField emailTextField = new TextField();
        GridPane.setConstraints(emailTextField,1,3);

        Label birthLabel = new Label("BirthDAY:");
        birthLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(birthLabel,0,4);
        TextField birthTextField = new TextField();
        birthTextField.setPromptText("xx/xx/xxxx");
        GridPane.setConstraints(birthTextField,1,4);

        Button addBtn = new Button("ADD STUDENT");
        addBtn.setStyle("-fx-background-color:green;-fx-text-fill:white;");
        GridPane.setHalignment(addBtn, HPos.CENTER);

        //Button action
        addBtn.setOnAction(event -> {
            if(cneTextField.getText().isEmpty()||firstNameTextField.getText().isEmpty()||
            lastNameTextField.getText().isEmpty()||emailTextField.getText().isEmpty()||
                    birthTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            }else{
            addStudent(cneTextField.getText(),firstNameTextField.getText(),
                    lastNameTextField.getText(),emailTextField.getText(),birthTextField.getText());
            }
        });
        GridPane.setConstraints(addBtn,1,5);
        grid.getChildren().addAll(cneLabel,cneTextField,firstNameLabel,firstNameTextField,
                lastNameLabel,lastNameTextField,emailLabel,emailTextField,birthLabel,birthTextField,addBtn);



        layout.getChildren().addAll(stackPane,table,grid);
        Scene scene = new Scene(layout, 1300, 600);
        stage.setTitle("Student Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    //Get student from dataBase
    public ObservableList<student> getStudent(){
        ObservableList<student> students = FXCollections.observableArrayList();

        try {
            Connection conn = dataBaseConnection.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM student";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int cne = resultSet.getInt("CNE");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String birthD = resultSet.getString("birthDay");
                student stud = new student(cne, firstName, lastName,email,birthD);
                students.add(stud);
            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return students;
    }

    public void addStudent(String cne,String firstName,String lastName,String email,String birthDay){
        try {
            Connection conn = dataBaseConnection.getConnection();
            try {
                int cneNum = Integer.parseInt(cne);
                Statement statement = conn.createStatement();
                try {
                    //check if birthDay's format is valid
                    if (isValidBirthday(birthDay)) {
                        // Save the birthday to the database
                        statement.executeUpdate("INSERT INTO student " +
                                "VALUES ('" + cneNum + "', '" + firstName + "', '" + lastName + "', '" + email + "', '" + birthDay + "')");
                    } else {
                        // Show an alert if the birthday is invalid
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Birthday");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a valid birthday in the format xx/xx/xxxx");
                        alert.showAndWait();
                    }

                } catch (SQLIntegrityConstraintViolationException e) {
                    // CNE already exists in the database
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("This CNE is already exists.");
                    alert.showAndWait();
                }

                statement.close();
            } catch (NumberFormatException e) {
                // Display error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter integer value in cne.");
                alert.showAndWait();
            }
            conn.close();
            //View Refresh UI
            table.setItems(getStudent());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //check if id is already exits
    private boolean isIdExists(Connection connection, int id) throws SQLException {
        String query = "SELECT CNE FROM student WHERE CNE = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    //format of birthDay
    private boolean isValidBirthday(String birthday) {
        String pattern = "\\d{2}/\\d{2}/\\d{4}"; // Regular expression pattern
        return birthday.matches(pattern);
    }


    //delete student
    private void deleteStudent(int studentCNE) {
        try {
            Connection conn = dataBaseConnection.getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM student WHERE CNE = " + studentCNE );
            statement.close();
            conn.close();

            // Update UI or refresh student data
            table.setItems(getStudent());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateStudentDialog(student student) {
        // Create the update student dialog stage
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Update Student");

        // Create the grid pane for the dialog content
        //Student Information
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        //--------------Labels and textfields-------------------
        Label cneLabel = new Label("CNE:");
        GridPane.setConstraints(cneLabel,0,0);
        TextField cneTextField = new TextField();
        cneTextField.setEditable(false);
        cneTextField.setStyle("-fx-opacity: 1; -fx-text-fill: gray; -fx-focus-color: transparent;");
        GridPane.setConstraints(cneTextField,1,0);

        Label firstNameLabel = new Label("firstName:");
        GridPane.setConstraints(firstNameLabel,0,1);
        TextField firstNameTextField = new TextField();
        GridPane.setConstraints(firstNameTextField,1,1);

        Label lastNameLabel = new Label("lastName:");
        GridPane.setConstraints(lastNameLabel,0,2);
        TextField lastNameTextField = new TextField();
        GridPane.setConstraints(lastNameTextField,1,2);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel,0,3);
        TextField emailTextField = new TextField();
        GridPane.setConstraints(emailTextField,1,3);

        Label birthLabel = new Label("BirthDAY:");
        GridPane.setConstraints(birthLabel,0,4);
        TextField birthTextField = new TextField();
        GridPane.setConstraints(birthTextField,1,4);

        // Create the update button
        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color:orange;-fx-text-fill:white;");
        GridPane.setConstraints(updateButton,1,5);
        updateButton.setOnAction(e -> {
            // Handle the update button action
            int Cne =  Integer.parseInt(cneTextField.getText());
            String FirstName = firstNameTextField.getText();
            String LastName = lastNameTextField.getText();
            String Email = emailTextField.getText();
            String Birth = birthTextField.getText();

            // Perform the update operation with the new student details
            try {
                Connection conn = dataBaseConnection.getConnection();
                Statement statement = conn.createStatement();
                if (isValidBirthday(Birth)) {
                    // Update the database
                    statement.executeUpdate("UPDATE student SET CNE = '" + Cne + "', firstName = '" + FirstName + "', lastName = '"
                            + LastName + "', email = '" + Email + "', birthDay = '" + Birth + "' WHERE CNE = '" + Cne + "'");
                } else {
                    // Show an alert if the birthday is invalid
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Birthday");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid birthday in the format xx/xx/xxxx");
                    alert.showAndWait();
                }
                statement.close();
                conn.close();

                // Update UI or refresh student data
                table.setItems(getStudent());
            } catch (SQLException s) {
                s.printStackTrace();
            }

            // Close the dialog
            if (isValidBirthday(Birth)) dialogStage.close();
        });

        // Add the update button to the grid pane
        grid.getChildren().addAll(cneLabel,cneTextField,firstNameLabel,firstNameTextField,
                lastNameLabel,lastNameTextField,emailLabel,emailTextField,birthLabel,birthTextField,updateButton);

        // Create the scene and set it on the dialog stage
        Scene dialogScene = new Scene(grid,300,200);
        dialogStage.setScene(dialogScene);

        // Set the initial student details in the text fields
        cneTextField.setText(String.valueOf(student.getCNE()));
        firstNameTextField.setText(student.getFirstName());
        lastNameTextField.setText(student.getLastName());
        emailTextField.setText(student.getEmail());
        birthTextField.setText(student.getBirthDay());

        // Show the update student dialog
        dialogStage.showAndWait();
    }
    private void  openProfileStudentDialog(student student){
        // Create the update student dialog stage
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Profile Student");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        Label cneLabel = new Label("CNE: "+String.valueOf(student.getCNE()));
        GridPane.setConstraints(cneLabel,0,0);

        Label firstNameLabel = new Label("first name: "+student.getFirstName());
        GridPane.setConstraints(firstNameLabel,0,1);

        Label lastNameLabel = new Label("last name: "+student.getLastName());
        GridPane.setConstraints(lastNameLabel,0,2);

        Label emailLabel = new Label("email: "+student.getEmail());
        GridPane.setConstraints(emailLabel,0,3);

        Label birthDayLabel = new Label("birth day: "+student.getBirthDay());
        GridPane.setConstraints(birthDayLabel,0,4);

        Button getPdfBtn = new Button("get pdf file");
        getPdfBtn.setStyle("-fx-background-color:#264653;-fx-text-fill:white;");
        GridPane.setConstraints(getPdfBtn,0,5);
        getPdfBtn.setOnAction(event -> {
            //generatePDF(cneLabel.getText(),firstNameLabel.getText()
       // ,lastNameLabel.getText(),emailLabel.getText(),birthDayLabel.getText())
            //
            printScene(dialogStage,cneLabel.getText(),firstNameLabel.getText(),
                    lastNameLabel.getText(),emailLabel.getText(),birthDayLabel.getText());
        dialogStage.close();});

        gridPane.getChildren().addAll(cneLabel,firstNameLabel,lastNameLabel,emailLabel,birthDayLabel,getPdfBtn);

        Scene dialogueScene = new Scene(gridPane,300,200);

        dialogStage.setScene(dialogueScene);

        dialogStage.showAndWait();


    }

    private void printScene(Stage S,String cne,String firstName,String lastName,String email,String birthDay){
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(S)) {
            Text tt = new Text("Registration form:");
            Text Tv = new Text("______________________________");
            Label cneLabel = new Label(cne);
            Label firstNameLabel = new Label(firstName);
            Label lastNameLabel = new Label(lastName);
            Label emailLabel = new Label(email);
            Label birthDayLabel = new Label(birthDay);
            VBox vBox = new VBox();
            vBox.getChildren().addAll(tt,Tv,cneLabel,firstNameLabel,lastNameLabel,emailLabel,birthDayLabel);
            boolean success = printerJob.printPage(vBox);
            if (success) {
                printerJob.endJob();
            }
        }
    }

}