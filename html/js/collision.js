// Simple Collision Detection: http://johannesgrandy.com/
// If you integrate support for rotated Objects please let me know ;) im not the best mathematician

$(function() {
    
    jQuery.fn.touch = function(options) {   
        
        return $(this).each(function() {
                
            target = $(this);
            
            var tAxis = target.offset();
            var t_x = [tAxis.left, tAxis.left + target.outerWidth()];
            var t_y = [tAxis.top, tAxis.top + target.outerHeight()];
            
            $(options.intersector).each(function() {
                var intersector = $(this);
                var intersectorPos = intersector.offset();
                var i_x = [intersectorPos.left, intersectorPos.left + intersector.outerWidth()]
                var i_y = [intersectorPos.top, intersectorPos.top + intersector.outerHeight()];
                
                if ( t_x[0] < i_x[1] && t_x[1] > i_x[0] &&
                    t_y[0] < i_y[1] && t_y[1] > i_y[0]) {
                    options.action(target, intersector);
                }else{
	                options.stop(target, intersector);
                }
            });
        
        });

    }

});