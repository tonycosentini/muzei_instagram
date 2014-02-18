package com.tonycosentini.muzei.instagramsource;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.tonycosentini.muzei.instagramsource.data.PreferencesHolder;
import com.tonycosentini.muzei.instagramsource.data.InstagramService;
import javax.inject.Inject;

/**
 * Created by tonyc on 2/11/14.
 */
public class SettingsActivity extends Activity {
  @Inject PreferencesHolder preferencesHolder;
  @Inject InstagramService instagramService;

  private static final String CLIENT_ID = "f3294c50f39b423d89370013ca38d99f";
  private static final String CALLBACK_SCHEME = "muzeiinstagram";

  @InjectView(R.id.authorize_account_button) Button authorizeAccountButton;
  @InjectView(R.id.remove_account_button) Button removeAccountButton;
  @InjectView(R.id.settings_layout) ViewGroup settingsLayout;
  @InjectView(R.id.photos_to_display_radio_group) RadioGroup photosToDisplayRadioGroup;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((InstagramArtSourceApplication) getApplication()).inject(this);

    setContentView(R.layout.activity_settings);
    ButterKnife.inject(this);

    checkURI();

    configureViewState(preferencesHolder.isAccountAuthorized());
  }

  private void configureViewState(boolean loggedIn) {
    if (loggedIn) {
      removeAccountButton.setVisibility(View.VISIBLE);
      authorizeAccountButton.setVisibility(View.GONE);
      settingsLayout.setVisibility(View.VISIBLE);

      photosToDisplayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
          if (checkedId == R.id.my_photos_radio_button) {
            preferencesHolder.setPhotosToDisplaySetting(PreferencesHolder.PHOTOS_TO_DISPLAY_MY_PHOTOS);
          } else if (checkedId == R.id.my_feed_radio_button) {
            preferencesHolder.setPhotosToDisplaySetting(PreferencesHolder.PHOTOS_TO_DISPLAY_MY_FEED);
          }

          // TODO: Reset photo.
        }
      });

      if (preferencesHolder.getPhotosToDisplaySetting() == PreferencesHolder.PHOTOS_TO_DISPLAY_MY_PHOTOS) {
        photosToDisplayRadioGroup.check(R.id.my_photos_radio_button);
      } else if (preferencesHolder.getPhotosToDisplaySetting() == PreferencesHolder.PHOTOS_TO_DISPLAY_MY_FEED) {
        photosToDisplayRadioGroup.check(R.id.my_feed_radio_button);
      } else {
        preferencesHolder.setPhotosToDisplaySetting(PreferencesHolder.PHOTOS_TO_DISPLAY_MY_PHOTOS);
        throw new RuntimeException("Photos to display setting set to invalid value. Resetting and crashing.");
      }
    } else {
      removeAccountButton.setVisibility(View.GONE);
      authorizeAccountButton.setVisibility(View.VISIBLE);
      settingsLayout.setVisibility(View.GONE);
    }
  }

  // TODO: See if there is a way to use parentActivity
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent upIntent = new Intent();
        upIntent.setComponent(new ComponentName("net.nurik.roman.muzei", "com.google.android.apps.muzei.settings.SettingsActivity"));
        NavUtils.navigateUpTo(this, upIntent);

        return true;
      default:
        return false;
    }
  }

  @OnClick(R.id.authorize_account_button) public void authorizeAccountButtonTapped() {
    String uriString = String.format(
        "https://instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token",
        CLIENT_ID, "muzeiinstagram%3A%2F%2Freceived_credentials%2F");
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(uriString));
    startActivity(i);
  }

  @OnClick(R.id.remove_account_button) public void setRemoveAccountButtonTapped() {
    preferencesHolder.removeAccount();
    configureViewState(false);
  }

  private void checkURI() {
    Uri dataUri = getIntent().getData();

    if (dataUri != null && dataUri.getScheme().equals(CALLBACK_SCHEME)) {
      if (dataUri.getEncodedFragment() != null) {
        String accessToken =
            getIntent().getData().getEncodedFragment().replaceFirst("\\Aaccess_token=", "");
        preferencesHolder.setAccountTokenKey(accessToken);
      } else if (dataUri.getQueryParameter("error_reason") != null) {
        Toast.makeText(this, getString(R.string.unable_to_authorize_account), Toast.LENGTH_SHORT)
            .show();
      }
    }
  }
}
