package com.example.samsung.p1301_audiorecorder;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * The class {@code Messager} is designed to send messages
 * to various recipients in the operating system
 *
 * The class contains the following methods:
 * <ul>
 *     <li>
 *         <p>{@code sendToOnlyLog()} - a method that generates a log entry</p>
 *     </li>
 *     <li>
 *         <p>{@code sendToAllRecipients()} - a method that generates a log entry
 *         and a pop-up message for the activity</p>
 *     </li>
 * </ul>
 *
 * Created by
 * @author Vladimir Bobkov
 * on 03.06.2017.
 */

public class Messager {

    /**
     * This method that generates a log entry based on passed parameters:
     * @param cxt - an instance of the class  {@code Context} according
     *            to the data to generate tag entry in the log
     * @param msg - an instance of the class  {@code String} reports
     *            data for generating log entries
     */
    public static void sendToOnlyLog (final Context cxt, final String msg) {

        if (msg.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for (Field field :
                cxt.getClass().getDeclaredFields()) {
            String logTag = field.getName();
            if (logTag.equals("TAG")) {
                Log.d(logTag, msg);
            }
        }
    }

    /**
     * This method that generates a log entry and a pop-up message
     * for the activity based on passed parameters:
     * @param cxt - an instance of the class  {@code Context} according
     *            to the data to generate the tag entries in the log
     *            and the context for a toast message
     * @param msg - an instance of the class  {@code String} according
     *            to the data to generate a log entry and a pop-up message
     */
    public static void sendToAllRecipients(final Context cxt, final String msg) {
        Toast.makeText(cxt, msg, Toast.LENGTH_SHORT).show();
        sendToOnlyLog(cxt, msg);
    }
}