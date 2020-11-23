package neptune.storage;

import org.apache.commons.cli.CommandLine;

public class commandLineOptionsSingleton {
    private static volatile commandLineOptionsSingleton _instance;

    private commandLineOptionsSingleton() {}

    public static synchronized commandLineOptionsSingleton getInstance() {
        if (_instance == null) {
            synchronized (commandLineOptionsSingleton.class) {
                if (_instance == null) _instance = new commandLineOptionsSingleton();
            }
        }
        return _instance;
    }

    private CommandLine options;

    public void setOptions(CommandLine options) {
        this.options = options;
    }

    public CommandLine getOptions() {
        return options;
    }
}
