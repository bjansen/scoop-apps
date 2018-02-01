package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class ReplaceTag extends TemplateTag {

    @Option
    @Getter @Setter
    private String find;

    @Option
    @Getter @Setter
    private String replace;

    @Inject
    public ReplaceTag() {
        super("replace", true);
    }

    @Override
    public String[] parameters() {
        return new String[] {"find", "replace"};
    }
}
