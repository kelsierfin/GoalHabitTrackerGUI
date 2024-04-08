package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.comparators;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import java.util.Comparator;

/**
 * A comparator for sorting {@link Habit} objects based on their weekly completion rate in descending order.
 * This means that habits with a higher completion rate will appear before those with a lower completion rate.
 */
public class WeeklyCompletionRateComparator implements Comparator<Habit> {

    /**
     * Compares two {@link Habit} objects by their weekly completion rate.
     *
     * @author: Phone
     * @param h1 the first habit to be compared.
     * @param h2 the second habit to be compared.
     * @return a negative integer, zero, or a positive integer as the weekly completion rate of {@code h1}
     *         is greater than, equal to, or less than the weekly completion rate of {@code h2}, respectively.
     */
    @Override
    public int compare(Habit h1, Habit h2) {
        return Double.compare(h2.getWeeklyCompletionRate(), h1.getWeeklyCompletionRate()); // Descending order
    }
}
