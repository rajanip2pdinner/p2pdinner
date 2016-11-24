var express = require('express');
var router = express.Router();
var unirest = require('unirest');
var http = require('http');
var https = require('https');
var qs = require('querystring');
var moment = require('moment');

/* GET profiles listing. */
router.get('/', function(req, res, next) {
	res.send('respond with a resource');
});

router.get('/current', function(req, res, next) {
	var request = unirest.get(res.locals.rest_endpoint + '/listing/view/current');
	request.headers({'Accept': 'application/json', "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]})
	.end(function(response) {
		res.render('listings', {'data' : response.body });
	});
});

router.get('/add/:id', function(req, res, next) {
	var menuItemId = req.params.id;
	res.render('addtolisting', {
		'menuItemId' : menuItemId
	});
});

router.post('/add', function(req, res, next) {
	var jsonReq = {};
	jsonReq.startTime = moment.utc(Date.parse(req.body.start_time)).format('MM/DD/YYYY HH:mm:ss');
	jsonReq.endTime = moment.utc(Date.parse(req.body.end_time)).format('MM/DD/YYYY HH:mm:ss');
	jsonReq.closeTime = moment.utc(Date.parse(req.body.close_time)).format('MM/DD/YYYY HH:mm:ss');
	jsonReq.availableQuantity = parseInt(req.body.available_quantity);
	jsonReq.costPerItem = parseFloat(req.body.cost_per_item);
	jsonReq.menuItemId = req.body.menu_item_id;
	console.dir(jsonReq);
	var Request = unirest.post(res.locals.rest_endpoint + '/listing')
	.headers({'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + req.app.locals.authenticationInfo["access_token"]})
	.send(jsonReq).end(function(response) {
		if (response.code !== undefined && response.code !== 200) {
			res.render('listings', {'status' : response.code, 'msg' : response.body });
		}
		console.log(response.body);
		res.redirect('/listings/current');
	});
});

router.get('/current/profile', function(req, res, next){
	var profile = req.session.profile;
	var Request = unirest.get(res.locals.rest_endpoint + '/listing/view/current/' + profile.id)
	.headers({'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + req.app.locals.authenticationInfo["access_token"]})
	.send()
	.end(function(response){
		console.log(response.body);
		res.render('listings', { 'data' : response.body });
	});
});

router.get('/search', function(req, res, next) {
	res.render('search');
});

router.get('/search/defaultlocation', function(req, res, next){
	var profile = req.session.profile;
	res.json({ 'lat': profile.latitude, 'lng' : profile.longitude })
});

router.get('/search/markers', function(req, res, next) {
	console.log(res.locals.rest_endpoint);
	console.log(req.session.profile);
	var profile = req.session.profile;
	var markers;
	var q = {};
	q.latitude = profile.latitude;
	q.longitude = profile.longitude;
	var queryParams = require('url').parse(req.url).query;
	if (queryParams !== undefined && queryParams !== null) {
		console.log(queryParams);
		q.q = queryParams.replace(/&/g,'|').replace(/=/g,':');
	}
	console.log(q);
	var Request = unirest.get(res.locals.rest_endpoint + '/places/nearbysearch')
		.headers({'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + req.app.locals.authenticationInfo["access_token"]})
		.query(q)
		.send()
		.end(function(response) {
			console.log(response.body);
			if (response.code !== undefined && response.code !== 200) {
				res.json({'status' : response.code, 'markers' : [] });
			} else {
				res.json({'status' : response.code, 'markers': response.body});
			}
		});
});

module.exports = router;
