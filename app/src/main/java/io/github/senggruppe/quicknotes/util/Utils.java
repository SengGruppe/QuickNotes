package io.github.senggruppe.quicknotes.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.util.function.BiConsumer;
import io.github.senggruppe.quicknotes.util.function.Consumer;

public class Utils {
    private static final AtomicInteger nextId = new AtomicInteger();
    private static final SparseArray<BiConsumer<Integer, Intent>> intentResultHandlers = new SparseArray<>();
    private static final SparseArray<Runnable> permissionSuccessHandlers = new SparseArray<>();
    private static final SparseArray<Consumer<List<String>>> permissionFailedHandlers = new SparseArray<>();

    private Utils() {

    }

    @SafeVarargs
    public static <T> T orDefault(T... v) {
        for (T e : v) {
            if (e != null) {
                return e;
            }
        }
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

    public static <A extends Activity & PermissionResultHandler> void requestPermission(A ctx, String perm,
                                                                                        Runnable onSuccess, Runnable onFailed) {
        requestPermissions(ctx, new String[]{perm}, onSuccess, l -> onFailed.run());
    }

    public static <A extends Activity & PermissionResultHandler> void requestPermissions(A ctx, String[] perms,
                                                                                         Runnable onSuccess, Consumer<List<String>> onFailed) {
        if (onSuccess == null) {
            onSuccess = () -> {
            };
        }
        if (onFailed == null) {
            onFailed = l -> {
            };
        }
        for (String perm : perms) {
            if (ActivityCompat.checkSelfPermission(ctx, perm) != PackageManager.PERMISSION_GRANTED) {
                int id = nextId.incrementAndGet();
                permissionSuccessHandlers.put(id, onSuccess);
                permissionFailedHandlers.put(id, onFailed);
                ActivityCompat.requestPermissions(ctx, perms, id);
                return;
            }
            onSuccess.run();
        }
    }

    public static void startIntentForResult(Activity ctx, Intent intent, @NotNull BiConsumer<Integer, Intent> resultHandler) {
        int id = nextId.incrementAndGet();
        intentResultHandlers.put(id, resultHandler);
        ctx.startActivityForResult(intent, id);
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

    public static Activity getActivity(View v) {
        Context context = v.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * For being called by the activity's onActivityResult
     */
    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        BiConsumer<Integer, Intent> handler = intentResultHandlers.get(requestCode);
        if (handler != null) {
            handler.accept(resultCode, data);
            return true;
        } else {
            return false;
        }
    }

    public interface PermissionResultHandler extends ActivityCompat.OnRequestPermissionsResultCallback {
        default void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            Runnable onSuccess = permissionSuccessHandlers.get(requestCode);
            Consumer<List<String>> onFailed = permissionFailedHandlers.get(requestCode);
            if (onSuccess != null && onFailed != null) {
                List<String> res = new ArrayList<>();
                if (permissions.length == 0) {
                    onFailed.accept(res);
                } else {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            res.add(permissions[i]);
                        }
                    }
                    if (res.isEmpty()) {
                        onSuccess.run();
                    } else {
                        onFailed.accept(res);
                    }
                }
            } else if (onSuccess != null || onFailed != null) {
                Crashlytics.logException(new IllegalStateException("permission request only has one handler!"));
            }
        }
    }
}
