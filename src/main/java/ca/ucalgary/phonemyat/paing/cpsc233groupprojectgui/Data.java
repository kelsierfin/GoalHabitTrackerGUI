package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.comparators.WeeklyCompletionRateComparator;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;

import java.io.File;
import java.io.PrintWriter;

import java.util.*;
import java.util.stream.Collectors;

/** The data class stores all methods providing functionality to the tracker.
 * @author Tania, Phone, Sanbeer
 * @tutorial T10, T14, T09
 * Emails: tania.rizwan@ucalgary.ca, sanbeer.shafin@ucalgary.ca, phonemyat.paing@ucalgary.ca
 */
public class Data {

    protected static  ArrayList<Integer> choicesArrayList2; // choices for the goals for categorization in order of input.

    protected static HashSet<Goal> goals; // Hashset to contain all goal objects
    protected static HashMap<String, ArrayList<String>> matrix; // intializes the eisenhower matrix so that it can be changed anywhere in package

    protected static HashMap<String, ArrayList<String>> fields; // intializes the categorization matrix so that it can be changed anywhere in packag
    protected static HashMap<String, ArrayList<String>> matrix2; // used for the GUI
    protected static ArrayList<String> list10 = new ArrayList<>(); // used for GUI Matrix
    protected static ArrayList<String> list20 = new ArrayList<>(); // used for GUI Matrix
    protected static ArrayList<String> list30 = new ArrayList<>(); // used for GUI matrix
    protected static ArrayList<String> list40 = new ArrayList<>(); // Used for GUI Matrix


    protected static HashMap<Goal, HashSet<Habit>> tracker; // Maps each goal object to hashset<Habit> habits associated with it
    protected static HashMap<String, Integer> habitAndICounts; // Stores each habit and its ideal count
    protected static  HashMap<String, Integer> habitAndECounts; // Stores each habit and its points earned
    private static Scanner scanner = new Scanner(System.in);
    public Data() {
        this.choicesArrayList2 = new ArrayList<>();
        this.goals = new HashSet<>();
        this.matrix = new HashMap<>();
        this.matrix2 = new HashMap<>();
        this.list10 = new ArrayList<>();
        this.list20 = new ArrayList<>();
        this.list30 = new ArrayList<>();
        this.list40 = new ArrayList<>();
        this.fields = new HashMap<>();
        this.tracker = new HashMap<>();
        this.habitAndICounts = new HashMap<>();
        this.habitAndECounts = new HashMap<>();
    }

    // Getters and Setters

    /** @description: Getter for tracker
     *
     * @return Hashmap<Goal, HashSet<Habit>> tracker that stores all goals and habits
     */
    public static HashMap<Goal, HashSet<Habit>> getTracker() {
        return tracker;
    }

    /** @description: Getter for goals HashSet
     *
     * @return Hashset<Goal> goals
     */
    public static HashSet<Goal> getGoals(){
        return goals;
    }



    /**
     * @description: This method set-ups the goal objects and adds to the goals hashset
     * @param goalName The String name of the goal
     * @param goalIdealCount The Integer desired count for goal progress/week
     * @param category The category for a goal. Initially set to null.
     * @return boolean
     * @author Tania
     */
    public static boolean createAGoal(String goalName, Integer goalIdealCount, String category) {

        Goal goal = new Goal(goalName, goalIdealCount, category);

        if(goals.contains(goal)){
            System.out.println("Your goal (" + goalName + ") already exists.");
            return false;
        } else{
            goals.add(goal);
            System.out.printf("Goal added successfully!\nYour goal is: " + goal.getGoal() + " and your ideal count is: " + goal.getIdealCount() + "\n");
            return true;
        }
    }

    /**
     * @description: This function removes a goal from the Goals hashset
     * @param goalToDelete The name for goal to remove
     * @return true if a goal has been deleted
     */

    public static boolean goalDelete(String goalToDelete) {

        boolean goalFound = false;

//        for (Goal goal : goals) {
//            if (goal.getGoal().equals(goalToDelete)){
//                System.out.println("Successfully deleted: " + goal.getGoal());
//                goals.remove(goal);
//                goalFound = true;
//                return true;
//            }
//        }

        for (Map.Entry<Goal, HashSet<Habit>> e: tracker.entrySet()) {
            if (e.getKey().getGoal().equals(goalToDelete)) {
                tracker.remove(e.getKey());
                System.out.println("Successfully deleted: " + e.getKey().getGoal());
                goalFound = true;
                return true;
            }
        }

        if (!goalFound) {
            System.out.println("Please enter a valid goal for deletion.");
            return false;
        }

        return false;
    }



    /**
     * @description This function allows a user to add habits to their goal. It places them in the tracker
     * @param goalName The name of the goal object to add habits to
     * @param habitsList The list of habits to add for the goal
     * @param currentCount The number of points for completing a habit. Currently set to null
     * @return None
     * @author: Tania
     */

    public static void addHabits(String goalName, ArrayList<String> habitsList, Integer currentCount) {

        // Go through each goal until we find goalName
        // convert each string to a habit object
        // place the object in a hashset (prevent duplicates)
        // Add the habit object to tracker, next to the goal
        HashSet<Habit> allHabits = null;
        for (Goal goal : goals) {
            if (goal.getGoal().equals(goalName)) {
                allHabits = tracker.getOrDefault(goal, new HashSet<>());
                for (String habitName : habitsList) {
                    Habit individualHabit = new Habit(goal.getGoal(), goal.getIdealCount(), goal.getCategory(), currentCount, habitName);
                    allHabits.add(individualHabit);
                }
                // Add hashset to tracker

                tracker.put(goal, allHabits);
            }
        }
    }


    /**
     * This function takes the name of all goal objects, and places them in an ArrayList.
     * This arraylist is used for the Eisenhower matrix.
     * @return ArrayList containing String goal names
     * @author Tania
     */

    public static ArrayList<String> getGoalsArrayList() {
        ArrayList<String> goalsArrayList = new ArrayList<>();

        for (Goal goal : goals) {
            goalsArrayList.add(goal.getGoal());
        }
        return goalsArrayList;
    }



    /**
     * @description: This function deletes a specific habit from a goal
     * @param goalName The name of the goal to delete a habit from
     * @param habitToDelete The name of the habit to remove
     */

    public static boolean deleteHabitsFromGoal(String goalName, String habitToDelete) {


        Goal goalOfInterest = null;

        // Locate the goal
        for (Goal goal : goals) {
            if (goal.getGoal().equals(goalName)) {
                goalOfInterest = goal;
                break;
            }
        }

        HashSet<Habit> habitsSet = tracker.get(goalOfInterest); // Get hashset for our goal

        // Use an iterator to modify hashset within loop
        if (habitsSet!=null) {
            Iterator<Habit> iterator = habitsSet.iterator();

            while (iterator.hasNext()) {
                Habit habit = iterator.next();

                if (habit.getHabit().equals(habitToDelete)) {
                    iterator.remove();
                    System.out.println("Habit removed successfully");
                    tracker.put(goalOfInterest, habitsSet);
                    return true;
                }
            }
        } else {
            System.out.println("Enter a valid Goal/Habit");
            return false;
        }

    return false;
    }



    /**
     * Searches for and returns a habit based on its name.
     * This method iterates through all habit collections stored in {@code tracker}, searching for a habit
     * whose name matches the specified {@code habitName}, disregarding case differences. The first habit found
     * with a matching name is returned.
     *
     * The search is exhaustive across all collections of habits in the tracker. If no habit with the given name
     * is found, the method returns {@code null}.
     *
     * @author: Phone
     * @param habitName The name of the habit to search for. The search is case-insensitive.
     * @return The {@link Habit} object with the specified name if found; {@code null} otherwise.
     */
    public Habit findHabitByName(String habitName) {
        for (HashSet<Habit> habits : tracker.values()) {
            for (Habit habit : habits) {
                if (habit.getHabit().equalsIgnoreCase(habitName)) {
                    return habit;
                }
            }
        }
        return null;
    }


    /**
     * Increments the completion count of a specified habit by one.
     * This method searches for a habit by its name using {@code findHabitByName} method. If the habit is found,
     * its current count is incremented by one to reflect an additional completion. A message is printed to the console
     * to confirm the update, showing the habit's name and its new completion count.
     *
     * If the specified habit name does not correspond to an existing habit, a message is printed to the console
     * indicating that the habit name is invalid.
     *
     * @author: Phone
     * @param habitName The name of the habit to be updated. It is assumed to be a unique identifier for the habit.
     */
    public void updateHabitCompletion(String habitName) {
        Habit habit = findHabitByName(habitName);
        if (habit != null) {
            habit.incrementCurrentCount();
            System.out.println("Habit '" + habitName + "' updated. New count: " + habit.getCurrentCount());
        } else {
            System.out.println("Invalid habit. Please try again.");
        }
    }


    /**
     * Calculates and returns the weekly completion rates for all habits tracked.
     * The completion rate for each habit is calculated as the ratio of the current count to the ideal count,
     * multiplied by 100 to convert it into a percentage. This method iterates over all habits stored in a
     * tracker (assumed to be a static field containing habits grouped by some criteria) and computes their
     * completion rates.
     *
     * If a habit's ideal count is zero, indicating that no completions were expected for the week,
     * the method assigns a completion rate of 0.0 to avoid division by zero errors.
     *
     * @author: Phone
     * @return A map where each key is a habit's unique identifier (assumed to be a string) and each value is
     *         the calculated weekly completion rate for that habit. The completion rate is a double value
     *         ranging from 0.0 to 100.0.
     */
    public static Map<String, Double> calculateWeeklyCompletionRates() {
        // Initialize a map to store the completion rates for each habit.
        Map<String, Double> rates = new HashMap<>();

        // Iterate over all sets of habits grouped by some criteria (not shown here).
        for (HashSet<Habit> habits : tracker.values()) {
            // Process each habit in the current set.
            for (Habit habit : habits) {
                // Calculate the completion rate. If the ideal count is 0, set rate to 0.0 to avoid division by zero.
                // Otherwise, calculate the rate as (currentCount/idealCount) * 100.
                double rate = (habit.getIdealCount() == 0) ? 0.0 :
                        ((double) habit.getCurrentCount() / habit.getIdealCount()) * 100;

                // Store the calculated rate in the map with the habit's name as the key.
                rates.put(habit.getHabit(), rate);

                System.out.printf("Habit: %s, Ideal Count: %d, Current Count: %d, Completion Rate: %.2f%%\n",
                        habit.getHabit(), habit.getIdealCount(), habit.getCurrentCount(), rate);
            }
        }

        // Return the map containing all habits and their respective completion rates.
        return rates;
    }


    /**
     * Retrieves the top 3 habits with the highest weekly completion rates.
     * This method first collects all existing habits and sorts them in descending order by their completion rates,
     * using the {@link WeeklyCompletionRateComparator}. It then selects the top 3 habits based on this sorted order.
     *
     * This approach assumes that all habits are available through the {@code getAllHabits()} method and that
     * the {@link WeeklyCompletionRateComparator} is implemented to sort habits by their completion rates in descending order.
     * If fewer than three habits exist, it returns all available habits, sorted by their completion rates.
     *
     * @author: Phone
     * @return A list of the top 3 {@link Habit} objects with the highest weekly completion rates. The list is sorted
     *         in descending order by completion rate. If there are fewer than three habits, returns all habits, maintaining
     *         the descending order by completion rate.
     */
    public static List<Habit> getTop3HabitsByCompletionRate() {
        // First, get all habits and put them into a new list.
        List<Habit> sortedHabits = new ArrayList<>(getAllHabits());

        // Sort the list of habits using the WeeklyCompletionRateComparator.
        // This comparator is designed to sort habits in descending order by their completion rates.
        sortedHabits.sort(new WeeklyCompletionRateComparator());

        // Use a stream to select the top 3 habits from the sorted list.
        // The stream is limited to the first 3 elements, effectively selecting the habits
        // with the highest completion rates thanks to the prior sorting.
        return sortedHabits.stream().limit(3).collect(Collectors.toList());
    }


    /**
     * Retrieves all habits from a tracker.
     * This method compiles a list of all habits stored across various collections in the tracker.
     * Each collection in the tracker is assumed to group habits based on certain criteria (not specified here).
     *
     * @author: Phone
     * @return A {@link List} of {@link Habit} objects representing all the habits currently tracked.
     */
    public static List<Habit> getAllHabits() {
        List<Habit> allHabits = new ArrayList<>();
        // Iterate over each set of habits in the tracker and add all to the list
        for (HashSet<Habit> habitSet : tracker.values()) {
            allHabits.addAll(habitSet);
        }
        return allHabits;
    }


    /**
     * Resets the contents of a specified CSV file.
     * This method attempts to clear the contents of the CSV file located at the given filePath.
     * It provides two options for resetting: clearing the file's contents or deleting the file.
     * The default behavior is to clear the file's contents. Uncomment the deletion line if deletion
     * is preferred. Any errors encountered during the reset process are caught and logged to the console.
     *
     * @author: Phone
     * @param filePath The path to the CSV file to be reset.
     */
    public static void resetCsvFile(String filePath) {
        try {
            File file = new File(filePath);

            // Delete the existing file
            boolean isDeleted = file.delete();

            // Check if deletion was successful or if the file didn't exist
            if (isDeleted || !file.exists()) {
                // Attempt to create a new, empty file
                boolean isCreated = file.createNewFile();
                if (isCreated) {
                    System.out.println("CSV file has been reset.");
                } else {
                    System.out.println("Failed to create the CSV file.");
                }
            } else {
                System.out.println("Failed to delete the existing CSV file.");
            }
        } catch (Exception e) {
            System.out.println("Error resetting CSV file: " + e.getMessage());
        }
    }


    /**
     * Resets all tracked data and the associated CSV file to their default states.
     * This method clears all data collections and resets the CSV file specified by its path.
     * It is useful for reinitializing the application's data state to a clean slate, whether for testing
     * purposes or to restart data tracking from the beginning.
     *
     * @author: Phone
     */
    public static void resetAllData() {
        // Clear all collections
        goals.clear();
        matrix.clear();
        fields.clear();
        tracker.clear();

        // Reset CSV File
        //resetCsvFile("C:\\Users\\phone\\Desktop\\gg\\userData.vcs"); //

        System.out.println("All data have been reset to their default state.");
    }





    /**
     * @param choicesArrayList(the quadrant choices for the goals in order)
     * @description This function creates/organizes the choices and goals into a proper hashmap for eisenhower matrix
     * @return It returns the updated version of the matrix to menu to print out.
     * @author: Sanbeer
     */


    public static HashMap<String,ArrayList<String>>storeEisenhowerMatrix(ArrayList<Integer> choicesArrayList) {
        ArrayList<String> goalsArrayList = Data.getGoalsArrayList();

        String[] categories = {"Urgent & Important", "Urgent & Not Important", "Important & Not Urgent",
                "Not Important & Not Urgent"};

        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();


        for (int i = 0; goalsArrayList.size() > i; i++){
            if (choicesArrayList.get(i) == 1){
                list1.add(goalsArrayList.get(i));
            }
            if (choicesArrayList.get(i) == 2){
                list2.add(goalsArrayList.get(i));
            }
            if (choicesArrayList.get(i) == 3){
                list3.add(goalsArrayList.get(i));
            }
            if (choicesArrayList.get(i) == 4){
                list4.add(goalsArrayList.get(i));
            }
        }

        matrix.put("Urgent & Important", list1);
        matrix.put("Urgent & Not Important", list2);
        matrix.put("Important & Not Urgent", list3);
        matrix.put("Not Important & Not Urgent", list4);

        return matrix;
    }
    /**
     * @param: none
     * @description This functions simply checks if the matrix had been intialized before since whenever it is intialized in menu, all quadrants get added
     * @return: It returns the boolean which is uses in the display function for the matrix.
     */

    public static boolean matrixExists() {
        boolean matrixExist = false;
        if (matrix.containsKey("Urgent & Important")){
            matrixExist = true;
        }
        return matrixExist;
    }
    /**
     * @param choicesArrayList2(the category choices for the goals in order)
     * @description This function creates/organizes the choices and goals into a proper hashmap for categorized goals
     * @return It returns the updated version of the fields to menu to print out.
     * @author: Sanbeer
     * It also changes the category property of the object Goal itself using the choices
     */

    public static HashMap<String,ArrayList<String>> storeCategorizeGoals(ArrayList<Integer> choicesArrayList2) {
        ArrayList<String> goalsArrayList2 = Data.getGoalsArrayList();
        String[] categories2 = {"1) Finance", "2) Work", "3) School", "4) Emotional", "5) Spiritual", "6) Social"};

        // Create another for loop to iterate thru each goal object in the hashset Goals
        //  Assign category to goal / get category for a goal
        int count = 0;
        for(Goal goal : goals){
            if (choicesArrayList2.get(count) == 1){
                goal.setCategory("Finance");
                count++;
            }
            if (choicesArrayList2.get(count) == 2){
                goal.setCategory("Work");
                count++;
            }
            if (choicesArrayList2.get(count) == 1){
                goal.setCategory("School");
                count++;
            }
            if (choicesArrayList2.get(count) == 1){
                goal.setCategory("Emotional");
                count++;
            }
            if (choicesArrayList2.get(count) == 1){
                goal.setCategory("Spiritual");
                count++;
            }
            if (choicesArrayList2.get(count) == 1){
                goal.setCategory("Social");
                count++;
            }
        }

        ArrayList<String> list11 = new ArrayList<>();
        ArrayList<String> list22 = new ArrayList<>();
        ArrayList<String> list33 = new ArrayList<>();
        ArrayList<String> list44 = new ArrayList<>();
        ArrayList<String> list55 = new ArrayList<>();
        ArrayList<String> list66 = new ArrayList<>();

        for (int i = 0; goalsArrayList2.size() > i; i++){
            if (choicesArrayList2.get(i) == 1){
                list11.add(goalsArrayList2.get(i));
            }
            if (choicesArrayList2.get(i) == 2){
                list22.add(goalsArrayList2.get(i));
            }
            if (choicesArrayList2.get(i) == 3){
                list33.add(goalsArrayList2.get(i));
            }
            if (choicesArrayList2.get(i) == 4){
                list44.add(goalsArrayList2.get(i));
            }
            if (choicesArrayList2.get(i) == 5){
                list55.add(goalsArrayList2.get(i));
            }
            if (choicesArrayList2.get(i) == 6){
                list66.add(goalsArrayList2.get(i));
            }

        }
        fields.put("Finance" , list11);
        fields.put("Work" , list22);
        fields.put("School" , list33);
        fields.put("Emotional" , list44);
        fields.put("Spiritual" , list55);
        fields.put("Social" , list66);

        return fields;
    }
    /**
     * @param: none
     * @description This functions simply checks if the fields had been intialized before since whenever it is intialized in menu, all categories get added.
     * @return: It returns the boolean which is uses in the display function for the field.
     */

    public static boolean categoryExists() {
        boolean fieldExist = false;
        if (fields.containsKey("Finance")) {
            fieldExist = true;
        }
        return fieldExist;
    }

    /**
     * @param: categoryChoice - this is the category choice for the goal, goalChoice - this is the goal we are pointing
     * @description it just sets the category for a specific goal.
     * @return:none
     */
    public static void setCategory(String categoryChoice, String goalChoice) {

        // Iterate through tracker to get habits for your goal
        for (Map.Entry<Goal, HashSet<Habit>> e : Data.getTracker().entrySet()) { // e = [goal, [habit1, habit2]]
            if (e.getKey().getGoal().equals(goalChoice)) {
                e.getKey().setCategory(categoryChoice);
                HashSet<Habit> habitsForGoal = e.getValue(); // Get hashset for all habits for goalChoice
                for (Habit habit : habitsForGoal) {
                    habit.setCategory(categoryChoice);
                }
            }

//            HashSet<Habit> habitsForGoal = e.getValue(); // Get hashset for all habits for goalChoice
//            for (Habit habit : habitsForGoal) {
//                habit.setCategory(categoryChoice);
//            }

        }
//        for (Goal goal : goals) {
//            if (goal.getGoal().equals(goalChoice)) {
//                goal.setCategory(categoryChoice);
//            }
//        }
    }
    /**
     * @param: matrixChoice - this is the quadrant to put the goal into , goalChoice2 - the goal we are going to be putting into quadrant
     * @description used to set a specific matrix quadrant for a goal
     * @return: it doesn't return anything  but rather just add to a preexisting hashmap
     */

    public static void setMatrix(String matrixChoice, String goalChoice2) {
        if(matrixChoice.equals("Urgent and Important")){
            list10.add(goalChoice2);
        }
        if(matrixChoice.equals("Urgent and Not Important")){
            list20.add(goalChoice2);
        }
        if(matrixChoice.equals("Important and Not Urgent")){
            list30.add(goalChoice2);
        }
        if(matrixChoice.equals("Not Important and Not Urgent")){
            list40.add(goalChoice2);
        }

        matrix2.put("Urgent and Important",list10);
        matrix2.put("Urgent and Not Important",list20);
        matrix2.put("Important and Not Urgent",list30);
        matrix2.put("Not Important and Not Urgent",list40);

    }

    public void addHabits(String key, ArrayList<Habit> value) {
    }
}




