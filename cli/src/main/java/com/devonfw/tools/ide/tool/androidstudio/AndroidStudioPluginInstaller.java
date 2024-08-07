package com.devonfw.tools.ide.tool.androidstudio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.devonfw.tools.ide.context.IdeContext;
import com.devonfw.tools.ide.os.MacOsHelper;
import com.devonfw.tools.ide.tool.ide.IdeToolCommandlet;
import com.devonfw.tools.ide.tool.ide.PluginDescriptor;
import com.devonfw.tools.ide.tool.ide.PluginInstaller;

/**
 * Plugin Installer for {@link AndroidStudio}.
 */
public class AndroidStudioPluginInstaller extends PluginInstaller {

  private static final String BUILD_FILE = "build.txt";

  /**
   * The constructor.
   *
   * @param context the {@link IdeContext}.
   * @param commandlet the {@link IdeToolCommandlet}
   */
  public AndroidStudioPluginInstaller(IdeContext context, IdeToolCommandlet commandlet) {
    super(context, commandlet);
  }

  /**
   * @param plugin the {@link PluginDescriptor} to be installer
   * @return a {@link String} representing the download URL.
   */
  public String getDownloadUrl(PluginDescriptor plugin) {
    String downloadUrl = plugin.getUrl();
    String pluginId = plugin.getId();

    String buildVersion = readBuildVersion();

    if (downloadUrl == null || downloadUrl.isEmpty()) {
      downloadUrl = String.format("https://plugins.jetbrains.com/pluginManager?action=download&id=%s&build=%s", pluginId, buildVersion);
    }
    return downloadUrl;
  }

  private String readBuildVersion() {
    Path buildFile = commandlet.getToolPath().resolve(BUILD_FILE);
    if (context.getSystemInfo().isMac()) {
      MacOsHelper macOsHelper = new MacOsHelper(context);
      Path rootToolPath = macOsHelper.findRootToolPath(this.commandlet, context);
      buildFile = rootToolPath.resolve("Android Studio Preview" + ".app").resolve("Contents/Resources").resolve(BUILD_FILE);
    }
    try {
      return Files.readString(buildFile);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read AndroidStudio build version: " + buildFile, e);
    }
  }

}