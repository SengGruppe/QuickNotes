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
        Period p = new Period(date.getTime(), System.currentTimeMillis());
        if (p.getYears() > 0) p = new Period(p.getYears(), PeriodType.years());
        else if (p.getMonths() > 0) p = new Period(p.getMonths(), PeriodType.months());
        else if (p.getWeeks() > 0) p = new Period(p.getWeeks(), PeriodType.weeks());
        else if (p.getDays() > 0) p = new Period(p.getDays(), PeriodType.days());
        else if (p.getHours() > 0) p = new Period(p.getHours(), PeriodType.hours());
        else if (p.getMinutes() > 0) p = new Period(p.getMinutes(), PeriodType.minutes());
        else if (p.getSeconds() >= 30) p = new Period(p.getSeconds(), PeriodType.seconds());
        else p = null;

        return ctx.getString(R.string.note_item_creationdate, p == null ? ctx.getString(R.string.time_below_30s) : PeriodFormat.wordBased(Locale.getDefault()).print(p));
    }
}
