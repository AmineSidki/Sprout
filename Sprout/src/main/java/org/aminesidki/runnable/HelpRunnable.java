package org.aminesidki.runnable;

public class HelpRunnable implements Runnable {
  @Override
  public void run() {
    String helpMenu =
        """
        ┌──────────────┬───────────┬──────────────────────────────────────────────┐
        │ Flag         │ Shortened │ Effect                                       │
        ├──────────────┼───────────┼──────────────────────────────────────────────┤
        │ --dir        │           │ Changes target directory to given argument   │
        │ --version    │    -v     │ Displays the currently installed version     │
        │ --partial    │    -p     │ Toggles partial generation mode              │
        │ --repository │    -r     │ Toggles repository generation (Needs -p)     │
        │ --dto        │    -d     │ Toggles dto generation (Needs -p)            │
        │ --mapper     │    -m     │ Toggles mapper generation (Needs -p)         │
        │ --service    │    -s     │ Toggles service generation (Needs -p)        │
        │ --controller │    -c     │ Toggles controller generation (Needs -p)     │
        │ --exception  │    -e     │ Toggles exception generation (Needs -p)      │
        └──────────────┴───────────┴──────────────────────────────────────────────┘
        """;

    System.out.println(helpMenu);
  }
}
