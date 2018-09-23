package com.eden.orchid.starter;

import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.registration.OrchidModule;

public class OrchidStarterModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(TemplateTag.class,
                ReplaceTag.class);

        addToSet(TemplateFunction.class,
                ReplaceFunction.class);
    }

}
