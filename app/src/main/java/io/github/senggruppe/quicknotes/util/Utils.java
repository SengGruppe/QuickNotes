package io.github.senggruppe.quicknotes.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

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

    private static Runnable onSuccess;
    private static Consumer<List<String>> onFailed;

    public static <A extends Activity & PermissionResultHandler> void requestPermission(A ctx, String perm, Runnable onSuccess, Runnable onFailed) {
        requestPermissions(ctx, new String[]{perm}, onSuccess, l->onFailed.run());
    }

    public static <A extends Activity & PermissionResultHandler> void requestPermissions(A ctx, String[] perms, Runnable onSuccess, Consumer<List<String>> onFailed) {
        if (onSuccess == null) onSuccess = () -> {};
        if (onFailed == null) onFailed = l -> {};
        for (String perm: perms) {
            if (ActivityCompat.checkSelfPermission(ctx, perm) != PackageManager.PERMISSION_GRANTED) {
                if (Utils.onSuccess != null || Utils.onFailed != null) Crashlytics.logException(new IllegalStateException("request runnables still filled!"));
                Utils.onSuccess = onSuccess;
                Utils.onFailed = onFailed;
                ActivityCompat.requestPermissions(ctx, perms, Constants.PERMISSION_REQUEST);
                return;
            }
            onSuccess.run();
        }
    }

    public interface PermissionResultHandler extends ActivityCompat.OnRequestPermissionsResultCallback {
        default void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == Constants.PERMISSION_REQUEST) {
                List<String> res = new ArrayList<>();
                if (permissions.length == 0) onFailed.accept(res);
                for (int i = 0; i < permissions.length; i++) if (grantResults[i] == PackageManager.PERMISSION_DENIED) res.add(permissions[i]);
                if (res.isEmpty()) onSuccess.run(); else onFailed.accept(res);
            }
        }
    }

    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
}
