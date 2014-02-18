package com.tonycosentini.muzei.instagramsource;

import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.tonycosentini.muzei.instagramsource.data.PreferencesHolder;
import com.tonycosentini.muzei.instagramsource.data.InstagramFeed;
import com.tonycosentini.muzei.instagramsource.data.InstagramService;
import java.util.ArrayList;
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
  @Inject PreferencesHolder preferencesHolder;

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
    if (preferencesHolder.isAccountAuthorized()) {
      scheduleNext();

      if (preferencesHolder.getPhotosToDisplaySetting() == PreferencesHolder.PHOTOS_TO_DISPLAY_MY_PHOTOS) {
        instagramService.getUserMedia(preferencesHolder.getAccountTokenKey(), updateCallback);
      } else if (preferencesHolder.getPhotosToDisplaySetting() == PreferencesHolder.PHOTOS_TO_DISPLAY_MY_FEED) {
        instagramService.getUserFeed(preferencesHolder.getAccountTokenKey(), updateCallback);
      }
    }
  }

  private Callback<InstagramFeed> updateCallback = new Callback<InstagramFeed>() {
    @Override public void success(InstagramFeed instagramFeed, Response response) {
      List<InstagramFeed.Item> imageItems = new ArrayList<InstagramFeed.Item>();
      for (InstagramFeed.Item item: instagramFeed.items) {
        if (item.type.equals(InstagramFeed.TYPE_IMAGE)) {
          imageItems.add(item);
        }
      }

      useRandomPhoto(imageItems);
    }

    @Override public void failure(RetrofitError retrofitError) {

    }
  };

  private void useRandomPhoto(List<InstagramFeed.Item> items) {
    if (items.size() > 0) {
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

  private void scheduleNext() {
    int rotateIntervalMinutes = preferencesHolder.getRotationSetting();
    if (rotateIntervalMinutes > 0) {
      scheduleUpdate(System.currentTimeMillis() + rotateIntervalMinutes * 60 * 1000);
    }
  }
}
