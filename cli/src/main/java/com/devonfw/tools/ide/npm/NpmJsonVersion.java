package com.devonfw.tools.ide.npm;

import com.devonfw.tools.ide.common.JsonVersionItem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON data object for a version of Npm. We map only properties that we are interested in and let jackson ignore all others.
 *
 * @see NpmJsonObject#getVersions()
 */
public class NpmJsonVersion implements JsonVersionItem {

  @JsonProperty("version")
  private String version;

  @JsonProperty("dist")
  private NpmJsonDist dist;

  public String getVersion() {

    return version;
  }

  public NpmJsonDist getDist() {

    return dist;
  }
}
