app.directive('mcToggleActive', function() {
    return {
        link: function(scope, element, attrs) {
            element.find('li').on('click', function() {
                $(this).addClass('active').siblings().removeClass('active');
            });
        }
    }
});