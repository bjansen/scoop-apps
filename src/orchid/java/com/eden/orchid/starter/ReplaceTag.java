package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;

public class ReplaceTag extends TemplateTag {

    @Option
    public String find;

    @Option
    public String replace;

    @Inject
    public ReplaceTag() {
        super("replace", Type.Content, true);
    }

    @Override
    public String[] parameters() {
        return new String[]{"find", "replace"};
    }

}
