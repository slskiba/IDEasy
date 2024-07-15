package com.devonfw.tools.ide.tool.python;

import com.devonfw.tools.ide.json.mapping.JsonMapping;
import com.devonfw.tools.ide.url.model.folder.UrlEdition;
import com.devonfw.tools.ide.url.model.folder.UrlRepository;
import com.devonfw.tools.ide.url.model.folder.UrlTool;
import com.devonfw.tools.ide.url.model.folder.UrlVersion;
import com.devonfw.tools.ide.url.updater.JsonUrlUpdater;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * The {@Link JsonUrlUpdater} for Python
 */
public class PythonUrlUpdater extends JsonUrlUpdater<PythonJsonObject, PythonRelease> {

  /**
   * The base Url of the Python versions Json
   */
  private String VERSION_BASE_URL = "https://raw.githubusercontent.com";

  private final static String VERSION_FILENAME = "actions/python-versions/main/versions-manifest.json";

  final static ObjectMapper MAPPER = JsonMapping.create();

  private static final Logger logger = LoggerFactory.getLogger(PythonUrlUpdater.class);

  @Override
  protected String getTool() {

    return "python";
  }

  @Override
  protected void addVersion(UrlVersion urlVersion, PythonRelease release) {

    String version = release.getVersion();

    try {
      for (PythonFile download : release.getFiles()) {
        if (download.getPlatform().equals("win32") && download.getArch().equals("x64")) {
          doAddVersion(urlVersion, download.getDownloadUrl(), WINDOWS, X64);
        } else if (download.getPlatform().equals("linux") && download.getArch().equals("x64")) {
          doAddVersion(urlVersion, download.getDownloadUrl(), LINUX, X64);
        } else if (download.getPlatform().equals("darwin") && download.getArch().equals("arm64")) {
          doAddVersion(urlVersion, download.getDownloadUrl(), MAC, ARM64);
        } else {
          logger.info("Unknown architecture for tool {} version {} and download {}.", getToolWithEdition(), version,
              download.getDownloadUrl());
        }
      }
      urlVersion.save();
    } catch (Exception exp) {
      logger.error("For tool {} we failed to add version {}.", getToolWithEdition(), version, exp);
    }
  }

  /**
   * @return String of version base Url
   */
  protected String getVersionBaseUrl() {

    return this.VERSION_BASE_URL;
  }

  @Override
  protected String doGetVersionUrl() {

    return getVersionBaseUrl() + "/" + VERSION_FILENAME;
  }

  @Override
  protected Class<PythonJsonObject> getJsonObjectType() {

    return PythonJsonObject.class;
  }

  @Override
  public void update(UrlRepository urlRepository) {

    UrlTool tool = urlRepository.getOrCreateChild(getTool());
    UrlEdition edition = tool.getOrCreateChild(getEdition());
    updateExistingVersions(edition);
    try {
      String response = doGetResponseBodyAsString(doGetVersionUrl());

      PythonRelease[] res = MAPPER.readValue(response, PythonRelease[].class);

      PythonJsonObject jsonObj = new PythonJsonObject();
      jsonObj.setReleases(List.of(res));

      collectVersionsWithDownloadsFromJson(jsonObj, edition);

    } catch (Exception e) {
      throw new IllegalStateException("Error while getting versions from JSON API " + doGetVersionUrl(), e);
    }
  }

  @Override
  protected Collection<PythonRelease> getVersionItems(PythonJsonObject jsonObject) {

    return jsonObject.getReleases();
  }

  @Override
  protected String getVersion(PythonRelease jsonVersionItem) {

    return jsonVersionItem.getVersion();
  }

}
