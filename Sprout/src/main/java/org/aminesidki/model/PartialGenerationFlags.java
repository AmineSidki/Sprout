package org.aminesidki.model;

import picocli.CommandLine;

public class PartialGenerationFlags {
  @CommandLine.Option(
      names = {"-p", "--partial"},
      required = true,
      description = "Partial generation")
  public boolean pFlag;

  @CommandLine.Option(names = {"-r", "--repository"})
  public boolean rFlag;

  @CommandLine.Option(names = {"-d", "--dto"})
  public boolean dFlag;

  @CommandLine.Option(names = {"-s", "--service"})
  public boolean sFlag;

  @CommandLine.Option(names = {"-m", "--mapper"})
  public boolean mFlag;

  @CommandLine.Option(names = {"-c", "--controller"})
  public boolean cFlag;

  @CommandLine.Option(names = {"-e", "--exception"})
  public boolean eFlag;

  public static PartialGenerationFlags allEnabled() {
    PartialGenerationFlags flags = new PartialGenerationFlags();
    flags.pFlag =
        flags.rFlag = flags.dFlag = flags.sFlag = flags.mFlag = flags.cFlag = flags.eFlag = true;
    return flags;
  }
}
