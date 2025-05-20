package model;

public class Student extends Person {
    private String course;
    private int marks;
    private String grade;
    private double attendancePercentage;
    private boolean examEligible;

    public Student(String name, int age, String gender, String course, int marks, double attendancePercentage) {
        super(name, age, gender);
        this.course = course;
        this.marks = marks;
        this.attendancePercentage = attendancePercentage;
        this.grade = calculateGrade();
        this.examEligible = isExamEligible();
    }

    public String calculateGrade() {
        if (marks >= 90) return "O";
        else if (marks >= 80) return "A+";
        else if (marks >= 70) return "A";
        else if (marks >= 60) return "B+";
        else if (marks >= 50) return "B";
        else return "E";
    }
    
    public boolean isExamEligible() {
        // Students need at least 75% attendance to be eligible for exams
        return attendancePercentage >= 75.0;
    }

    // Getters and Setters
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) {
        this.marks = marks;
        this.grade = calculateGrade();
    }
    
    public double getAttendancePercentage() { return attendancePercentage; }
    
    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
        this.examEligible = isExamEligible();
    }
    
    public boolean isExamEligible() { return examEligible; }

    public String getGrade() { return grade; }
}