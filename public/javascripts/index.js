/**
 * @author rajani@p2pdinner.com
 */
$(document).ready(function(){
	console.log("JQuery Initialized");
	$('#register_btn').click(function(event){
		window.location.assign('/profiles/register');
	});
});