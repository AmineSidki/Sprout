package org.AmineSidki.util;

import picocli.CommandLine.Help.Ansi;

public class BannerPrinter {

    private static final String SPROUT_BANNER =
            "  ____                             _   \n" +
                    " / ___|  _ __   _ __   ___   _   _ | |_ \n" +
                    " \\___ \\ | '_ \\ | '__| / _ \\ | | | || __|\n" +
                    "  ___) || |_) || |   | (_) || |_| || |_ \n" +
                    " |____/ | .__/ |_|    \\___/  \\__,_| \\__|\n" +
                    "        |_|                             ";

    public static void print() {
        String coloredBanner = Ansi.AUTO.string(
                "@|bold,green " + SPROUT_BANNER + "|@\n" +
                        "@|cyan  :: Sprout Generator :: |@ @|yellow (v1.1-MVP)|@\n"
        );

        System.out.println(coloredBanner);
    }
}