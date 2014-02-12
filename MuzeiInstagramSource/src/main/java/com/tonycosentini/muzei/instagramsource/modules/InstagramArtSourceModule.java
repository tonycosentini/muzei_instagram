package com.tonycosentini.muzei.instagramsource.modules;

import com.tonycosentini.muzei.instagramsource.InstagramArtSource;
import com.tonycosentini.muzei.instagramsource.SettingsActivity;
import com.tonycosentini.muzei.instagramsource.data.InstagramService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;

/**
 * Created by tonyc on 2/11/14.
 */
@Module(
    injects = {
        SettingsActivity.class,
        InstagramArtSource.class
    },
    complete = false
)
public class InstagramArtSourceModule {
  @Provides InstagramService provideInstagramService() {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint("https://api.instagram.com/v1")
        .build();

    return restAdapter.create(InstagramService.class);
  }
}
