package org.aminesidki.util;

import picocli.CommandLine;

/** Static utility that prints the application's banner */
public class BannerPrinter {

  private static final String SPROUT_BANNER =
      "  .   ____                        _   \n"
          + " /\\\\ / ___| _ __  _ __ ___  _   _| |_ \n"
          + "( ( )\\___ \\| '_ \\| '__/ _ \\| | | | __|\n"
          + " \\\\/  ___) | |_) | | | (_) | |_| | |_ \n"
          + "  '  |____/| .__/|_|  \\___/ \\__,_|\\___|\n"
          + "           |_|                         ";

  public static void print() {
    String coloredBanner =
        CommandLine.Help.Ansi.AUTO.string(
            "@|green "
                + SPROUT_BANNER
                + "|@\n"
                + "@|faint   :: Sprout ::|@            @|yellow (v"
                + VersionUtil.version
                + "-Stable release)|@\n");

    System.out.println();
    System.out.println(coloredBanner);
    System.out.println();
  }
}
