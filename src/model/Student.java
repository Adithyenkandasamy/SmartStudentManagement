package model;

public class Student extends Person {
    private String course;
    private int marks;
    private String grade;

    public Student(String name, int age, String gender, String course, int marks) {
        super(name, age, gender);
        this.course = course;
        this.marks = marks;
        this.grade = calculateGrade();
    }

    public String calculateGrade() {
        if (marks >= 90) return "O";
        else if (marks >= 80) return "A+";
        else if (marks >= 70) return "A";
        else if (marks >= 60) return "B+";
        else if (marks >= 50) return "B";
        else return "E";
    }

    // Getters and Setters
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) {
        this.marks = marks;
        this.grade = calculateGrade();
    }

    public String getGrade() { return grade; }
}