package util;

import model.Student;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_PATH = "resources/students.csv";

    public static void saveStudents(List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student s : students) {
                bw.write(String.join(",",
                        s.getName(),
                        String.valueOf(s.getAge()),
                        s.getGender(),
                        s.getCourse(),
                        String.valueOf(s.getMarks()),
                        s.getGrade(),
                        String.valueOf(s.getAttendancePercentage()),
                        String.valueOf(s.isExamEligible())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Student> loadStudents() {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    double attendance = 0.0;
                    if (parts.length >= 8) {
                        try {
                            attendance = Double.parseDouble(parts[7]);
                        } catch (NumberFormatException e) {
                            // Default to 0 if parsing fails
                            attendance = 0.0;
                        }
                    }
                    
                    Student s = new Student(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3],
                        Integer.parseInt(parts[4]),
                        attendance
                    );
                    list.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}