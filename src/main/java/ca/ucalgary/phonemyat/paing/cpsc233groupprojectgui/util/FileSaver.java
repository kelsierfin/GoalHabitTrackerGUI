package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util;


import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.Data;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

public class FileSaver {
    public static boolean save(File file, Data data) {
        try(FileWriter fw = new FileWriter(file)){
            // Write CSV header

            fw.write("Goals" + "\n");

            for (Map.Entry<Goal, HashSet<Habit>> entry : data.getTracker().entrySet()) {
                fw.write(String.format("%s,%d,%s%n", entry.getKey().getGoal(), entry.getKey().getIdealCount(), entry.getKey().getCategory()));
            }
            fw.flush();

            fw.write("Habits" + "\n");
            for (Map.Entry<Goal, HashSet<Habit>> entry : data.getTracker().entrySet()) {
                for (Habit habit : entry.getValue()) {
                    fw.write(String.format("%s,%d,%s,%d,%s%n", habit.getGoal(), habit.getIdealCount(), habit.getCategory(), habit.getCurrentCount(), habit.getHabit()));
                }
            }
            fw.flush();

        } catch (IOException e) {
            return false;
        }
        return true;


    }
}
