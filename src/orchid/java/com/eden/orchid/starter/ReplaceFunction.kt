package com.eden.orchid.starter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

/**
 * This is an example Function, added as a custom component for this site only. A Function is can be used as part of an
 * Pebble expression when precompiling markup. TemplateFunctions can also accept their first parameter piped in as a
 * Pebble filter.
 *
 * To create a Function, you'll need to:
 *
 * 1) Make a subclass of `TemplateFunction`
 * - `public class ReplaceFunction extends TemplateFunction`
 * - `super("replace", false)`
 * - return the order of parameters set on the tag. The first one is typically "input"
 * - override `apply()` and return the function result
 * 2) Register that subclass in your custom Module
 * - in `OrchidStarterModule`: `addToSet(TemplateFunction.class, ReplaceFunction.class)`
 */
class ReplaceFunction : TemplateFunction("replace", false) {

    @Option
    @Description("The input content")
    var input: String = ""

    @Option
    @Description("A String to find within the input content")
    var find: String = ""

    @Option
    @Description("The replacement String")
    var replace: String = ""

    override fun parameters(): Array<String> = arrayOf("input", "find", "replace")

    override fun apply(context: OrchidContext?, page: OrchidPage?): String = when {
        input.isBlank() -> ""
        else -> input.replace(find.toRegex(), replace)
    }
}
