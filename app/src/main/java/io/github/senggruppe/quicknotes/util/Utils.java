package io.github.senggruppe.quicknotes.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import io.github.senggruppe.quicknotes.R;

public class Utils {
    @SafeVarargs
    public static <T> T orDefault(T... v) {
        for (T e : v) if (e != null) return e;
        throw new NullPointerException("All values null!");
    }

    public static void showMessage(Activity a, String msg) {
        Snackbar.make(a.findViewById(R.id.snackbar_coordinator), msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showMessage(Activity a, @StringRes int resId) {
        Snackbar.make(a.findViewById(R.id.snackbar_coordinator), resId, Snackbar.LENGTH_LONG).show();
    }

    public static int dpToPx(Context ctx, int dp) {
        return (int) (dp * ctx.getResources().getDisplayMetrics().density);
    }
}
