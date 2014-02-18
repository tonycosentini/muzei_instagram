package com.tonycosentini.muzei.instagramsource.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by tonyc on 2/12/14.
 */
public class InstagramFeed {
  public static final String TYPE_IMAGE = "image";
  public static final String TYPE_VIDEO = "video";

  public @SerializedName("data") List<Item> items;

  public static class Item {
    public @SerializedName("type") String type;
    public @SerializedName("images") Images images;
    public @SerializedName("caption") Caption caption;
    public @SerializedName("user") User user;

    public static class User {
      public @SerializedName("username") String username;
    }

    public static class Caption {
      public @SerializedName("text") String text;
    }

    public static class Images {
      public @SerializedName("standard_resolution") Image standardResolution;

      public static class Image {
        public @SerializedName("url") String url;
      }
    }
  }
}
