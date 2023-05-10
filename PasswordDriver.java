package Package1;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordDriver {

    private static Connection conn;
    //entering file paths, and creating file objects to save new and modified key value pairs
    public static Scanner input = new Scanner(System.in); //to get user input
    final static String filePath1 = "passwordStorage.txt";
    final static String filePath2 = "EntrancePassword.txt";
    public static File file1 = new File(filePath1); //file object
    public static File file2 = new File(filePath2);

    public static void main(String[] args) throws SQLException {

        connectDB();
        PassAdapter passwordAdapter = new PassAdapter(conn,false);
        Password p = new Password();
        p.setAdapter(passwordAdapter);
        HashMap<String, String> passwordDatabase = passwordAdapter.getPassMap();

        p.setpassMap(passwordDatabase); //Password Object

        readToFile(passwordDatabase,p); //reads file and writes the hashmap to input saved from previous run
        boolean allowed; //to check if user is authorized to enter the program
        boolean terminate = false;
        System.out.print("\nEnter the Password to the Password database: ");
        String pass = input.next();
        if (pass.equals(p.getEntrancePassword())) { //if user input of password is correct
            allowed = true;
        } else {
            allowed = false;
            System.out.println("Incorrect Password");
        }
        if (allowed) { //if authorized
            do { //user keeps going until done
                System.out.print("\n");
                System.out.printf("%30s","HOME"); //Home screen header
                System.out.println("\n-------------------------------------------------------------");
                System.out.println("\nType key to select Action:"); //prints all user options
                System.out.print(""" 

                        A --> Add Password
                        C --> Change Password
                        D --> Delete Password
                        V --> View Passwords
                        P --> Change entrance Password
                        T --> Save progress and terminate program
                        \s""");
                String index = input.next();
                switch (index) {
                    case "A" -> {
                        System.out.printf("%30s","ADD PASSWORD");
                        System.out.println("\n-------------------------------------------------------------");
                        passwordDatabase = p.addPassword(passwordDatabase); //calls p instance of Password with add password method
                        System.out.println("\n-------------------------------------------------------------");
                    }
                    case "D" ->{
                        System.out.printf("%30s","DELETE PASSWORD");
                        System.out.println("\n-------------------------------------------------------------");
                        passwordDatabase = p.deletePassword(passwordDatabase); //calls delete pass method
                        System.out.println("\n-------------------------------------------------------------");
                    }
                    case "C" -> {
                        System.out.printf("%30s","CHANGE PASSWORD");
                        System.out.println("\n-------------------------------------------------------------");
                        passwordDatabase = p.changePassword(passwordDatabase); //calls change pass method
                        System.out.println("\n-------------------------------------------------------------");
                    }
                    case "V" -> { System.out.printf("%30s","VIEW PASSWORDS");
                        System.out.println("\n-------------------------------------------------------------");
                        p.viewPasswords(passwordDatabase); //calls view pass method
                        System.out.println("\n-------------------------------------------------------------");
                    }
                    case "P"-> {
                        System.out.printf("%30s","CHANGE ENTRANCE PASSWORD");
                        System.out.println("\n-------------------------------------------------------------");
                        p.changeEntrancePassword(); //calls change enterance password method
                        System.out.println("\n-------------------------------------------------------------");
                    }
                    case "T" -> terminate = true; //if user selects T the progress is saved and program is terminated

                    default -> System.out.println("Invalid input");

                }
                writeToFile(passwordDatabase,p); //overwrites modified data to files
            } while (!terminate); //user continues doing actions until they chose to terminate program
        }
    }

    public static void writeToFile(HashMap<String, String> passWords, Password p) {
        //takes in parameters of hashmap and the entrance password
        try (BufferedWriter bf2 = new BufferedWriter(new FileWriter(file1))) {
            // iterate map entries
            for (Map.Entry<String, String> entry : passWords.entrySet()) {
                //writes values to file seperated by colon
                bf2.write(entry.getKey() + ":"
                        + entry.getValue());
                // new line
                bf2.newLine();
            }
            bf2.flush(); //emptys bf2
        } catch (IOException e) { //exception
            e.printStackTrace();
        }
        try (BufferedWriter bf2 = new BufferedWriter(new FileWriter(file2))) {  //writes the entrance password
            bf2.write(p.getEntrancePassword());
            bf2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readToFile(HashMap<String, String> passWords, Password p) {
        //reads file to get saved data from previous run
        BufferedReader br1 = null; //initialized as null
        try {
            // create file object
            File file1 = new File(filePath1);
            //BufferedReader object from the File
            br1 = new BufferedReader(new FileReader(file1));
            String line;

            // read file line by line
            while ((line = br1.readLine()) != null) { //checks all lines for data
                String[] parts = line.split(":");
                //splits apart the line and turns into array for each Key Value pair

                String app = parts[0].trim(); //arr at 0 is the app
                String pass = parts[1].trim(); //arr at 1 is the corresponding password

                if (!app.equals("") && !pass.equals("")) //boolean if both are not empty insert to hashmap
                    passWords.put(app, pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the BufferedReader
            if (br1 != null) {
                try {
                    br1.close();
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        }

        BufferedReader br2 = null; //Same thing but for the entrance password
        try {

            File file2 = new File(filePath2);
            br2 = new BufferedReader(new FileReader(file2));
            String line = br2.readLine();
            if(line != null){
                p.setEntrancePassword(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (br2 != null) {
                try {
                    br2.close();
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        }
    }

    public static void connectDB(){
        try{
            // Create a named constant for the URL
            // NOTE: This value is specific for Java DB
            String DB_URL = "jdbc:derby:passwordDB;create=true";
            // Create a connection to the database
            conn = DriverManager.getConnection(DB_URL);
            // Create the admin account if it is not already in the database
            new PassAdapter(conn,false);

        } catch (SQLException ex) {
            System.out.println("Failed to create table");
        }
    }



}
