package SMS;


import SMS.JdbcConn.Student;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        String table = "studentdetails";

        
        String createQuery = "CREATE TABLE IF NOT EXISTS " + table +
                " (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL, " +
                "course_name VARCHAR(100) NOT NULL" +
                ")";
        JdbcConn.createTable(table, createQuery);

        
        Collection<Student> students = new ArrayList<>();
        students.add(new Student(1, "Aditya", 18, "CSE"));
        students.add(new Student(2, "Sheshadri", 19, "MECH"));
        students.add(new Student(3, "Yash", 17, "CIVIL"));
        String insertQuery = "INSERT INTO " + table + " (id, name, age, course_name) VALUES (?, ?, ?, ?)";
        JdbcConn.insertStudents(table, students, insertQuery);

        List<Student> allStudents = JdbcConn.readAll("SELECT * FROM " + table);
        allStudents.forEach(System.out::println);

        //Update Course
        System.out.println("\nUpdating course for student with ID 2...");
        JdbcConn.updateCourseName(2, "MBA");

        //Delete Student 
        System.out.println("\nDeleting student with ID 1...");
        JdbcConn.deleteStudent(1);

        System.out.println("\nStudents after update and delete:");
        JdbcConn.readAll("SELECT * FROM " + table).forEach(System.out::println);
    }
}
