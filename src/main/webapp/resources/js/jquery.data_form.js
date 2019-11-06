/* <![CDATA[ */
$.fn.initializeDataEntry = function(bgcolour, fgcolour)
{
	this.mouseover(
		function(e)
		{
      if(!this.highlightset) {
        $(this).addClass("mouseover");
        $(this).find(".commentIcon").show();
        this.highlightset = true;
      }
		}
	).mouseout(
		function()
		{
      if(this.highlightset) {
        $(this).removeClass("mouseover");
        $(this).find(".commentIcon").hide();
        this.highlightset = false;
      }
		}
	).focusout(
		function()
		{
      if(this.highlightset) {
        $(this).removeClass("mouseover");
        $(this).find(".commentIcon").hide();
        this.highlightset = false;
      }
		}
  );
	return this;
};
/* ]]> */
