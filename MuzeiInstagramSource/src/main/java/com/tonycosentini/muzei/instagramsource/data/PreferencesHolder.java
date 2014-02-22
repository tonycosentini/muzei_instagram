package com.tonycosentini.muzei.instagramsource.data;

import android.content.SharedPreferences;
import android.util.SparseIntArray;
import com.tonycosentini.muzei.instagramsource.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by tonyc on 2/11/14.
 */
public class PreferencesHolder {
  @Inject SharedPreferences sharedPreferences;

  private static final String ACCOUNT_TOKEN_KEY = "com.tonycosentini.muzei.instagramsource.account_token";
  private static final String PHOTOS_TO_DISPLAY = "com.tonycosentini.muzei.instagramsource.photos_to_display";
  private static final String ROTATION_SETTING = "com.tonycosentini.muzei.instagramsource.rotation_setting";

  public static final int PHOTOS_TO_DISPLAY_MY_PHOTOS = 1;
  public static final int PHOTOS_TO_DISPLAY_MY_FEED = 2;
  public static final int PHOTOS_TO_DISPLAY_LIKED = 3;
  public static final int PHOTOS_TO_DISPLAY_POPULAR = 4;

  private static final List<Integer> photosToDisplayOptions = new ArrayList<Integer>(Arrays.asList(
    PHOTOS_TO_DISPLAY_MY_PHOTOS,
    PHOTOS_TO_DISPLAY_MY_FEED,
    PHOTOS_TO_DISPLAY_LIKED,
    PHOTOS_TO_DISPLAY_POPULAR
  ));

  private static final int DEFAULT_ROTATE_INTERVAL_MIN = 60 * 6;

  public boolean isAccountAuthorized() {
    return sharedPreferences.contains(ACCOUNT_TOKEN_KEY);
  }

  public String getAccountTokenKey() {
    return sharedPreferences.getString(ACCOUNT_TOKEN_KEY, null);
  }

  public void setAccountTokenKey(String accountTokenKey) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(ACCOUNT_TOKEN_KEY, accountTokenKey);
    editor.commit();
  }

  public int getPhotosToDisplaySetting() {
    return sharedPreferences.getInt(PHOTOS_TO_DISPLAY, PHOTOS_TO_DISPLAY_MY_PHOTOS);
  }

  public void setPhotosToDisplaySetting(int photosToDisplaySetting) {
    if (!photosToDisplayOptions.contains(photosToDisplaySetting)) {
      throw new RuntimeException("Attempted to set photos to display setting to invalid value.");
    }

    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(PHOTOS_TO_DISPLAY, photosToDisplaySetting);
    editor.commit();
  }

  public int getRotationSetting() {
    return sharedPreferences.getInt(ROTATION_SETTING, DEFAULT_ROTATE_INTERVAL_MIN);
  }

  public void setRotationSetting(int rotationInterval) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(ROTATION_SETTING, rotationInterval);
    editor.commit();
  }

  public void removeAccount() {
    setAccountTokenKey(null);
    setPhotosToDisplaySetting(PHOTOS_TO_DISPLAY_MY_PHOTOS);
  }
}
