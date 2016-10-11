var express = require('express');
var router = express.Router();
var http = require('http');
var https = require('https');
var qs = require('querystring');
var unirest = require('unirest');

/* GET profiles listing. */
router.get('/', function(req, res, next) {
	res.send('respond with a resource');
});

router.get('/menu', function(req, res, next) {
	var profile = req.session.profile;
	try {
		unirest.get(res.locals.rest_endpoint + '/menu/view/' + profile.id)
		.headers({'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + req.app.locals.authenticationInfo['access_token']})
		.send()
		.end(function(response){
			if (response.code !== 200) {
			    var e = {};
			    e.message = response.body;
			    e.status = response.code;
			    throw e;
			}
			res.render('menuitems', { 'data' : response.body });
		})
	} catch(e) {
		res.render('menuitems', e);
	}
});

router.get("/add", function(req, res, next){
	var categories;
	var special_needs;
	var delivery_type;
	try {
	   unirest.get(res.locals.rest_endpoint + '/dinnercategory/view')
		 .headers({
			 "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
		 })
		 .send()
		 .end(function(response){
				if (response.code !== 200) {
					throw new Error(response.body);
				}
				categories = response.body;
				unirest.get(res.locals.rest_endpoint + '/specialneed/view')
				.headers({
	 			 "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
	 		 })
				.send()
				.end(function(response){
					if (response.code !== 200) {
						throw new Error(response.body);
					}
					special_needs = response.body;
					unirest.get(res.locals.rest_endpoint + '/delivery/view')
					.headers({
		 			 "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
		 		 })
					.send()
					.end(function(response){
					if (response.code !== 200) {
						throw new Error(response.body);
					}
					delivery_types = response.body;
					res.render('addToMenu', { "categories" : categories, "special_needs" : special_needs, "delivery_types" : delivery_types} );
					});
				});
	   });
	} catch(e) {
	   res.render('addToMenu', { "status" : response.body.status, "message" : response.body.message });
	}

});

router.post("/add", function(req, res, next){
	var request = {};
	request.title = req.body.title;
	request.description = req.body.description;
	request.userId = req.session.profile.id;
	request.isActive = true;
	request.addressLine1 = req.body.address_line1;
	request.addressLine2 = req.body.address_line2;
	request.state = req.body.states;
	request.city = req.body.city;
	request.costPerItem = req.body.cost_per_item;
	request.availableQuantity = req.body.available_quantity;
	request.zipCode = req.body.zip_code;
	if (req.body.category !== undefined) {
		request.dinnerCategories = req.body.category;
	}
	if (req.body.special_need !== undefined) {
		request.dinnerSpecialNeeds  = req.body.special_need;
	}
	if (req.body.delivery_type !== undefined) {
		request.dinnerDelivery  = req.body.delivery_type;
	}
	console.log(request);
	console.log(req.files);
	unirest.post(res.locals.rest_endpoint + '/menu/add').headers('Content-Type', 'multipart/form-data').attach('filename', req.files.image.path).field('menuItem', request).send().end(function(response){
		if (response.code !== 200) {
			res.render('addToMenu', { "status" : response.body.status, "message" : response.body.message });
		}
		res.redirect('/menuitems/menu');
	});
});

module.exports = router;
