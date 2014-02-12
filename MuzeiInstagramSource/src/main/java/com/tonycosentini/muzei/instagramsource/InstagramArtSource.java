package com.tonycosentini.muzei.instagramsource;

import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.tonycosentini.muzei.instagramsource.data.CredentialsHolder;
import com.tonycosentini.muzei.instagramsource.data.InstagramFeed;
import com.tonycosentini.muzei.instagramsource.data.InstagramService;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tonyc on 2/11/14.
 */
public class InstagramArtSource extends RemoteMuzeiArtSource {
  private static final String SOURCE_NAME = "InstagramArtSource";

  @Inject InstagramService instagramService;
  @Inject CredentialsHolder credentialsHolder;

  public InstagramArtSource() {
    super(SOURCE_NAME);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    ((InstagramArtSourceApplication) getApplication()).inject(this);

    setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
  }

  @Override protected void onTryUpdate(int i) throws RetryException {
    if (credentialsHolder.isAccountAuthorized()) {
      instagramService.getUserMedia(credentialsHolder.getAccountTokenKey(), new Callback<InstagramFeed>() {
        @Override public void success(InstagramFeed instagramFeed, Response response) {
          useRandomPhoto(instagramFeed.items);
        }

        @Override public void failure(RetrofitError retrofitError) {

        }
      });
    }
  }

  private void useRandomPhoto(List<InstagramFeed.Item> items) {
    Random randomGenerator = new Random();
    int index = randomGenerator.nextInt(items.size());

    InstagramFeed.Item item = items.get(index);

    Artwork.Builder builder = new Artwork.Builder();
    builder.imageUri(Uri.parse(item.images.standardResolution.url));

    // Need to check if Instagram always returns these.
    if (item.caption != null && item.caption.text != null) {
      builder.title(item.caption.text);
    }

    builder.byline(item.user.username);

    publishArtwork(builder.build());
  }
}
