package io.github.senggruppe.quicknotes.util;

import android.content.Context;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

import java.util.Date;
import java.util.Locale;

import io.github.senggruppe.quicknotes.R;

public class Format {
    public static String formatCreationDate(Context ctx, Date date) {
        long dur = System.currentTimeMillis() - date.getTime();
        Period p = new Period(dur);
        if (p.getYears() > 0) p = new Period(dur, PeriodType.years());
        else if (p.getMonths() > 0) p = new Period(dur, PeriodType.months());
        else if (p.getWeeks() > 0) p = new Period(dur, PeriodType.weeks());
        else if (p.getDays() > 0) p = new Period(dur, PeriodType.days());
        else if (p.getHours() > 0) p = new Period(dur, PeriodType.hours());
        else if (p.getMinutes() > 0) p = new Period(dur, PeriodType.minutes());
        else if (p.getSeconds() >= 30) p = new Period(dur, PeriodType.seconds());
        else p = null;

        return ctx.getString(R.string.note_item_creationdate, p == null ? ctx.getString(R.string.time_below_30s) : PeriodFormat.wordBased(Locale.getDefault()).print(p));
    }
}
