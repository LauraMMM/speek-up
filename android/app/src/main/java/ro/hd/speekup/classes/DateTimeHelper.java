package ro.hd.speekup.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import ro.hd.speekup.R;

import android.content.Context;
import android.content.res.Resources;


public final class DateTimeHelper
{
    public static String writeRelativeTime(long unixTime, Context context, String customDateFormat) {
        if(customDateFormat == null) {
            customDateFormat = "dd/MM/yyyy";
        }

        Date date = new Date(unixTime * 1000L);
        String formattedTime;

        if (isToday(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US); // the format of your date
            formattedTime = sdf.format(date);
        } else {
            if (isYesterday(date)) {
                formattedTime = context.getString(R.string.yesterday);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(customDateFormat, Locale.US); // the format of your date
                formattedTime = sdf.format(date);
            }
        }
        return formattedTime;
    }

    public static String writeDate(long unixTime, Context context, String customDateFormat) {
        if(customDateFormat == null) {
            customDateFormat = "dd/MM/yyyy";
        }
        Date date = new Date(unixTime * 1000L);
        String formattedDate;

        if (isToday(date)) {
            formattedDate = context.getString(R.string.today);
        } else {
            if (isYesterday(date)) {
                formattedDate = context.getString(R.string.yesterday);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(customDateFormat, Locale.US);
                formattedDate = sdf.format(date);
            }
        }
        return formattedDate;
    }

    public static String writeTimeToday(long unixTime, String customTimeFormat) {
        if(customTimeFormat == null) {
            customTimeFormat = "HH:mm";
        }

        Date date = new Date(unixTime * 1000L);
        String formattedTime;

        SimpleDateFormat sdf = new SimpleDateFormat(customTimeFormat, Locale.US);
        formattedTime = sdf.format(date);

        return formattedTime;
    }

    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance(); // today

        Calendar testedTime = Calendar.getInstance();
        testedTime.setTime(date);

        return (today.get(Calendar.YEAR) == testedTime.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == testedTime.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isYesterday(Date date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar testedTime = Calendar.getInstance();
        testedTime.setTime(date);

        return (yesterday.get(Calendar.YEAR) == testedTime.get(Calendar.YEAR)
                && yesterday.get(Calendar.DAY_OF_YEAR) == testedTime.get(Calendar.DAY_OF_YEAR));

    }
}
