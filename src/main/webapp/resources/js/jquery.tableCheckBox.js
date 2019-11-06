/* <![CDATA[ */
$.fn.initializeDataEntry = function(bgcolour, fgcolour)
{
	this.mouseover(
		function(e)
		{
      if(!this.highlightset) {
        $(this).addClass("mouseover");
        this.highlightset = true;
      }
		}
	).mouseout(
		function()
		{
      if(this.highlightset) {
        $(this).removeClass("mouseover");
        this.highlightset = false;
      }
		}
	).focusout(
		function()
		{
      if($("#hideFinished").is(":checked")) {
        if(!$(this).hasClass("required")) {
          if(this.highlightset) {
            $(this).removeClass("mouseover");
            this.highlightset = false;
          }
          $(this).addClass("hide");
        }
        else {
          if($(this).find(":checked").length>0 || $(this).find(":selected").length>0 
          || ($(this).find(":text").length>0 && $(this).find(":text").val().length>0 || ($("textarea:first", this).length>0 && $("textarea:first", this).val().length>0) )) {
            if(this.highlightset) {
              $(this).removeClass("mouseover");
              this.highlightset = false;
            }
            $(this).addClass("hide");
          }
        }
      }
		}
  );
	return this;
};
/* ]]> */
