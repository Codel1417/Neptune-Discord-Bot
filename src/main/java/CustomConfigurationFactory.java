import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

// Configures the logger
// https://logging.apache.org/log4j/2.x/manual/customconfig.html

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class CustomConfigurationFactory extends ConfigurationFactory {

    static Configuration createConfiguration(
            final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.WARN);
        builder.add(
                builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                        .addAttribute("level", Level.WARN));
        AppenderComponentBuilder appenderBuilder =
                builder.newAppender("Stdout", "CONSOLE")
                        .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(
                builder.newLayout("PatternLayout")
                        .addAttribute(
                                "pattern",
                                "%highlight{[%d][%location] - %msg%n}{FATAL=red blink, ERROR=red,"
                                        + " WARN=yellow bold, INFO=white, DEBUG=green bold,"
                                        + " TRACE=blue}"));
        appenderBuilder.add(
                builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL)
                        .addAttribute("marker", "FLOW"));
        builder.add(appenderBuilder);
        builder.add(
                builder.newLogger("org.apache.logging.log4j", Level.WARN)
                        .add(builder.newAppenderRef("Stdout"))
                        .addAttribute("additivity", false));
        builder.add(
                builder.newRootLogger(Level.DEBUG)
                        .add(builder.newAppenderRef("Stdout"))); // controls overall log level

        return builder.build();
    }

    @Override
    public Configuration getConfiguration(
            final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(
            final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}
