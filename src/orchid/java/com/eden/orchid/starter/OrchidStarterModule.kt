package com.eden.orchid.starter

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent


@Suppress("unused")
class OrchidStarterModule : OrchidModule() {

    override fun configure() {
        addToSet(TemplateTag::class.java, ReplaceTag::class.java)

        addToSet(TemplateFunction::class.java, ReplaceFunction::class.java)

        addToSet(OrchidComponent::class.java, ReplaceComponent::class.java)
    }

}
