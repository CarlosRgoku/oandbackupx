/*
 * OAndBackupX: open-source apps backup and restore app.
 * Copyright (C) 2020  Antonios Hazim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.machiav3lli.backup.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.machiav3lli.backup.Constants;
import com.machiav3lli.backup.R;
import com.machiav3lli.backup.handler.HandleMessages;
import com.machiav3lli.backup.handler.ShellCommands;
import com.machiav3lli.backup.items.ActionResult;

public class UIUtils {
    private static final String TAG = Constants.classTag(".UIUtils");

    public static void showActionResult(final Activity activity, final ActionResult result, DialogInterface.OnClickListener saveMethod) {
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setPositiveButton(R.string.dialogOK, null);
            if (saveMethod != null) {
                builder.setNegativeButton(R.string.dialogSave, saveMethod);
            }
            if (!result.succeeded) {
                builder.setTitle(R.string.errorDialogTitle)
                        .setMessage(result.getMessage());
                builder.show();
            }
        });
    }

    public static void showErrors(final Activity activity) {
        activity.runOnUiThread(() -> {
            String errors = ShellCommands.getErrors();
            if (errors.length() > 0) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.errorDialogTitle)
                        .setMessage(errors)
                        .setPositiveButton(R.string.dialogOK, null)
                        .show();
                ShellCommands.clearErrors();
            }
        });
    }

    public static void showWarning(final Activity activity, final String title, final String message) {
        UIUtils.showWarning(activity, title, message, (dialog, id) -> {
        });
    }

    public static void showWarning(final Activity activity, final String title, final String message, final DialogInterface.OnClickListener callback) {
        activity.runOnUiThread(() -> new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialogOK, callback)
                .setCancelable(false)
                .show());
    }

    public static void reShowMessage(HandleMessages handleMessages, long tid) {
        // since messages are progressdialogs and not dialogfragments they need to be set again manually
        if (tid != -1)
            for (Thread t : Thread.getAllStackTraces().keySet())
                if (t.getId() == tid && t.isAlive())
                    handleMessages.reShowMessage();
    }

    public static void setVisibility(View view, int visibility, boolean withAnimation) {
        view.animate().alpha(visibility == View.VISIBLE ? 1.0f : 0.0f)
                .setDuration(withAnimation ? 600 : 1)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (visibility == View.VISIBLE && view.getVisibility() == View.GONE)
                            view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(visibility);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // not relevant
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // not relevant
                    }
                });
    }
}
