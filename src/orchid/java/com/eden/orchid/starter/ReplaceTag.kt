package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;

/**
 * This is an example Tag, added as a custom component for this site only. A Tag is a a section of content that is
 * dynamically replaced when markup is precompiled.
 *
 * To create a Tag, you'll need to:
 *
 * 1) Make a subclass of `TemplateTag`
 *   - `public class ReplaceTag extends TemplateTag`
 *   - `super("replace", Type.Content, true)`
 *   - return the order of parameters set on the tag
 * 2) Register that subclass in your custom Module
 *   - in `OrchidStarterModule`: `addToSet(TemplateTag.class, ReplaceTag.class)`
 * 3) Create a matching template in `templates/tags` to be rendered, whose filename matches the name passed to the superclass constructor
 *   - `templates/tags/replace.peb`
 */
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
