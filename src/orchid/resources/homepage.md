---
layout: index
components:
  - type: pageContent
  - type: recentPosts
    limit: 4
    category: ':any'
    noWrapper: true
    templates:
      - 'includes/postPreview_large'
next: 'category1'
docsBaseUrl: 'https://orchid.netlify.com/OrchidCore'
---

Thanks for trying out Orchid! Feel free to take a look around, then head over to the 
[User Manual]({{docsBaseUrl}}/wiki/userManual) to learn more about using Orchid to make your site
truly unique. While you're there, check out all our many great [plugins]({{docsBaseUrl}}/group/plugins) and browse our 
beautiful [themes]({{docsBaseUrl}}/group/themes) to see if there's one that catches your eye.

And finally, if you like Orchid, please support it by starring it on [Github](https://github.com/JavaEden/Orchid) and 
sharing a link on Twitter: <a 
    href="https://twitter.com/share?ref_src=twsrc%5Etfw" 
    class="twitter-share-button" 
    data-text="Check out the site I just made with Orchid, and new Static Site Generator for Java and Kotlin!" 
    data-url="{{site.baseUrl}}" data-show-count="false">Tweet</a>
<script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
