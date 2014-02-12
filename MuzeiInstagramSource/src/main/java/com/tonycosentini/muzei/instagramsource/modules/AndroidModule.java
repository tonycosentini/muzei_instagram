package com.tonycosentini.muzei.instagramsource.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.tonycosentini.muzei.instagramsource.InstagramArtSourceApplication;
import com.tonycosentini.muzei.instagramsource.SettingsActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by tonyc on 2/11/14.
 */
@Module(library = true)
public class AndroidModule {
  private final InstagramArtSourceApplication application;

  public AndroidModule(InstagramArtSourceApplication application) {
    this.application = application;
  }

  @Provides @Singleton @ForApplication Context provideApplicationContext() {
    return application;
  }

  @Provides SharedPreferences provideSharedPreferences(@ForApplication Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }
}
