/**
 * @author rajani@p2pdinner.com
 */
$(document).ready(function(){
	console.log("JQuery Initialized");
	$.ajax({
		url : '/options/dinnercategories',
		type : 'GET',
		success: function(data) {
			console.log(data);
			var elm = $('#categories');
			elm.append('<option></option>');
			_.each(data, function(element, index, list){
				elm.append('<option>' + element.name + '</option>');
			});
		},
		error : function(error) {
			console.log(error);
		}
	});

	$.ajax({
		url : '/options/specialneeds',
		type : 'GET',
		success: function(data) {
			console.log(data);
			var elm = $('#special_needs');
			elm.append('<option></option>');
			_.each(data, function(element, index, list){
				elm.append('<option>' + element.name + '</option>');
			});
		},
		error : function(error) {
			console.log(error);
		}
	});


	$.ajax({
		url : '/options/delivery',
		type : 'GET',
		success: function(data) {
			console.log(data);
			var elm = $('#delivery');
			elm.append('<option></option>');
			_.each(data, function(element, index, list){
				elm.append('<option>' + element.name + '</option>');
			});
		},
		error : function(error) {
			console.log(error);
		}
	});

	$('#frm_search').submit(function(){
		var title = $('#title').val();
		var description = $('#description').val();
		var categories = $('#categories').val();
		var max_price = $('#max_price').val();
		var min_price = $('#min_price').val();
		var special_needs = $('#special_needs').val();
		var delivery = $('#delivery').val();
		$.ajax({
			url : '/listings/search/markers',
			type : 'GET',
			data : {
				"title" : title,
				"description" : description,
				"categories" : categories,
				"max_price" : max_price,
				"min_price" : min_price,
				"categories" : categories,
				"special_needs" : special_needs,
				"delivery" : delivery
			},
			success : function(data){
				console.dir(data.markers);
				$.each(data.markers.results, function(idx, result) {
					console.dir(result);
					_.each(markers, function(elem) {
						elem.setMap(null);
					});
					markers = [];
					var marker = new google.maps.Marker({
						position : new google.maps.LatLng(result.location.lat,result.location.lng),
						map : map,
						animation: google.maps.Animation.DROP
					});
					markers.push(marker);
					var contents = [];
					$.each(result.menu_items, function(idx, mi){
						contents.push(mi.title);
					});
					var infowindow = new google.maps.InfoWindow({
						//content: '<b>' + contents.join() + '</b>',
						content : markerContentTemplate({"menu_items" : result.menu_items}),
						size: new google.maps.Size(50,50)
					});
					google.maps.event.addListener(marker, 'click', function(){
						infowindow.open(map, marker);
					});
				});
			},
			error : function(error) {
				console.log(error);
			}
		});
		return false;
	});

	var markerContentTemplate = _.template('<div id="lst" class="panel panel-default">'
					+ '<div class="panel-heading">'
					+ '  <div class="panel-content">'
					+ '    <div class="table-responsive">'
					+ '      <table class="table table-hover">'
					+ '      	<thead>'
					+ '      	 	<tr class="active"><th>Title</th><th>Description</th><th>Cost Per Item</th><th>Quantity Available</th></tr>'
					+ '      	</thead>'
					+ '		<%_.forEach(menu_items, function(mi) { %>'
					+ '		<tr>'
					+ '			<td><%=mi.title%></td> '
					+ '			<td><%=mi.description%></td> '
					+ '			<td><%=mi.listing.cost_per_item%></td> '
					+ '			<td><%=mi.listing.quantity_available%></td> '
					+ '			<td>'
					+ '			<div class="col-sm-10">'
					+ '				<input type="text" class="form-control" id="quantity" placeholder="Quantity" >'
					+ '			</div>'
					+ '			</td>'
					+ '		</tr>'
					+ '		<%})%>'
					+ '      </table>'
					+ '	 <button type="button" class="btn btn-success pull-right">Add To Cart</button>'
					+ '    </div>'
					+ '  </div><!-- /.modal-content -->'
					+ '</div><!-- /.modal -->');

	var mapCanvas = document.getElementById('map-canvas');
	var mapOptions = {
          center: new google.maps.LatLng(44.5403, -78.5463),
          zoom: 10,
          mapTypeId: google.maps.MapTypeId.TERRAIN
        };
	var map;
	var markers = [];
	$.ajax({
		url : '/listings/search/defaultlocation',
		type : 'GET',
		success : function(data){
			mapOptions.center = new google.maps.LatLng(data.lat, data.lng);
			map = new google.maps.Map(mapCanvas, mapOptions);
		},
		error : function(error) {
			console.log(error);
		}
	});
	$.ajax({
		url : '/listings/search/markers',
		type : 'GET',
		success : function(data){
			var items = new Map();
			console.dir(data.markers);
			_.each(data.markers.results, function(result, index, list){
				var profileId = result.profile_id;
				_.each(result.menu_items, function(mi, idx, mis){
					var key = [profileId, mi.location.lat, mi.location.lng].join("_");
					if (items.has(key)) {
							var item = items.get(key);
							item.menu_items.push(mi);
					} else {
						var item = {};
						item.location = mi.location;
						item.menu_items = [];
						item.menu_items.push(mi);
						items.set(key,item);
					}
				});
			});
			console.log("Writing items ....");
			console.dir(items);
			items.forEach(function(value, key) {
				var marker = new google.maps.Marker({
					position : new google.maps.LatLng(value.location.lat,value.location.lng),
					map : map,
					animation: google.maps.Animation.DROP
				});
				markers.push(markers);
				var contents = [];
				$.each(value.menu_items, function(idx, mi){
					contents.push(mi.title);
				});
				var infowindow = new google.maps.InfoWindow({
					//content: '<b>' + contents.join() + '</b>',
					content : markerContentTemplate({"menu_items" : value.menu_items}),
					size: new google.maps.Size(50,50)
				});
				google.maps.event.addListener(marker, 'click', function(){
					infowindow.open(map, marker);
				});
			});
		},
		error : function(error) {
			console.log(error);
		}
	});

});
