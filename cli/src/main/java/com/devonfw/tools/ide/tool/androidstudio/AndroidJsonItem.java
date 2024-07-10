package com.devonfw.tools.ide.tool.androidstudio;

import com.devonfw.tools.ide.common.JsonVersionItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * JSON data object for an item of Android. We map only properties that we are interested in and let jackson ignore all
 * others.
 */
public class AndroidJsonItem implements JsonVersionItem {

  @JsonProperty("version")
  private String version;

  @JsonProperty("download")
  private List<AndroidJsonDownload> download;

  /**
   * @return version
   */
  public String getVersion() {

    return this.version;
  }

  /**
   * @return download
   */
  public List<AndroidJsonDownload> getDownload() {

    return this.download;
  }

}