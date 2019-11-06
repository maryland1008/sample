/* <![CDATA[ */
jQuery(function($){
$('.nav_menu li').mouseover(
		function(e)
		{
      $(this).find('ul').show();
    }
	).mouseout(
		function()
		{
      $(this).find('ul').hide();
		}
	);
});
/* ]]> */
