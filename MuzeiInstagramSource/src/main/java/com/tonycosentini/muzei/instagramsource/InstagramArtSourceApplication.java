package com.tonycosentini.muzei.instagramsource;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.tonycosentini.muzei.instagramsource.modules.AndroidModule;
import com.tonycosentini.muzei.instagramsource.modules.InstagramArtSourceModule;
import dagger.ObjectGraph;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tonyc on 2/11/14.
 */
public class InstagramArtSourceApplication extends Application {
  private ObjectGraph graph;

  @Override public void onCreate() {
    super.onCreate();
    Crashlytics
    Crashlytics.start(this);


    graph = ObjectGraph.create(getModules().toArray());
  }

  protected List<Object> getModules() {
    return Arrays.asList(new AndroidModule(this), new InstagramArtSourceModule());
  }

  public void inject(Object object) {
    graph.inject(object);
  }
}
