package com.eden.orchid.starter;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

@Description(
        name="Replace Component",
        value = "Load a resource by name, and replace usages of `find` with `replace` throughout its contents."
)
public class ReplaceComponent extends OrchidComponent {

    @Option
    @Description("The path to a site resource to load")
    public String resource;

    @Option
    @Description("A String to find within the resource content")
    public String find;

    @Option
    @Description("The replacement String")
    public String replace;

    @Inject
    public ReplaceComponent(OrchidContext context) {
        super(context, "replace", 100);
    }

    public String getContent() {
        return context.getLocalResourceEntry(resource).compileContent(null);
    }

}
