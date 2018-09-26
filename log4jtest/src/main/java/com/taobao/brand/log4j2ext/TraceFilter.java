package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.filter.BurstFilter;
import org.apache.logging.log4j.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jinshuan.li 2018/9/23 16:40
 */

@Plugin(name = "TraceFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class TraceFilter extends FilterAawre {

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

        return Result.NEUTRAL;
    }

    public static class Builder extends AbstractFilterBuilder<Builder>
        implements org.apache.logging.log4j.core.util.Builder<TraceFilter> {

        /**
         * Builds the object after all configuration has been set. This will use default values for any unspecified
         * attributes for the object.
         *
         * @return the configured instance.
         * @throws ConfigurationException if there was an error building the object.
         */
        @Override
        public TraceFilter build() {
            return new TraceFilter();
        }
    }

}
