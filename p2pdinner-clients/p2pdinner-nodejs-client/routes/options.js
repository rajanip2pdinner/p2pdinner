var express = require('express');
var unirest = require('unirest');
var http = require('http');
var https = require('https');
var qs = require('querystring');

var router = express.Router();

router.get('/dinnercategories', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + "/dinnercategory/view")
  .send()
  .headers({
    "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
  })
  .end(function(response){
		if (response.code !== 200) {
			throw new Error(response.body);
		}
		res.json(response.body);
	});
});

 router.get('/specialneeds', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + "/specialneed/view")
  .headers({
    "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
  })
  .send()
  .end(function(response){
		if (response.code !== 200) {
			throw new Error(response.body);
		}
		res.json(response.body);
	});
});

 router.get('/delivery', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + "/delivery/view").headers({
    "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
  })
  .send()
  .end(function(response){
		if (response.code !== 200) {
			throw new Error(response.body);
		}
		res.json(response.body);
	});
});
  router.get('/states', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + "/profile/states")
  .headers({
    "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
  })
  .send()
  .end(function(response){
		if (response.code !== 200) {
			throw new Error(response.body);
		}
		res.json(response.body);
	});
});


module.exports = router;
