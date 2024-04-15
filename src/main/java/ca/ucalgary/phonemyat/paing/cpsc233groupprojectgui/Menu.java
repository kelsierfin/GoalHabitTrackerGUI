package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileLoader;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileSaver;

import java.io.File;
import java.util.*;


/** This class interacts with the user to get inputs. It displays the menu of the habit tracker.
 * @author : Tania Rizwan, Sanbeer Shafin, Phone Myat
 * Tutorial:T10, T09, T16
 * Emails: tania.rizwan@ucalgary.ca, sanbeer.shafin@ucalgary.ca, phonemyat.paing@ucalgary.ca
 */

// The menu setup code was followed from Dr. Hudson's project example.
public class Menu {

    private static Data data = new Data();

    // Create scanner object for user input.
    // Private so it is only accessible within the core.Menu class.
    private static final Scanner scanner = new Scanner(System.in);

    // Create menu for user interaction.
    // Use a static initializer. This initializes the ArrayList when we load the core.Menu class.
    private static final ArrayList<String> options = new ArrayList<>();
    static {
        options.add("Exit"); // At index 0
        options.add("Create A Goal");
        options.add("Delete a Goal");
        options.add("Add Habits to A Goal");
        options.add("Delete Habits From A Goal");
        options.add("Categorize Goals");
        options.add("Show Categorized Goals");
        options.add("Create Eisenhower Matrix");
        options.add("Show Current Eisenhower Matrix");
        options.add("Add Points to Habit");
        options.add("Weekly Habit Completion Rate");
        options.add("List Top 3 Habits of the Week");
        options.add("View Tracker");
        options.add("Load Data");
        options.add("Save Data");
        options.add("Reset Data");


    }

    // Create message to display to user. Then use static initializer to combine message and Options into one menu.
    private static String optMessage = """
            Welcome to your personal habit and goal tracker!
            \t Menu Options:
            """;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append(optMessage);
        for (int i = 0; i < options.size(); i++) {
            sb.append(String.format("\t%d. %s\n", i, options.get(i) )); // Append the index and menu item
        }
        optMessage = sb.toString(); // Update the optMessage object with the final menu
    }

    // Run the menuLoop everytime Main is called
    public static void menuLoop(File file) {

        if (file != null) {
            load(file);
        }
        System.out.println(optMessage);
        String choice = scanner.nextLine();
        int option = Integer.parseInt(choice);

        while (option != 0) {
            // Tell the user what option they selected. Then Continue
            if (option > 0 && option < options.size()) {
                System.out.printf("You have selected option %d. %s%n", option, options.get(option));
                System.out.println("Press Enter to continue:");
                scanner.nextLine();
            }

            switch (option) {
                case 1 -> menuCreateGoal();
                case 2 -> menuDeleteGoal();
                case 3 -> menuAddHabits();
                case 4 -> menuDeleteHabits();
                case 5 -> menuCategorizeGoals();
                case 6 -> menuShowCategorizedGoals();
                case 7 -> menuEisenhowerMatrix();
                case 8 -> menuShowEisenhowerMatrix();
                case 9 -> menuAddPointsToHabit();
                case 10 -> menuWeeklyHabitCompletionRate();
                case 11 -> menuTop3Habits();
                case 12 -> menuViewTracker();
                case 13 -> load();
                case 14 -> save();
                case 15 -> menuResetData();

                default -> System.out.printf("Option %d is not recognizable %n", option);
            }


            System.out.println("Press Enter to see the menu again");
            scanner.nextLine();
            System.out.println(optMessage);
            choice = scanner.nextLine();
            try {
                option = Integer.parseInt(choice);
            } catch (NumberFormatException e) { // Catch exception if a user enters an empty string
                System.out.println("Enter a valid option");
                scanner.nextLine();
            }

        }
        System.out.printf("Thank you for using the habit and goal tracker!%nSee you tomorrow (hopefully)!");
        scanner.close();
    }


    /** @description: This method is used to save data from the tracker (goals, habits) into a .csv file
     * @author: Tania, Johnathan Hudson
     * NOTE: Setup code was obtained from Dr. Hudson's project video.
     */

    private static void save() {
      String filename;
      File file = null;

      do {
          do {
              System.out.println("Enter a filename: ");
              filename = scanner.nextLine().trim();
          } while(filename.isEmpty());
          file = new File(filename);
      } while (file.exists() && !file.canWrite());

        FileSaver.save(file, data);

    }

    /** @description: This method is used to load data from a .csv file
     * @author: Tania, Johnathan Hudson
     * NOTE: Setup code was obtained from Dr. Hudson's project video.
     */
    private static void load() {
        String filename;
        File file;

        do {
            do{
                System.out.println("Enter a filename: ");
                filename = scanner.nextLine().trim();
            }while(filename.isEmpty());
            file = new File(filename);
        } while(!file.exists() || !file.canRead());
        load(file);
    }

    /** @description: This method is used to load the .csv file data into our program.
     * @author: Tania, Johnathan Hudson
     * NOTE: Setup code was obtained from Dr. Hudson's project video.
     */

    private static void load(File file) {
        Data data = FileLoader.load(file);

        if (data == null) {
            System.err.printf("Failed to load data from file %s%n", file);
        } else {
            System.out.printf("Loaded data from file %s%n", file);
            Menu.data = data;
        }
    }


    /** @description This function allows users to create a Goal and assign a goalIdealCount to it, which is the number of days they would like to work on the goal.
     * @author Tania
     * @params None
     * @returns None
     */
    private static void menuCreateGoal() {

        String goalName;
        Integer goalIdealCount;

        boolean shouldPrint;
        do {
            System.out.println("What is your goal?");
            // Ask user to input goal
            goalName = getGoalName();

            // Ask user to input num of days they want to work on their goal
            goalIdealCount = getIdealCount();
            String category = null;

            // Send data to core.Data.java to populate hashmap
            data.createAGoal(goalName, goalIdealCount, category);

            // Prompt user to continue or exit menu
            System.out.println("Would you like to enter another goal? (Yes / No)");
            String response = scanner.next().trim().toLowerCase();
            shouldPrint = response.equals("yes") || response.equals("y") || response.equals("true");

//            scanner.nextLine(); // Consume any leftover newline character in the buffer

        } while (shouldPrint);

        System.out.println("Your goals are:");
        for (Goal goal : data.goals) {
            System.out.println("Goal: " + goal.getGoal() + " Ideal Count: " + goal.getIdealCount());
        }
        scanner.nextLine(); // Consume the \n left in the buffer

    }

    /** @description This function prompts the user for the name of their goal.
     * @requirements The goal cannot be an empty string.
     * @author Tania
     * @params None
     * @return goalName (String)
     */

    private static String getGoalName() {
        String goalName;
//        System.out.println("What is your goal?");
        do {
            goalName = scanner.nextLine();
        } while(goalName.isEmpty());

        return goalName; // Remove whitespaces and make lowercase. This prevents duplicating goals
    }

    /** @description This function prompts the user for the idealCount of their goal (number of days/week they want to work on their goal).
     * @requirements The idealCount cannot be <= 0, or greater than 7.
     * @author Tania
     * @return goalIdealCount (Integer)
     */

    private static Integer getIdealCount() {
        Integer goalIdealCount;
        System.out.println("Add the number of days (1-7) you want to work on this goal:");

        do {
            goalIdealCount = scanner.nextInt();
        } while(goalIdealCount <= 0 || goalIdealCount > 7);

        return goalIdealCount;
    }

    /** @description This function prompts the user for a goal to delete, and allows them to delete as many as they wish.
     * @author Tania
     * @params None
     * @return None
     */
    private static void menuDeleteGoal() {
        boolean shouldPrint;

        do {
            System.out.println("Your goals are: ");
            for (Goal goal : data.goals) {
                System.out.println("Goal: " + goal.getGoal());
            }

            System.out.print("Enter the name of the goal to remove:");
            String goalToDelete = scanner.nextLine();
            data.goalDelete(goalToDelete);

            System.out.println("Would you like to enter another goal? (Yes / No)");
            String response = scanner.next().trim().toLowerCase();
            shouldPrint = response.equals("yes") || response.equals("y") || response.equals("true");

        } while(shouldPrint);

        System.out.println("Updated goals: ");
        for (Goal goal : data.goals) {
            System.out.println("Goal: " + goal.getGoal() + " Ideal Count: " + goal.getIdealCount());
        }

    }

    /** @description This function allows a user to add habits for their goal. It is used to create habit objects.
     * @author Tania
     * @params None
     * @return None
     */

    private static void menuAddHabits() {

        String goalName;
        ArrayList<String> habitsList;
        boolean shouldPrint;

        // print all goals
        System.out.println("Your goals are:");
        for (Goal goal : data.goals) {
            System.out.println("Goal: " + goal.getGoal());
        }

        // Get the habits and send them to data.java
        do {
            System.out.println("Enter the goal to add habits for:");
            goalName = getGoalName();

            habitsList = getHabits();

            data.addHabits(goalName, habitsList, 0);

            System.out.println("Would you like to enter another goal? (Yes / No)");
            String response = scanner.next().trim().toLowerCase();
            shouldPrint = response.equals("yes") || response.equals("y") || response.equals("true");

        } while (shouldPrint);

        // Finally, show all goals and their habits
        System.out.println("Here is a list of your goals, and the habits");
        for (Map.Entry<Goal, HashSet<Habit>> entry : data.tracker.entrySet()) {
            System.out.println("Goal: " + entry.getKey().getGoal());
            for (Habit habit : entry.getValue()) {
                System.out.println("Habit: " + habit.getHabit() + " Ideal Count: " + habit.getIdealCount());
            }
        }


        scanner.nextLine(); // Consume newline character in buffer

    }

    /**
     * @description This function prompts user for habits to append to an ArrayList. This is used in AddHabits
     * @author Tania
     * @params None
     * @return ArrayList<String> for habits
     */

    private static ArrayList<String> getHabits() {
        ArrayList<String> habitsList = new ArrayList<>();
        String habit;
        boolean shouldPrint = false;
       // Continuously get habits

        do {
            System.out.println("Enter a habit");
            habit = scanner.nextLine().trim().toLowerCase();

            if (!habit.isEmpty()) {
                habitsList.add(habit);
            } else {
                System.out.println("Invalid input.");
//                continue;
            }
            System.out.println("Would you like to enter another habit? (Yes / No)");
            String response = scanner.next().trim().toLowerCase();
            shouldPrint = response.equals("yes") || response.equals("y") || response.equals("true");
            scanner.nextLine();

        } while(shouldPrint);

        return habitsList;
    }


    /** @description This function allows users to delete habits from a goal.
     * @author Tania
     * @params None
     * @return None
     */
    private static void menuDeleteHabits() {

        String goalName;
        String habitToDelete;
        boolean shouldPrint;

        // Get list of all goals and habits
        System.out.println("Here is a list of your goals, and the habits");
        for (Map.Entry<Goal, HashSet<Habit>> entry : data.tracker.entrySet()) {
            System.out.println("Goal: " + entry.getKey().getGoal());
            for (Habit habit : entry.getValue()) {
                System.out.println("Habit: " + habit.getHabit() + " Ideal Count: " + habit.getIdealCount());
            }
        }

        // Prompt user for the habits they want to remove
        do {

            System.out.println("Enter the goal you want to remove a habit from:");
            goalName = getGoalName();

            for (Goal goal : data.goals) {
                if (goal.getGoal().equals(goalName)) {
                    System.out.println("Enter the habit you want to delete: ");
                    do {
                        habitToDelete = scanner.nextLine();
                    } while (habitToDelete.isEmpty());

                    data.deleteHabitsFromGoal(goalName, habitToDelete);
                }
            }

            System.out.println("Would you like to re-try or enter another habit? (Yes / No)");
            String response = scanner.next().trim().toLowerCase();
            shouldPrint = response.equals("yes") || response.equals("y") || response.equals("true");
            scanner.nextLine();


        } while(shouldPrint);

        // Finally, show all goals and their habits
        System.out.println("Here is an updated list of your goals, and the habits");
        for (Map.Entry<Goal, HashSet<Habit>> entry : data.tracker.entrySet()) {
            System.out.println("Goal: " + entry.getKey().getGoal());
            for (Habit habit : entry.getValue()) {
                System.out.println("Habit: " + habit.getHabit() + " Ideal Count: " + habit.getIdealCount());
            }
        }
    }

    /**@description: This functions allow the user to display their already exisitng quadrants or prompts them to create one)
     * Essentially a display function(uses OOP)
     * @param:none
     * @return:none
     * @author: Sanbeer
     */

    private static void menuShowEisenhowerMatrix() {
        if (data.matrixExists()){
            System.out.println(data.matrix);
            scanner.nextLine();
        }else{
            System.out.println("You haven't a created a matrix buddy, please press Enter");
            scanner.nextLine();
        }
    }
    /**@description: This functions allow the user to display their already exisitng categorized goals or prompts them to create one)
     * Essentially a display function(uses OOP)
     * @param:none
     * @return:none
     * @author: Sanbeer
     */
    private static void menuShowCategorizedGoals(){
        if (data.categoryExists()){
            System.out.println(data.fields);
            scanner.nextLine();
        }else{
            System.out.println("You haven't categorized your goals yet, please press Enter ");
            scanner.nextLine();}
    }

    /** @description: This method allows users to see all goals, habits, and their properties
     * @author: Tania
     */
    private static void menuViewTracker() {

        Integer count = 0;

        for (Map.Entry<Goal, HashSet<Habit>> entry : data.tracker.entrySet()) {
            count++;
            System.out.println("Goal No. " + count);
            System.out.println("Name: " + entry.getKey().getGoal() +
                    " Ideal Count: " + entry.getKey().getIdealCount() +
                    " Category: " + entry.getKey().getCategory());

            if (entry.getValue() != null) {
                for (Habit habit : entry.getValue()) {
                    System.out.println("Habit: " + habit.getHabit() + " Current Count: " + habit.getCurrentCount());
                }
            }
        }

    }



    /**@description:Asks the user for what quadrant he wants to put each of his goals
     * (uses OOP to get all goals and loops through them for quadrant choices)
     * @param:none
     * @return:none
     * @author: Sanbeer
     */
    private static void menuEisenhowerMatrix(){
        ArrayList<String> goalsArrayList = data.getGoalsArrayList();
        ArrayList<Integer> choicesArrayList = new ArrayList<>();
        String[] categories = {"1) Urgent & Important", "2) Urgent & Not Important", "3) Important & Not Urgent",
                "4) Not Important & Not Urgent"};

        for (int i = 0; i < data.getGoalsArrayList().size(); i++) {
            System.out.println("The 4 Quadrants are \"1) Urgent & Important\", \"2) Urgent & Not Important\", \"3) Important & Not Urgent\",\n" +
                    " \"4) Not Important & Not Urgent");
            System.out.println("What quadrant in the matrix would you like to choose for goal: " + goalsArrayList.get(i));
            int chosenSection = scanner.nextInt();
            choicesArrayList.add(chosenSection);
        }

        System.out.println(data.storeEisenhowerMatrix(choicesArrayList));
        scanner.nextLine();

    }
    /**@description: Asks the user for choices of categories to put his gaols into and shows it to the user
     * (uses OOP to get all goals and loops through them)
     * @param:none
     * @return:none
     * @author: Sanbeer
     */
    private static void menuCategorizeGoals() {
        ArrayList<String> goalsArrayList2 = data.getGoalsArrayList();
        String[] categories2 = {"1) Finance", "2) Work", "3) School", "4) Emotional", "5) Spiritual", "6) Social"};
        for (int i = 0; i < data.getGoalsArrayList().size(); i++) {
            System.out.println("The 6 Categories are \"1) Finance\", \"2) Work\", \"3) School\", \"4) Emotional\", \n" +
                    "\"5) Spiritual\", \"6) Social");
            System.out.println("What category of goals would you like to choose for goal: " + goalsArrayList2.get(i));
            int chosenSection2 = scanner.nextInt();
            data.choicesArrayList2.add(chosenSection2);
//            data.setChoicesArrayList2(chosenSection2);

        }
        System.out.println(data.storeCategorizeGoals(data.choicesArrayList2));
        scanner.nextLine();

    }


    /**
     * Displays a menu option to add a completion point to a specified habit.
     * This method prompts the user to enter the name of the habit to which they want to add a point.
     * After receiving the input, it trims any leading or trailing whitespace and updates the habit's
     * completion count using {@code data.updateHabitCompletion}.
     *
     * @author: Phone
     */
    private static void menuAddPointsToHabit() {
        // Prompt the user to enter the name of the habit
        System.out.println("Enter the name of the habit to add a point to:");
        String habitName = scanner.nextLine().trim();// Remove leading/trailing spaces
        data.updateHabitCompletion(habitName);// Update the completion count for the habit
    }

    /**
     * Displays a menu option to view the weekly completion rate for each habit.
     * This method asks the user if they want to see their weekly completion percentage for each habit.
     * If the user responds positively, it calculates and displays the completion rates for all habits.
     * The user must type 'yes' to view the rates or 'no' to skip. It repeats the prompt on invalid input.
     *
     * @author: Phone
     */
    private static void menuWeeklyHabitCompletionRate() {
        boolean isValidInput = false;
        do {
            System.out.println("Do you want to see your weekly completion percentage for each habit (type yes or no)?");
            String response = scanner.nextLine().trim().toLowerCase();

            // Determine if the response indicates to print or not to print the completion rates
            boolean shouldPrint = response.equals("yes") || response.equals("true");
            boolean shouldNotPrint = response.equals("no") || response.equals("false");

            if (shouldPrint) {
                // Calculate and display completion rates for all habits
                Map<String, Double> completionRates = Data.calculateWeeklyCompletionRates();
                completionRates.forEach((habit, rate) ->
                        System.out.println("Habit: " + habit + ", Completion Rate: " + String.format("%.0f%%", rate)));
                isValidInput = true;
            } else if (shouldNotPrint) {
                // Do not display rates; valid response received
                isValidInput = true;
            } else {
                // Handle invalid response
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        } while (!isValidInput);// Repeat until valid input is received
    }

    /**
     * Displays the top 3 habits based on their weekly completion rates.
     * This method retrieves the top 3 habits with the highest completion rates and formats a message
     * to display them. If fewer than three habits are available, it adjusts the message accordingly.
     * The habits are presented in descending order of their completion rates.
     *
     * @author: Phone
     */
    private static void menuTop3Habits() {
        List<Habit> topHabits = Data.getTop3HabitsByCompletionRate();
        if (topHabits.isEmpty()) {
            System.out.println("No habit in the database.");
            return; // Exit if no habits are found
        }

        StringBuilder message = new StringBuilder();
        // Determine the message based on the number of habits retrieved
        if (topHabits.size() >= 3) {
            message.append("Top 3 habits for this week are: ");
        } else {
            message.append("Mostly done habits are: ");
        }

        // Construct a message listing the habits and their completion rates
        for (int i = 0; i < topHabits.size(); i++) {
            Habit habit = topHabits.get(i);
            if (i > 0) message.append(", ");
            message.append(String.format("%s (%.2f%% complete)", habit.getHabit(), habit.getWeeklyCompletionRate()));
        }

        if (topHabits.size() <= 3) {
            message.append(" in descending order.");
        } else {
            message.append(".");
        }

        System.out.println(message);// Display the formatted message
    }

    /**
     * Displays a menu option to reset all application and file data.
     * This method clears all data tracked by the application and resets the specified CSV file
     * to its default state. It is intended for use in resetting the application's state, e.g., for testing
     * or starting fresh. The path to the CSV file must be adjusted as necessary.
     *
     * @author: Phone
     */
    private static void menuResetData() {
        Data.resetAllData();
        //Data.resetCsvFile("path/to/your/test.csv"); // Replace with the actual text file
        System.out.println("Application and file data have been reset.");
    }

}


