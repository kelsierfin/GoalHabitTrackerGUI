package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects;

import java.util.HashMap;
import java.util.Objects;

public class Goal {

    // Object fields
    private String goal;
    private Integer idealCount;
    private String category;
    private Integer currentCount;

    // Constructor
    public Goal (String goal, Integer idealCount, String category){
        this.goal = goal;
        this.idealCount = idealCount;
        this.category = category;
    }

    // Getters
    public String getGoal(){
        return this.goal;
    }

    public Integer getIdealCount(){
        return this.idealCount;
    }

    public String getCategory(){
        return this.category;
    }

    // Setters
    public void setGoal(String goalName){
        this.goal = goalName;
    }

    public void setIdealCount(Integer idealCount) {
        this.idealCount = idealCount;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * Override Equals to compare goalNames when creating a goal.
     * @param obj Object to compare
     * @return boolean True if goal exists, False otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Goal other = (Goal) obj;
        return Objects.equals(this.goal, other.goal);
    }

    /**
     * Return the hashcode for a goal. To be used with hash structures.
     * @return hashcode integer
     * @author: Tania
     */

    @Override
    public int hashCode() {
        return goal.hashCode();
    }


    /**
     * Customize the toString method to print information unique to each habit
     * @return String describing child-vars
     * @author: Tania
     */
    @Override
    public String toString() {
        return "Goal: " + goal + " " +
                "IdealCount: " + idealCount + " " +
                "Category: " + category;
    }

}
