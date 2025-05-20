package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Student;
import util.FileHandler;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {
    
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
    private Label highestMarksLabel;
    
    @FXML
    private Label highestMarksStudentLabel;
    
    @FXML
    private Label highestMaleMarksLabel;
    
    @FXML
    private Label highestMaleStudentLabel;
    
    @FXML
    private Label highestFemaleMarksLabel;
    
    @FXML
    private Label highestFemaleStudentLabel;
    
    @FXML
    private Label averageMarksLabel;
    
    @FXML
    private Label totalStudentsLabel;
    
    @FXML
    private Label passRateLabel;
    
    @FXML
    private Label lastUpdatedLabel;
    
    @FXML
    private PieChart gradeDistributionChart;
    
    @FXML
    private BarChart<String, Number> coursePerformanceChart;
    
    @FXML
    private Label avgAttendanceLabel;
    
    @FXML
    private Label eligibleStudentsLabel;
    
    @FXML
    private Label ineligibleStudentsLabel;
    
    private List<Student> students;
    private DecimalFormat df = new DecimalFormat("0.00");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load students from file
        students = FileHandler.loadStudents();
        
        // We need to resize the window after the scene is fully initialized
        javafx.application.Platform.runLater(() -> {
            try {
                Stage stage = (Stage) gradeDistributionChart.getScene().getWindow();
                stage.setWidth(800);
                stage.setHeight(700);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Update performance metrics
        updatePerformanceMetrics();
        
        // Update grade distribution chart
        updateGradeDistributionChart();
        
        // Update course performance chart
        updateCoursePerformanceChart();
        
        // Update attendance summary
        updateAttendanceSummary();
        
        // Update timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lastUpdatedLabel.setText("Last updated: " + LocalDateTime.now().format(formatter));
    }
    
    private void updateAttendanceSummary() {
        if (students != null && !students.isEmpty()) {
            // Calculate average attendance
            double avgAttendance = students.stream()
                .mapToDouble(Student::getAttendancePercentage)
                .average()
                .orElse(0.0);
            
            // Count eligible and ineligible students
            long eligibleCount = students.stream()
                .filter(Student::isExamEligible)
                .count();
            
            long ineligibleCount = students.size() - eligibleCount;
            
            // Update labels
            avgAttendanceLabel.setText(df.format(avgAttendance) + "%");
            eligibleStudentsLabel.setText(String.valueOf(eligibleCount));
            ineligibleStudentsLabel.setText(String.valueOf(ineligibleCount));
        } else {
            avgAttendanceLabel.setText("0%");
            eligibleStudentsLabel.setText("0");
            ineligibleStudentsLabel.setText("0");
        }
    }
    
    private void updatePerformanceMetrics() {
        int totalStudents = students.size();
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        
        if (totalStudents > 0) {
            // Find highest marks and student
            Student highestStudent = students.stream()
                    .max(Comparator.comparingDouble(Student::getMarks))
                    .orElse(null);
            
            if (highestStudent != null) {
                highestMarksLabel.setText(String.valueOf(highestStudent.getMarks()));
                highestMarksStudentLabel.setText(highestStudent.getName() + " (" + highestStudent.getCourse() + ")");
            }
            
            // Find highest male student
            Student highestMaleStudent = students.stream()
                    .filter(student -> "Male".equals(student.getGender()))
                    .max(Comparator.comparingDouble(Student::getMarks))
                    .orElse(null);
            
            if (highestMaleStudent != null) {
                highestMaleMarksLabel.setText(String.valueOf(highestMaleStudent.getMarks()));
                highestMaleStudentLabel.setText(highestMaleStudent.getName());
            } else {
                highestMaleMarksLabel.setText("0");
                highestMaleStudentLabel.setText("No male students");
            }
            
            // Find highest female student
            Student highestFemaleStudent = students.stream()
                    .filter(student -> "Female".equals(student.getGender()))
                    .max(Comparator.comparingDouble(Student::getMarks))
                    .orElse(null);
            
            if (highestFemaleStudent != null) {
                highestFemaleMarksLabel.setText(String.valueOf(highestFemaleStudent.getMarks()));
                highestFemaleStudentLabel.setText(highestFemaleStudent.getName());
            } else {
                highestFemaleMarksLabel.setText("0");
                highestFemaleStudentLabel.setText("No female students");
            }
            
            // Calculate average marks
            double averageMarks = students.stream()
                    .mapToDouble(Student::getMarks)
                    .average()
                    .orElse(0.0);
            averageMarksLabel.setText(df.format(averageMarks));
            
            // Calculate pass rate
            long passCount = students.stream()
                    .filter(student -> student.getMarks() >= 50.0) // Assuming 50 is pass mark
                    .count();
            double passRate = (double) passCount / totalStudents * 100;
            passRateLabel.setText(df.format(passRate) + "%");
        } else {
            highestMarksLabel.setText("0");
            highestMarksStudentLabel.setText("No students");
            highestMaleMarksLabel.setText("0");
            highestMaleStudentLabel.setText("No male students");
            highestFemaleMarksLabel.setText("0");
            highestFemaleStudentLabel.setText("No female students");
            averageMarksLabel.setText("0.0");
            passRateLabel.setText("0%");
        }
        // Update attendance summary
        updateAttendanceSummary();
    }
    
    private void updateGradeDistributionChart() {
        // Count students by grade
        Map<String, Long> gradeCount = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        gradeCount.forEach((grade, count) -> 
            pieChartData.add(new PieChart.Data(grade + " (" + count + ")", count))
        );
        
        gradeDistributionChart.setData(pieChartData);
    }
    
    private void updateCoursePerformanceChart() {
        // Calculate average marks by course
        Map<String, Double> courseAvgMarks = students.stream()
                .collect(Collectors.groupingBy(
                    Student::getCourse,
                    Collectors.averagingDouble(Student::getMarks)
                ));
        
        // Create bar chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Marks");
        
        courseAvgMarks.forEach((course, avgMarks) -> 
            series.getData().add(new XYChart.Data<>(course, avgMarks))
        );
        
        coursePerformanceChart.getData().clear();
        coursePerformanceChart.getData().add(series);
        
        // Apply custom colors to bars based on course
        for (XYChart.Data<String, Number> data : series.getData()) {
            String course = data.getXValue();
            String courseClass = getCourseStyleClass(course);
            
            // Apply the course-specific color when the node is created
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    // Remove any existing course style classes
                    newNode.getStyleClass().removeAll(newNode.getStyleClass().stream()
                        .filter(style -> style.startsWith("course-"))
                        .toArray(String[]::new));
                    
                    // Add the appropriate course style class
                    newNode.getStyleClass().add(courseClass);
                }
            });
        }
    }
    

    
    @FXML
    private void handleBackToStudents() {
        try {
            // Load the student management view
            Parent root = FXMLLoader.load(getClass().getResource("/view/student.fxml"));
            Stage stage = (Stage) highestMarksLabel.getScene().getWindow();
            stage.setTitle("Student Management");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
