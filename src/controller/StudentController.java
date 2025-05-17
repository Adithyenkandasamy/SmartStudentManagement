package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Student;
import util.FileHandler;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    
    /**
     * Gets the appropriate CSS class for a course
     * @param course The course name
     * @return The CSS class name for the course
     */
    private String getCourseStyleClass(String course) {
        if (course == null) return "";
        
        switch (course) {
            case "EEE": return "course-eee";
            case "ECE": return "course-ece";
            case "CSE": return "course-cse";
            case "IT": return "course-it";
            case "AI&DS": return "course-aids";
            case "AI&ML": return "course-aiml";
            case "CSE(IoT)": return "course-cse-iot";
            case "CSD": return "course-csd";
            case "CST": return "course-cst";
            case "MECH": return "course-mech";
            default: return "";
        }
    }
    @FXML
    private TableView<Student> studentTable;
    
    @FXML
    private TableColumn<Student, String> nameColumn;
    
    @FXML
    private TableColumn<Student, Integer> ageColumn;
    
    @FXML
    private TableColumn<Student, String> genderColumn;
    
    @FXML
    private TableColumn<Student, String> courseColumn;
    
    @FXML
    private TableColumn<Student, Integer> marksColumn;
    
    @FXML
    private TableColumn<Student, String> gradeColumn;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField ageField;
    
    @FXML
    private ComboBox<String> genderComboBox;
    
    @FXML
    private ComboBox<String> courseComboBox;
    
    @FXML
    private TextField marksField;
    
    @FXML
    private Label statusLabel;
    
    private ObservableList<Student> studentList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the gender combo box
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        
        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // Set up custom cell factory for course column to apply course-specific styling
        courseColumn.setCellFactory(column -> {
            return new TableCell<Student, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        getStyleClass().removeAll(getStyleClass().stream()
                            .filter(style -> style.startsWith("course-"))
                            .toArray(String[]::new));
                    } else {
                        setText(item);
                        getStyleClass().removeAll(getStyleClass().stream()
                            .filter(style -> style.startsWith("course-"))
                            .toArray(String[]::new));
                        
                        // Apply course-specific styling
                        String courseClass = getCourseStyleClass(item);
                        getStyleClass().addAll("course-label", courseClass);
                    }
                }
            };
        });
        
        // Load students from file
        loadStudentsFromFile();
        
        // Set up table selection listener
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                ageField.setText(String.valueOf(newSelection.getAge()));
                genderComboBox.setValue(newSelection.getGender());
                courseComboBox.setValue(newSelection.getCourse());
                marksField.setText(String.valueOf(newSelection.getMarks()));
            }
        });
    }
    
    private void loadStudentsFromFile() {
        List<Student> students = FileHandler.loadStudents();
        studentList = FXCollections.observableArrayList(students);
        studentTable.setItems(studentList);
        updateStatus("Loaded " + students.size() + " students from file");
    }
    
    @FXML
    private void handleAddStudent() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderComboBox.getValue();
            String course = courseComboBox.getValue();
            int marks = Integer.parseInt(marksField.getText());
            
            if (name.isEmpty() || gender == null || course == null) {
                updateStatus("Please fill all fields");
                return;
            }
            
            Student student = new Student(name, age, gender, course, marks);
            studentList.add(student);
            clearFields();
            updateStatus("Student added successfully");
        } catch (NumberFormatException e) {
            updateStatus("Please enter valid numbers for age and marks");
        }
    }
    
    @FXML
    private void handleUpdateStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            updateStatus("Please select a student to update");
            return;
        }
        
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderComboBox.getValue();
            String course = courseComboBox.getValue();
            int marks = Integer.parseInt(marksField.getText());
            
            if (name.isEmpty() || gender == null || course == null) {
                updateStatus("Please fill all fields");
                return;
            }
            
            selectedStudent.setName(name);
            selectedStudent.setAge(age);
            selectedStudent.setGender(gender);
            selectedStudent.setCourse(course);
            selectedStudent.setMarks(marks);
            
            studentTable.refresh();
            clearFields();
            updateStatus("Student updated successfully");
        } catch (NumberFormatException e) {
            updateStatus("Please enter valid numbers for age and marks");
        }
    }
    
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            updateStatus("Please select a student to delete");
            return;
        }
        
        studentList.remove(selectedStudent);
        clearFields();
        updateStatus("Student deleted successfully");
    }
    
    @FXML
    private void handleSaveToFile() {
        FileHandler.saveStudents(studentList);
        updateStatus("Students saved to file successfully");
    }
    
    private void clearFields() {
        nameField.clear();
        ageField.clear();
        genderComboBox.setValue(null);
        courseComboBox.setValue(null);
        marksField.clear();
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    @FXML
    private void handleViewDashboard() {
        try {
            // Save students before navigating to dashboard
            FileHandler.saveStudents(studentList);
            
            // Load the dashboard view
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setTitle("Student Performance Dashboard");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            updateStatus("Error loading dashboard view");
            e.printStackTrace();
        }
    }
}