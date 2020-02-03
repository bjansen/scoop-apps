package com.eden.orchid.starter

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage

/**
 * This is an example MenuFactory, added as a custom menu for this site only. A MenuFactory is responsible for returning
 * a list of `MenuItem`s which get added to a theme's menu areas.
 *
 * To create a MenuFactory, you'll need to:
 *
 * 1) Make a subclass of `OrchidMenuFactory`
 *   - `public class ReplaceTag extends TemplateTag`
 *   - `super("groupPages")`
 * 2) Register that subclass in your custom Module
 *   - in `OrchidStarterModule`: `addToSet(OrchidMenuFactory.class, GroupPagesMenuItem.class)`
 * 3) Implement the menu factory
 *   - Get a list of `OrchidPage`s from the context
 *   - Use `MenuItem.Builder` to convert those pages into `MenuItems`. They can be nested arbitrarily deep, and it's up
 *      to the theme to decide how deeply-nested themes are actually displayed.
 */
@Description("Adds all pages from a generator to the menu.", name = "Generator Pages")
class GroupPagesMenuItem : OrchidMenuFactory("groupPages") {

    @Option
    @Description("The collectionId to find pages from")
    lateinit var collectionId: String

    @Option
    @Description("The collectionType to find pages from")
    lateinit var collectionType: String

    @Option
    @Description("The tags to group pages by.")
    lateinit var groups: List<String>

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        // get a list of pages from the context, typically by collection
        val pageList = context
            .findAll(collectionType, collectionId, null)
            .filterIsInstance<OrchidPage>()

        return groups
            .map { group ->
                // make a parent item for each group
                MenuItem.Builder(context)
                    .title(group.capitalize())
                    // add child menu items to the parent item
                    .children(
                        pageList
                            // filter the pages in the collection by those with the group property set to `true` in its FrontMatter
                            .filter { it[group]?.toString()?.toBoolean() == true }

                            // map each page to a MenuItem instance
                            .map {
                                MenuItem.Builder(context)
                                    .page(it)
                                    .build()
                            }
                    )
                    .build()
            }
    }
}
