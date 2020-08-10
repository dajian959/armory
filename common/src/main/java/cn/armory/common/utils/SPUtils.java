package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences相关工具类
 */
public class SPUtils {
    private static SPUtils sSharedPreferencesUtils;

    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;
    private static SharedPreferencesCompat.EditorCompat editorCompat = SharedPreferencesCompat.EditorCompat.getInstance();

    private static final String DEFAULT_SP_NAME = "utils_sp";
    private static final int DEFAULT_INT = 0;
    private static final float DEFAULT_FLOAT = 0.0f;
    private static final String DEFAULT_STRING = "";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final Set<String> DEFAULT_STRING_SET = new HashSet<>(0);

    private static String mCurSPName = DEFAULT_SP_NAME;

    private SPUtils() {
        this(DEFAULT_SP_NAME);
    }

    @SuppressLint("CommitPrefEdits")
    private SPUtils(String spName) {
        sSharedPreferences = ACUtils.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
        mCurSPName = spName;
    }

    public static SPUtils init() {
        if (sSharedPreferencesUtils == null || !mCurSPName.equals(DEFAULT_SP_NAME)) {
            sSharedPreferencesUtils = new SPUtils();
        }
        return sSharedPreferencesUtils;
    }

    public static SPUtils init(String spName) {
        if (sSharedPreferencesUtils == null) {
            sSharedPreferencesUtils = new SPUtils(spName);
        } else if (!spName.equals(mCurSPName)) {
            sSharedPreferencesUtils = new SPUtils(spName);
        }
        return sSharedPreferencesUtils;
    }

    public SPUtils put(@StringRes int key, Object value) {
        return put(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils put(String key, Object value) {
        if (value == null) return sSharedPreferencesUtils;
        if (value instanceof String) {
            sEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            sEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            sEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            sEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            sEditor.putLong(key, (Long) value);
        } else {
            sEditor.putString(key, value.toString());
        }
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public Object get(@StringRes int key, Object defaultObject) {
        return get(ACUtils.getApplicationContext().getString(key), defaultObject);
    }

    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sSharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sSharedPreferences.getInt(key, (int) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sSharedPreferences.getBoolean(key, (boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sSharedPreferences.getFloat(key, (float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sSharedPreferences.getLong(key, (long) defaultObject);
        }
        return null;
    }

    public SPUtils putInt(String key, int value) {
        sEditor.putInt(key, value);
        editorCompat.apply(sEditor);
        return this;
    }

    public SPUtils putInt(@StringRes int key, int value) {
        return putInt(ACUtils.getApplicationContext().getString(key), value);
    }

    public int getInt(@StringRes int key) {
        return getInt(ACUtils.getApplicationContext().getString(key));
    }

    public int getInt(@StringRes int key, int defValue) {
        return getInt(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }


    public int getInt(String key, int defValue) {
        return sSharedPreferences.getInt(key, defValue);
    }

    public SPUtils putFloat(@StringRes int key, float value) {
        return putFloat(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils putFloat(String key, float value) {
        sEditor.putFloat(key, value);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public float getFloat(String key) {
        return getFloat(key, DEFAULT_FLOAT);
    }

    public float getFloat(String key, float defValue) {
        return sSharedPreferences.getFloat(key, defValue);
    }

    public float getFloat(@StringRes int key) {
        return getFloat(ACUtils.getApplicationContext().getString(key));
    }

    public float getFloat(@StringRes int key, float defValue) {
        return getFloat(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public SPUtils putLong(@StringRes int key, long value) {
        return putLong(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils putLong(String key, long value) {
        sEditor.putLong(key, value);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public long getLong(String key) {
        return getLong(key, DEFAULT_INT);
    }

    public long getLong(String key, long defValue) {
        return sSharedPreferences.getLong(key, defValue);
    }

    public long getLong(@StringRes int key) {
        return getLong(ACUtils.getApplicationContext().getString(key));
    }

    public long getLong(@StringRes int key, long defValue) {
        return getLong(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public SPUtils putString(@StringRes int key, String value) {
        return putString(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils putString(String key, String value) {
        sEditor.putString(key, value);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    public String getString(String key, String defValue) {
        return sSharedPreferences.getString(key, defValue);
    }

    public String getString(@StringRes int key) {
        return getString(ACUtils.getApplicationContext().getString(key), DEFAULT_STRING);
    }

    public String getString(@StringRes int key, String defValue) {
        return getString(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public SPUtils putBoolean(@StringRes int key, boolean value) {
        return putBoolean(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils putBoolean(String key, boolean value) {
        sEditor.putBoolean(key, value);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sSharedPreferences.getBoolean(key, defValue);
    }

    public boolean getBoolean(@StringRes int key) {
        return getBoolean(ACUtils.getApplicationContext().getString(key));
    }

    public boolean getBoolean(@StringRes int key, boolean defValue) {
        return getBoolean(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public SPUtils putStringSet(@StringRes int key, Set<String> value) {
        return putStringSet(ACUtils.getApplicationContext().getString(key), value);
    }

    public SPUtils putStringSet(String key, Set<String> value) {
        sEditor.putStringSet(key, value);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, DEFAULT_STRING_SET);
    }


    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sSharedPreferences.getStringSet(key, defValue);
    }

    public Set<String> getStringSet(@StringRes int key) {
        return getStringSet(ACUtils.getApplicationContext().getString(key));
    }

    public Set<String> getStringSet(@StringRes int key, Set<String> defValue) {
        return getStringSet(ACUtils.getApplicationContext().getString(key), defValue);
    }

    public boolean contains(String key) {
        return sSharedPreferences.contains(key);
    }

    public boolean contains(@StringRes int key) {
        return contains(ACUtils.getApplicationContext().getString(key));
    }

    public Map<String, ?> getAll() {
        return sSharedPreferences.getAll();
    }

    public SPUtils remove(@StringRes int key) {
        return remove(ACUtils.getApplicationContext().getString(key));
    }

    public SPUtils remove(String key) {
        sEditor.remove(key);
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public SPUtils clear() {
        sEditor.clear();
        editorCompat.apply(sEditor);
        return sSharedPreferencesUtils;
    }

    public SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

}