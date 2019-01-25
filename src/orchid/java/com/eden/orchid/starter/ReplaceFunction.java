package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;

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
