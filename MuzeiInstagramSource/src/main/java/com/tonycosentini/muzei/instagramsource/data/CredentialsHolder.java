package com.tonycosentini.muzei.instagramsource.data;

import android.content.SharedPreferences;
import javax.inject.Inject;

/**
 * Created by tonyc on 2/11/14.
 */
public class CredentialsHolder {
  @Inject SharedPreferences sharedPreferences;

  private static final String ACCOUNT_TOKEN_KEY = "com.tonycosentini.muzei.instagramsource.account_token";

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

  public void removeAccount() {
    setAccountTokenKey(null);
  }
}
