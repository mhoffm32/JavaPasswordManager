package Package1;
import java.util.HashMap;
import java.util.Scanner;

//Class to hold app, passwords, entrance password
public class Password {
    //testing changes
    public static Scanner input = new Scanner(System.in);
    PassAdapter pAdapter;
    private HashMap<String,String> PassMap;
    private String entrancePassword;

    Password(){}
    Password(HashMap<String,String> myPassMap){ //Constructor to make Password obj
       this.PassMap = myPassMap;
   }
    public void setEntrancePassword(String entrancePassword) { //setting entrance password
        this.entrancePassword = entrancePassword;
    }
    public String getEntrancePassword(){ //get entrance pass
       return entrancePassword;
    }
    public void setpassMap(HashMap<String,String> myPassMap) {
        this.PassMap = myPassMap;
    }

    public void changeEntrancePassword(){ //to change password protecting the database
       boolean same = false;
       do{
           System.out.print("Enter your old entrance password: ");
           //user enters password twice to change it
           String oP = input.next();
           if(oP.equals(getEntrancePassword())) {
               System.out.print("\n");
               same = true;
               System.out.println("Correct.");
               String newP = PasswordSafety();
               setEntrancePassword(newP); //setting new entrance password
               System.out.println(getEntrancePassword() + " is your new entrance password.");
           }
           else {
               System.out.println("Incorrect");
           }
       }while(!same); //keeps asking for input if you get initial password wrong
    }


    public HashMap<String,String> changePassword(HashMap<String,String> passWords) {
       //to change a password in hashmap, returns the modified hashmap
        System.out.print("Enter the application to which you would like to change the password: ");
        String app1 = input.next();

        if (passWords.containsKey(app1)) {
            //verifying that the database contains a password for this app
            passWords.replace(app1,PasswordSafety());

        } else {
            System.out.println("The password database does not contain data for " +
                    app1 + ". " + "Type 'A' if you would like to add it.");
            //allows user to add new Key Value pair if key does not exist
            String choice = input.nextLine();
            if (choice.equals("A")){
                addPassword(passWords);
            }
        }
        return passWords; //returns modified hashmap
    }
    public void viewPasswords(HashMap<String,String> passWords){ //prints out the database
        System.out.println("\n" + passWords);
        System.out.print("\nTo search for a specific password," +
                " enter the application you need the password to: ");
        //allows user to search for a specific password using the corresponding app.
        String app = input.next();
        if (passWords.containsKey(app)) {
            System.out.println("The password for " + app + " is " +
                    passWords.get(app) + "\n"); //hashmap get method to return corresponsing password
        } else {
            System.out.println("The password database does not contain data for " +
                    app + ". " + "Type 'A' to add a password for " + app);
            String choice = input.next();
            if (choice.equals("A")) {
                addPassword(passWords);
            }
        }
    }
    public HashMap<String,String> addPassword(HashMap<String,String> passWords) {//takes in hashmap parameter to add passwords to
        System.out.print("Enter the application: ");
        String app = input.next();
        if (passWords.containsKey(app)) { //checks to see if a password for this app exists already
            System.out.println("You already have a a password stored for " + app);
        } else {
            passWords.put(app,PasswordSafety());
            //if it doesnt already exist, adds a new app password pair to the hashmap
        }
        return passWords; //returns the revised database
    }

    public HashMap<String,String> deletePassword(HashMap<String,String> passWords) {//takes in hashmap parameter to delete passwords from
        System.out.print("Enter the application you would like to delete data for: ");
        String app = input.next();
        if (passWords.containsKey(app)) {//checks to see if data is stored for this key value pair
            System.out.println("{"+app+"="+passWords.get(app)+"}"); //prints the current pair
            passWords.remove(app); //deletes the pair
            System.out.println("\nData for " + app + " has been deleted.");
        } else {
            System.out.println("Data for " + app + " does not exist in the database.");
            //if it doesnt exist in map
        }
        return passWords; //returns the revised database
    }


    public String PasswordSafety() { //returns a safe password
        boolean isSafe = false; //returns true if password meets security criteria
        String myPassword;
        do {
            System.out.print("Enter password: ");
            myPassword = input.next();
            boolean digitSafe = true, sCharSafe = true, lengthSafe = true,
                    lowerCaseSafe = true, upperCaseSafe = true;
            int upperCase = 0, lowerCase = 0, sCharCount = 0, digitCount = 0;

            for (int i = 0; i < myPassword.length(); i++) { //iterating through characters of password
                //checking if special char exists;
                if (!Character.isDigit(myPassword.charAt(i))
                        && !Character.isLetter(myPassword.charAt(i))
                        && !Character.isWhitespace(myPassword.charAt(i))) {
                    sCharCount++; //increases when special char is detected
                }
                //numbers increases if password contains digits;
                if (Character.isDigit(myPassword.charAt(i))) {
                    digitCount++;
                }
                //counts lower case
                if (Character.isLowerCase(myPassword.charAt(i))) {
                    lowerCase++;
                } //counts upper case

                if (Character.isUpperCase(myPassword.charAt(i))) {
                    upperCase++;
                }
            }
            if (digitCount < 2) { //safe if contains more than 2 digits
                digitSafe = false;
                System.out.println("Password must contain at least " + (2 - digitCount) + " more digits.");
            }
            if (sCharCount == 0) { //safe if contains a special character
                sCharSafe = false;
                System.out.println("Password must contain at least 1 special character.");
            }
            if (lowerCase == 0) { //safe if contains at least 1 lowercase
                lowerCaseSafe = false;
                System.out.println("Password must contain at least 1 lower case character.");
            }
            if (upperCase == 0) { //safe if contains at least 1 lowercase
                upperCaseSafe = false;
                System.out.println("Password must contain at least 1 upper case character.");
            }
            if (myPassword.length() < 12) { //safe if contains 12 characters or more
                lengthSafe = false;
                System.out.println("Password must contain at least 12 characters.");
            }

            if (digitSafe && sCharSafe && lowerCaseSafe && upperCaseSafe && lengthSafe) {
                System.out.println("Password is safe. Saved to the database. ");
                isSafe = true; //loop breaks when password is safe
            }
        } while (!isSafe); //asks user for password until its safe

        return myPassword; //returns string of the accepted password.
    }

    public void setAdapter(PassAdapter adapter){
        pAdapter = adapter;
    }

}
