package com.tonycosentini.muzei.instagramsource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.tonycosentini.muzei.instagramsource.data.CredentialsHolder;
import com.tonycosentini.muzei.instagramsource.data.InstagramFeed;
import com.tonycosentini.muzei.instagramsource.data.InstagramService;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tonyc on 2/11/14.
 */
public class SettingsActivity extends Activity {
  @Inject CredentialsHolder credentialsHolder;
  @Inject InstagramService instagramService;

  // TODO: Put this in BuildConfig before committing.
  private static final String CLIENT_ID = "f3294c50f39b423d89370013ca38d99f";
  private static final String CALLBACK_SCHEME = "muzeiinstagram";

  @InjectView(R.id.authorize_account_button) Button authorizeAccountButton;
  @InjectView(R.id.remove_account_button) Button removeAccountButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((InstagramArtSourceApplication) getApplication()).inject(this);

    setContentView(R.layout.activity_settings);
    ButterKnife.inject(this);

    checkURI();

    if (credentialsHolder.isAccountAuthorized()) {
      removeAccountButton.setVisibility(View.VISIBLE);
      authorizeAccountButton.setVisibility(View.GONE);
    } else {
      removeAccountButton.setVisibility(View.GONE);
      authorizeAccountButton.setVisibility(View.VISIBLE);
    }
  }

  @OnClick(R.id.authorize_account_button) public void authorizeAccountButtonTapped() {
    String uriString = String.format("https://instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token", CLIENT_ID, "muzeiinstagram%3A%2F%2Freceived_credentials%2F");
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(uriString));
    startActivity(i);
  }

  @OnClick(R.id.remove_account_button) public void setRemoveAccountButtonTapped() {
    credentialsHolder.removeAccount();
    removeAccountButton.setVisibility(View.GONE);
    authorizeAccountButton.setVisibility(View.VISIBLE);
  }

  private void checkURI() {
    Uri dataUri = getIntent().getData();

    if (dataUri != null && dataUri.getScheme().equals(CALLBACK_SCHEME)) {
      if (dataUri.getEncodedFragment() != null) {
        String accessToken = getIntent().getData().getEncodedFragment().replaceFirst("\\Aaccess_token=", "");
        credentialsHolder.setAccountTokenKey(accessToken);
      } else if (dataUri.getQueryParameter("error_reason") != null) {
        Toast.makeText(this, getString(R.string.unable_to_authorize_account), Toast.LENGTH_SHORT).show();
      }
    }

  }
}
