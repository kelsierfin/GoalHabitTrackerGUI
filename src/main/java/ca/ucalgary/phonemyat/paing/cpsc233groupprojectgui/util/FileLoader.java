package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.Data;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class FileLoader {
    public static Data load(File file) {
        Data data = new Data();


        try (Scanner scanner = new Scanner(file)) {
            String line = scanner.nextLine();

            if (!line.equals("Goals")) {
                return null;
            }

            boolean found_habits = false;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                if (line.equals("Habits")) {
                    found_habits = true;
                    break;
                }

                String[] parts = line.split(",");

                if (parts.length != 3) {
                    return null;
                }

                String goalName = parts[0];
                int goalIdealCount = Integer.parseInt(parts[1]);
                String goalCategory = parts[2];

                data.createAGoal(goalName, goalIdealCount, goalCategory);
            }

            if(!found_habits) {
                return null;
            }

            while(scanner.hasNextLine()) {
                line = scanner.nextLine();

                String[] parts = line.split(",");

                if (parts.length == 5) {
                    ArrayList<String> habitsList = new ArrayList<>();
                    String goalName = parts[0];
                    int goalIdealCount = Integer.parseInt(parts[1]);
                    String goalCategory = parts[2];
                    int habitCurrentCount = Integer.parseInt(parts[3]);
                    String habitName = parts[4];

                    Habit habit = new Habit(goalName, goalIdealCount, goalCategory, habitCurrentCount, habitName);

                    habitsList.add(habit.getHabit());
                    data.addHabits(goalName, habitsList, habitCurrentCount);

                } else {
                    System.out.println("Not enough data!" + parts.length);
                    return null;
                }
            }
        } catch(IOException e) {
            return null;
        }

        return data;

    }
}
