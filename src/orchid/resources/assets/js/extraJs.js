var client = algoliasearch('SMWQFSV0XP', '7bf5a65a6668db774f2172b5edf6cc67');
var index = client.initIndex('prod_ScoopApps');

function newHitsSource(index, params) {
  return function doSearch(query, cb) {
    index
      .search(query, params)
      .then(function(res) {
        cb(res.hits, res);
      })
      .catch(function(err) {
        console.error(err);
        cb([]);
      });
  };
}

autocomplete('#query', { debug: true, hint: false }, [
  {
    source: newHitsSource(index, { hitsPerPage: 5 }),
    displayKey: 'name',
    templates: {
      suggestion: function(suggestion) {
        return '<div><a href="' + window.site.baseUrl + '/' + suggestion.bucket + '/' + suggestion.name + '">' +
          '<div class="app-name">' + suggestion._highlightResult.name.value + '</div>' +
          '<div class="app-description">' + suggestion._highlightResult.description.value + '</div>' +
          '</a></div>';
      }
    }
  }
]).on('autocomplete:selected', function(event, suggestion, dataset, context) {
  // Do nothing on click, as the browser will already do it
  if (context.selectionMethod === 'click') {
    return;
  }
  // Change the page, for example, on other events
  window.location.assign(window.site.baseUrl + '/' + suggestion.bucket + '/' + suggestion.name);
});
