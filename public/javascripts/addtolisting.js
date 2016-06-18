$(document).ready(function(){
    $.ajax({
        url : '/options/states',
        type : 'GET',
        success: function(data) {
			console.log(data);
			var elm = $('#states');
			_.each(data, function(element, index, list){
			    elm.append('<option value=' + element.abbreviation + '>' + element.state + '</option>');
			});
			$.ajax({
			    url : '/profiles/current/profile',
			    type : 'GET',
			    success: function(data) {
					console.log(data);
					$('#address_line1').val(data.addressLine1);
					$('#address_line2').val(data.addressLine2);
					$('#city').val(data.city);
					$('#states').find('option[value="' +  data.state +'"]').attr("selected",true);
				    },
				    error : function(error) {
					    console.log(error);
				    }
			});
                },
		error : function(error) {
			console.log(error);
		}
    });
});