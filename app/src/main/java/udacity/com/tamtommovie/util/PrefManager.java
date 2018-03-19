package udacity.com.tamtommovie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class PrefManager {

    private static final Integer DEFAULT_INT = -1;
    private static final Boolean DEFAULT_BOOLEAN = false;
    private static final Long DEFAULT_LONG = -1L;
    private static volatile PrefManager mInstance;
    private SharedPreferences pref;
    private final SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    private PrefManager(Context context) {
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = pref.edit();
    }

    public static PrefManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PrefManager.class) {
                if (mInstance == null) {
                    mInstance = new PrefManager(context);
                }
            }
        }
        return mInstance;
    }

    public boolean putInt(String key, Integer value) {
        return storeEntry(editor.putInt(key, value));
    }

    private boolean storeEntry(SharedPreferences.Editor editor) {
        return editor.commit();
    }

    public Integer getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    public Integer getInt(String key, Integer defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public boolean putLong(String key, Long value) {
        return storeEntry(editor.putLong(key, value));
    }

    public Long getLong(String key) {
        return getLong(key, DEFAULT_LONG);
    }

    public Long getLong(String key, Long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public boolean putBoolean(String key, Boolean value) {
        return storeEntry(editor.putBoolean(key, value));
    }

    public boolean putString(String key, String value) {
        return storeEntry(editor.putString(key, value));
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
}
