package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;

/**
 * This is an example Function, added as a custom component for this site only. A Function is can be used as part of an
 * Pebble expression when precompiling markup. TemplateFunctions can also accept their first parameter piped in as a
 * Pebble filter.
 *
 * To create a Function, you'll need to:
 *
 * 1) Make a subclass of `TemplateFunction`
 *   - `public class ReplaceFunction extends TemplateFunction`
 *   - `super("replace", false)`
 *   - return the order of parameters set on the tag. The first one is typically "input"
 *   - override `apply()` and return the function result
 * 2) Register that subclass in your custom Module
 *   - in `OrchidStarterModule`: `addToSet(TemplateFunction.class, ReplaceFunction.class)`
 */
public class ReplaceFunction extends TemplateFunction {

    @Option
    public String input;

    @Option
    public String find;

    @Option
    public String replace;

    @Inject
    public ReplaceFunction() {
        super("replace", false);
    }

    @Override
    public String[] parameters() {
        return new String[]{"input", "find", "replace"};
    }

    @Override
    public Object apply() {
        return (input != null)
                ? input.replaceAll(find, replace)
                : "";
    }
}
