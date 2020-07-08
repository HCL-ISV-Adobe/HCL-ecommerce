
// (function($) {
//     "use strict";

//     var BUTTON_NAME = ".cmp-form-button__editor-name";
//     var BUTTON_VALUE = ".cmp-form-button__editor-value";
//     var PROP_ERROR_MESSAGE = "error-message";

//     $.validator.register({
//         selector: BUTTON_NAME,
//         validate: function(el) {
//             var valueInput = el.closest("form").find(BUTTON_VALUE);
//             if (valueInput.val() !== "") {
//                 if (el.val() === "") {
//                     return el.data(PROP_ERROR_MESSAGE);
//                 }
//             }
//         }
//     });

// })(jQuery);
