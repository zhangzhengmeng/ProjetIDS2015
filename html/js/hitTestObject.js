$.fn.hitTestObject = function(obj){
 
    var bounds = this.offset();
    bounds.right = bounds.left + this.outerWidth();
    bounds.bottom = bounds.top + this.outerHeight();
     
    var compare = obj.offset();
    compare.right = compare.left + obj.outerWidth();
    compare.bottom = compare.top + obj.outerHeight();
             
    return (!(compare.right < bounds.left ||
        compare.left > bounds.right ||
        compare.bottom < bounds.top ||
        compare.top > bounds.bottom));
     
}