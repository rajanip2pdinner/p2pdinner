/**
 * @author rajani@p2pdinner.com
 */
$(document).ready(function(){
	console.log("JQuery Initialized");
	$('#register_btn').click(function(event){
		$('#registration_form').attr('action', '/profiles/register').attr('method', 'POST').submit();
	});
	$.ajax({
        url : '/options/states',
        type : 'GET',
        success: function(data) {
			console.log(data);
			var elm = $('#states');
			_.each(data, function(element, index, list){
			    elm.append('<option value=' + element.abbreviation + '>' + element.state + '</option>');
			});
                },
		error : function(error) {
			console.log(error);
		}
	});
});