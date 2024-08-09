package com.devonfw.tools.ide.commandlet;

import com.devonfw.tools.ide.context.IdeContext;
import com.devonfw.tools.ide.property.PluginProperty;
import com.devonfw.tools.ide.property.ToolProperty;
import com.devonfw.tools.ide.tool.ToolCommandlet;
import com.devonfw.tools.ide.tool.plugin.PluginBasedCommandlet;

/**
 * {@link Commandlet} to install a tool.
 *
 * @see ToolCommandlet#install()
 */
public class UninstallPluginCommandlet extends Commandlet {

  /** The tool to install. */
  public final ToolProperty tool;

  /** The optional version to set and install. */
  public final PluginProperty plugin;

  /**
   * The constructor.
   *
   * @param context the {@link IdeContext}.
   */
  public UninstallPluginCommandlet(IdeContext context) {

    super(context);
    addKeyword(getName());
    this.tool = add(new ToolProperty("", true, "tool"));
    this.plugin = add(new PluginProperty("", false, "plugin"));
  }

  @Override
  public String getName() {

    return "uninstall-plugin";
  }

  @Override
  public void run() {
    ToolCommandlet commandlet = this.tool.getValue();
    String plugin = this.plugin.getValue();

    if (commandlet instanceof PluginBasedCommandlet) {
      PluginBasedCommandlet cmd = (PluginBasedCommandlet) commandlet;
      cmd.uninstallPlugin(cmd.getPlugin(plugin));
    } else {
      context.warning("Tool {} does not support plugins.", tool.getName());
    }
  }

}
