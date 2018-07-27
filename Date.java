package expedition.game_tools;

import java.util.Random;

public class Date {

    private int day;
    private Month month;

    public Date(Month month, int day) {
        this.day = day;
        this.month = month;
    }

    public Date() {
        Random rand = new Random();

        this.day = rand.nextInt(15) + 1;

        int randInt = rand.nextInt(4);

        if (randInt == 1) {
            month = Month.Abb;
        } else if (randInt == 2) {
            month = Month.Bea;
        } else if (randInt == 3) {
            month = Month.Cee;
        } else {
            month = Month.Dei;
        }

    }

    public void increment() {

        if (day < 15) {
            day++;
        } else if (month == Month.Abb) {
            day = 1;
            month = Month.Bea;
        } else if (month == Month.Bea) {
            day = 1;
            month = Month.Cee;
        } else if (month == Month.Cee) {
            day = 1;
            month = Month.Dei;
        } else {
            day = 1;
            month = Month.Abb;
        }
    }

    public void increment(int days) {
        for (int i = 0; i < days; i++) {
            increment();
        }
    }

    public int daysSince(Date compare) {
        return compare.daysUntil(this);
    }

    public int daysUntil(Date compare) {
        if (this.compare(compare) == 0) { //current date is the same day as comparison
            return 0;
        } else if (this.compare(compare) > 0) { //current date is later in the year than our compare target.
            //add the remaining days in the month, then add the number of remaining months in the year, then add the number of months until the next month, then add the days in that month.
            return this.daysUntilNextMonth() + (15 * (4 - this.month.getNum())) + (15 * (compare.month.getNum() - 1)) + compare.day;

        } else if (compare.month == this.month) { //current date is earlier in the year than our compare target.
            return compare.day - this.day; // CASE 1 same month, just subtract days.
        } else { //CASE 2 different month, add days until end of month, then number of months, then days until the day in the target month.
            return this.daysUntilNextMonth() + (15 * (compare.month.getNum() - this.month.getNum() - 1)) + compare.day;
        }

    }

    public int compare(Date compare) {
        if ((this.month.getNum() > compare.month.getNum()) || ((this.month == compare.month) && (this.day > compare.getDay()))) {
            return 1;
        } else if (this.month == compare.month && this.day == compare.day) {
            return 0;
        } else {
            return -1;
        }
    }

    public int daysUntilNextMonth() {
        return 15 - day;
    }

    public int getDay() {
        return day;
    }

    public Month getMonth() {
        return month;
    }
    
    public boolean equals(Date in){
        return (in.day == this.day && in.month.equals(this.month));
    }

    public String toString() {
        String suffix;
        if (day == 1) {
            suffix = "st";
        } else if (day == 2) {
            suffix = "nd";
        } else if (day == 3) {
            suffix = "rd";
        } else {
            suffix = "th";
        }
        return "" + month + " " + day + suffix;
    }

}
