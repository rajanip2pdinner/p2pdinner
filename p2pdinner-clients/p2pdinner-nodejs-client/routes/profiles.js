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

router.get('/signout', function(req, res, next) {
	req.session.profile = undefined;
	res.redirect('/');
});

router.get('/register', function(req, res, next) {
	res.render("register");
});

router.post('/register', function(req, res, next) {
	requestBody = {};
	requestBody.emailAddress = req.body.email_address;
	requestBody.password = req.body.password;
	requestBody.addressLine1 = req.body.address_line1;
	requestBody.addressLine2 = req.body.address_line2;
	requestBody.city = req.body.city;
	requestBody.state = req.body.state;
	requestBody.zip = req.body.zip_code;
	requestBody.devices = [];
	requestBody.devices.push({
		"deviceType" : req.body.device_type,
		"registrationId" : req.body.registion_uuid,
		"notifiationsEnabled"  :  req.body.enable_notifications === 'on' ? true : false
	});
	console.log(requestBody);
	var Request = unirest.post(res.locals.rest_endpoint + "/profile")
		.headers({
			"Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"],
			"Content-Type" : "application/json"
		})
		.send(requestBody)
		.end(function(response){
				console.dir(response.body);
				res.render("register", { 'status' : response.code, 'msg' : response.body.status + ' : ' + response.body.message });
		});
});

router.post('/validate', function(req, res, next) {
	if (req.session.profile !== undefined) {
		res.render('mainmenu');
	}
	var emailAddress = req.body.emailAddress;
	var password = req.body.password;
	var Request = unirest.get(res.locals.rest_endpoint + "/profile/validate")
		.headers({"Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]})
		.query({ 'emailAddress' : emailAddress, 'password': password})
		.end(function(response) {
			if (response.body === undefined) {
				var err = {};
				err.status = response.code;
				err.msg = 'Application server may be down. Please try again';
				err.stack = '';
				res.render('error', { 'error' : err });
			} else {
				console.dir(response.body)
				if (response.body.code !== undefined) {
					res.render('index', { 'status' : response.body.code, 'msg' : response.body.message + ' : ' + response.body.message });
				} else {
					req.session.profile = response.body;
					res.render('mainmenu');
				}
			}

		});
});

router.get('/validate', function(req, res, next){
	res.render('mainmenu');
});

router.get('/current/profile', function(req, res, next) {
	res.json(req.session.profile);
});

module.exports = router;
