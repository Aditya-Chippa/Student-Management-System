package SMS;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JdbcConn {

    private static final String URL = "jdbc:postgresql://localhost:5432/studentdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "031207";

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTable(String table, String sql) {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println(table + " created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertStudents(String tableName, Collection<Student> students, String query) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            for (Student s : students) {
                pstmt.setInt(1, s.getId());
                pstmt.setString(2, s.getName());
                pstmt.setInt(3, s.getAge());
                pstmt.setString(4, s.getCourseName());
                pstmt.addBatch();
            }
            int[] rows = pstmt.executeBatch();
            System.out.println(rows.length + " student(s) inserted into " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Student> readAll(String query) {
        List<Student> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course_name")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateCourseName(int id, String newCourseName) {
        String sql = "UPDATE studentdetails SET course_name = ? WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newCourseName);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " student(s) updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id) {
        String sql = "DELETE FROM studentdetails WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " student(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inner class Student
    public static class Student {
        private int id;
        private String name;
        private int age;
        private String courseName;

        public Student(int id, String name, int age, String courseName) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.courseName = courseName;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getCourseName() { return courseName; }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        @Override
        public String toString() {
            return "Student [id=" + id + ", name=" + name + ", age=" + age + ", courseName=" + courseName + "]";
        }
    }
}
