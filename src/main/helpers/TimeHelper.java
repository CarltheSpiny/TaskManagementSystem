package main.helpers;

import main.tasks.Task;

public class TimeHelper {

	/**
	 * Converts a time in 12-hour format represented a s afloat to a float
	 * in 24-hour time. This method will already parse the string. <b>Cannot have
	 * a 0 in the front.<b>
	 * @param timeIn12Hour a string in the format: HH:MM AM/PM
	 * @return
	 */
    public static float convertTo24Hour(String timeIn12Hour) {
        String[] parts = timeIn12Hour.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].substring(0, 2));
        String amPm = parts[1].substring(2);

        // Adjust hours based on AM/PM, +12 if pm
        if (amPm.equalsIgnoreCase("PM") && hours < 12) {
            hours += 12;
        } else if (amPm.equalsIgnoreCase("AM") && hours == 12) {
            hours = 0;
        }

        // Calculate the time in 24-hour format
        float time24Hour = hours + (float) minutes / 60;

        return time24Hour;
    }
    
    /**
     * Rounds the float time to the nearest 15 minutes. Can be used for any time duration
     * @param unroundedTime
     * @return
     */
    public static float roundToNearest15Minutes(float unroundedTime) {
        int totalMinutes = (int) (unroundedTime * 60);
        int modRemainder = totalMinutes % 15;
        int roundedMinutes = totalMinutes + (modRemainder < 8 ? -modRemainder : 15 - modRemainder);
        // Convert back to hours
        float roundedTime = roundedMinutes / 60.0f;

        // validate time so it is within 24 hours
        roundedTime = roundedTime % 24;

        return roundedTime;
    }
    
    /**
     * Checks if the number passed is rounded to the nearest 15 minutes.
     * This does not repair this number, however.
     * @param time24Hour
     * @return
     */
    public static boolean isRounded(float time24Hour) {
        // Convert the time to minutes
        int totalMinutes = (int) (time24Hour * 60);

        // Check if the minutes are divisible by 15
        return totalMinutes % 15 == 0;
    }
    
    /**
	 * Get month name from the integer representing the month
	 * @param month an integer from 1 to 12
	 * @return
	 */
	public static String getMonthName(int month) {
		switch(month) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "Apirl";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December ";
		default:
			return "Error";
		}
	}
	
	public static boolean isDateValid(int date, int month, int day) {
		if (date >= 10000000) {
			// validate date: has the form YYYYMMDD as an integer
			if (month > 0 && month < 13) {
				// date has to be 1-12
				if (day > 0 && day < 31) {
					// day has to be 1-31 (not checking for months with less days)
				} else {
					System.err.println("The day is invalid");
					return false;
				}
			} else {
				System.err.println("The month is invalid");
				return false;
			} 
		} else {
			System.err.println("The date is invalid");
			return false;
		}
		return true;
	}
    
    public static void main(String[] args) {
        /**
String time12Hour = "3:50PM"; // Example time in 12-hour format
		float time24Hour = convert12HourTo24Hour(time12Hour);
		System.out.println("Time in 24-hour format: " + time24Hour);
		float startTime = time24Hour; // Example start time in 24-hour format
		float roundedTime = roundToNearest15Minutes(startTime);
		System.out.println("Rounded time: " + roundedTime);
		System.out.println("Test Time 1 rounded to nearest 15 minutes: " + isRoundedToNearest15Minutes(startTime));
		System.out.println("Test Time 2 rounded to nearest 15 minutes: " + isRoundedToNearest15Minutes(roundedTime));
**/
    }

}
