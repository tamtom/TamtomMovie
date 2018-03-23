package udacity.com.tamtommovie.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Omar AlTamimi on 3/20/2018.
 */

public class DateUtil {
    public static String getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }
}
