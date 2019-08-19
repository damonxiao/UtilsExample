package com.ll.commonutils;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * xiaofang
 * 19-8-19
 **/
public final class IntentUtils {
    private static final String TAG = "IntentUtils";
    private static final SparseArray<String> ACTIVITY_FLAG_FIELD_MAP = new SparseArray<>();
    private static final SparseArray<String> RECEIVER_FLAG_FIELD_MAP = new SparseArray<>();

    static {
        invokeAllActivityFlags();
        invokeAllReceiverFlags();
    }

    public static String activityFlagsToString(int flags) {
        return intentFlagsToString(flags, ACTIVITY_FLAG_FIELD_MAP);
    }

    public static String receiverFlagsToString(int flags) {
        return intentFlagsToString(flags, RECEIVER_FLAG_FIELD_MAP);
    }

    private static String intentFlagsToString(int flags, SparseArray<String> map) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < map.size(); i++) {
            int flag = map.keyAt(i);
            if ((flags & flag) == flag) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("flag_name", map.get(flag));
                    jsonObject.put("flag_hex_value", "0x" + Integer.toHexString(flag));
                    jsonObject.put("flag_int_value", flag);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray.toString();
    }

    private static void invokeAllActivityFlags() {
        invokeAllIntentFlags(ACTIVITY_FLAG_FIELD_MAP, "FLAG_ACTIVITY_");
    }

    private static void invokeAllReceiverFlags() {
        invokeAllIntentFlags(RECEIVER_FLAG_FIELD_MAP, "FLAG_RECEIVER_");
    }

    private static void invokeAllIntentFlags(final SparseArray<String> map, final String flagPrefix) {
        Log.d(TAG, "invokeAllIntentFlags flagPrefix : " + flagPrefix);
        Field[] fields = Intent.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getName().startsWith(flagPrefix)) {
                    map.put(field.getInt(null), field.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
