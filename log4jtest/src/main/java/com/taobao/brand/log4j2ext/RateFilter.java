package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

import java.util.Random;

/**
 * @author jinshuan.li 2018/9/24 12:23
 */
@Plugin(name = "RateFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class RateFilter extends FilterAawre {

    private Random random = new Random(System.currentTimeMillis());

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Decide if we're going to log <code>event</code> based on whether the maximum burst of log statements has been
     * exceeded.
     *
     * @param level The log level.
     * @return The onMatch value if the filter passes, onMismatch otherwise.
     */
    @Override
    protected Result filter(Level level) {

        Boolean skip = true;

        if (level.equals(Level.INFO)) {
            int i = random.nextInt(100);
            if (i < 20) {
                skip = false;
            }

        }
        // 错误日志不跳过
        if (level.equals(Level.ERROR)) {
            skip = false;
        }

        return skip == true ? Result.DENY : Result.NEUTRAL;

    }

    public static class Builder extends AbstractFilterBuilder<RateFilter.Builder>
        implements org.apache.logging.log4j.core.util.Builder<RateFilter> {

        /**
         * Builds the object after all configuration has been set. This will use default values for any unspecified
         * attributes for the object.
         *
         * @return the configured instance.
         * @throws ConfigurationException if there was an error building the object.
         */
        @Override
        public RateFilter build() {
            return new RateFilter();
        }
    }
}
