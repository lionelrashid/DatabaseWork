import javax.xml.transform.Result;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//Main Class
public class Main
{
    //Global variables used to access db
    static final String url = "jdbc:postgresql://localhost:5432/A3_3005";
    static final String user = "postgres";
    static final String password = "admin";

    //Main function to call methods
    public static void main(String[] args)
    {

        getAllStudents();
        //addStudent("Might","Guy","might.guy@gmail.com","2023-09-03");
        //getAllStudents();
        //updateStudentEmail(7, "new@gmail.com");
        //deleteStudent(7);



        /*
        String url = "jdbc:postgresql://localhost:5432/A3_3005";
        String user = "postgres";
        String password = "admin";
        try
        {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url,user,password);

            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students");
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next())
            {
                System.out.println(resultSet.getString("first_name"));
            }
        }

        catch(Exception e)
        {
            System.out.println(e);
        }

         */
    }

    //Get all students method
    public static void getAllStudents()
    {
        //try to connect to db and return all students
        try
        {
            Class.forName("org.postgresql.Driver");
            //connecting to db and requesting student data
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM students");

            //print formatting for information
            System.out.println("All Students:");
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("%-12s %-15s %-15s %-30s %s\n", "student_id", "first_name", "last_name", "email", "enrollment_date");
            System.out.println("----------------------------------------------------------------------------------------");

            //retrieving data for each student in database
            while(rs.next())
            {
                String studentId = rs.getString("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String enrollmentDate = rs.getString("enrollment_date");

                System.out.printf("%-12s %-15s %-15s %-30s %s\n", studentId, firstName, lastName, email, enrollmentDate);
            }

            //close resources and connection
            rs.close();
            statement.close();
            connection.close();

            //more formatting
            System.out.println("----------------------------------------------------------------------------------------");
        }

        //catch for any sql errors
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //Add student to database method
    public static void addStudent(String first_name, String last_name, String email, String enrollment_date)
    {
        //try and catch for errors while adding students
        try
        {
            Class.forName("org.postgresql.Driver");

            //connecting to database
            Connection connection = DriverManager.getConnection(url, user, password);

            //query for inserting students into database
            String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

            //Setting values for student being added
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL))
            {
                pstmt.setString(1, first_name);
                pstmt.setString(2, last_name);
                pstmt.setString(3, email);

                //Since enrollment is a date object, adjustments had to be made for insertion
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat.parse(enrollment_date);
                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                pstmt.setDate(4, sqlDate);

                //Adding student to db
                pstmt.executeUpdate();
                System.out.println("Data inserted using PreparedStatement.");
            }

            connection.close();


            //success
            System.out.println("Student added successfully.");
        }

        //catch for errors
        catch (SQLException | ParseException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //Update student email method
    public static void updateStudentEmail(Integer student_id, String new_email)
    {

        //try and catch for any errors
        try
        {
            Class.forName("org.postgresql.Driver");

            //connecting to db
            Connection connection = DriverManager.getConnection(url, user, password);

            //query for updating student email based on id
            String updateQuery = "UPDATE students SET email = ? WHERE student_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, new_email);
            preparedStatement.setInt(2, student_id);
            int updatedRows = preparedStatement.executeUpdate();

            //checking to see if email was updated
            if (updatedRows > 0)
            {
                System.out.println("Email updated successfully.");
            }

            else
            {
                System.out.println("ID not found.");
            }

            //close resources
            connection.close();
            preparedStatement.close();
        }

        //catch any exception errors
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //Delete student by id method
    public static void deleteStudent(int student_id)
    {
        try
        {
            Class.forName("org.postgresql.Driver");

            //connecting to db
            Connection connection = DriverManager.getConnection(url, user, password);

            //querying for deleting student based on id
            String deleteQuery = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, student_id);
            int deletedRows = preparedStatement.executeUpdate();

            //checking to see if student was deleted
            if (deletedRows > 0)
            {
                System.out.println("Student deleted successfully.");
            }

            //student id doesn't exist
            else
            {
                System.out.println("ID not found.");
            }

            //resources close
            connection.close();
            preparedStatement.close();


        }

        //catch any errors
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }



}
