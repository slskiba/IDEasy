package com.devonfw.tools.ide.tool.pgadmin;

import com.devonfw.tools.ide.common.Tag;
import com.devonfw.tools.ide.context.IdeContext;
import com.devonfw.tools.ide.repo.ToolRepository;
import com.devonfw.tools.ide.tool.GlobalToolCommandlet;
import com.devonfw.tools.ide.tool.PackageManager;
import com.devonfw.tools.ide.tool.PackageManagerCommand;
import com.devonfw.tools.ide.version.VersionIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * {@link GlobalToolCommandlet} for <a href="https://www.pgadmin.org/">pgadmin</a>
 */
public class PgAdmin extends GlobalToolCommandlet {
  /**
   * The constructor.
   *
   * @param context the {@link IdeContext}.
   */
  public PgAdmin(IdeContext context) {

    //TODO: add relevant Tag.
    super(context, "pgadmin", Set.of(Tag.MISC));
  }

  @Override
  protected boolean doInstall(boolean silent) {

    if (this.context.getSystemInfo().isLinux()) {
      return installWithPackageManager(silent, getPackageManagerCommands());
    } else {
      return super.doInstall(silent);
    }
  }

  private List<PackageManagerCommand> getPackageManagerCommands() {

    String edition = getEdition();
    ToolRepository toolRepository = this.context.getDefaultToolRepository();
    VersionIdentifier configuredVersion = getConfiguredVersion();
    String resolvedVersion = toolRepository.resolveVersion(this.tool, edition, configuredVersion).toString();

    List<PackageManagerCommand> pmCommands = new ArrayList<>();

    pmCommands.add(new PackageManagerCommand(PackageManager.APT, Arrays.asList(
        "curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | "
            + "sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg",
        "sudo sh -c 'echo \"deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] "
            + "https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main\" "
            + "> /etc/apt/sources.list.d/pgadmin4.list && apt update'",
        String.format("sudo apt install pgadmin4=%1$s pgadmin4-server=%1$s pgadmin4-desktop=%1$s pgadmin4-web=%1$s",
            resolvedVersion))));

    return pmCommands;
  }

  @Override
  protected String getBinaryName() {

    return "pgadmin4";
  }
}