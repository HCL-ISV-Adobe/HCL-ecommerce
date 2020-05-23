"use strict";
use(function() {
    var resourceResolver = resource.getResourceResolver();
    return {
      sizeSelect : resourceResolver.getResource(currentNode.getPath() + "/productSize"),
    };
});

