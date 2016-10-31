var express = require('express');
var router = express.Router();
var unirest = require('unirest');
var moment = require('moment');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('myorders', { title: 'My Orders' });
});

router.get('/iwantdinner', function(req, res, next){
  var profile = req.session.profile;
  var startDate;
  var endDate;
  if (req.query.start_date !== undefined) {
    startDate = moment(req.query.start_date,'DD, MMM YYYY');
  } else {
    startDate = moment();
  }
  if (req.query.end_date !== undefined) {
    endDate = moment(req.query.end_date,'DD, MMM YYYY');
  } else {
    endDate = moment(startDate).add(1, "d");
  }
  var dates = [];
  var now = moment();
  dates.push(moment().subtract(2, "d").format("DD, MMM YYYY"));
  dates.push(moment().subtract(1, "d").format("DD, MMM YYYY"));
  dates.push(moment().format("DD, MMM YYYY"));
  dates.push(moment().add(1, "d").format("DD, MMM YYYY"));
  dates.push(moment().add(2, "d").format("DD, MMM YYYY"));
  unirest.get(res.locals.rest_endpoint + '/cart/placedorders/' + profile.id)
    .headers({
      "Content-Type" : "application/json",
      "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
    })
    .query({
      "startDate" : startDate.utc().format("MM/DD/YYYY HH:mm:ss"),
      "endDate" : endDate.utc().format("MM/DD/YYYY HH:mm:ss")
    })
    .end(function(response){
      //console.log(JSON.parse(response.body));
      console.log(response.body);
      if (response.code !== undefined && response.code === 200) {
        res.render('iwantdinner', {
          "title": 'I Want Dinner',
          "data" : response.body,
          "dates" : dates,
          "currentdate" : startDate.format("DD, MMM YYYY")
        });
      }
    }); 
});

router.get('/ihavedinner',function(req, res, next){
  var profile = req.session.profile;
  var inputDate;
  if (req.query.input_date !== undefined) {
    inputDate = moment(req.query.input_date,'DD, MMM YYYY');
  } else {
    inputDate = moment();
  }
  var dates = [];
  var now = moment();
  dates.push(moment().subtract(2, "d").format("DD, MMM YYYY"));
  dates.push(moment().subtract(1, "d").format("DD, MMM YYYY"));
  dates.push(moment().format("DD, MMM YYYY"));
  dates.push(moment().add(1, "d").format("DD, MMM YYYY"));
  dates.push(moment().add(2, "d").format("DD, MMM YYYY"));
  unirest.get(res.locals.rest_endpoint + '/listing/view/' + profile.id)
    .headers({
      "Content-Type" : "application/json",
      "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
    })
    .query({
      "inputDate" : inputDate.utc().valueOf(),
    })
    .end(function(response){
      //console.log(JSON.parse(response.body));
      console.log(response.body);
      if (response.code !== undefined && response.code === 200) {
        res.render('ihavedinner', {
          "title": 'I Have Dinner',
          "data" : response.body,
          "dates" : dates,
          "currentdate" : inputDate.format("DD, MMM YYYY")
        });
      }
    }); 
});

router.get('/ihavedinner/:listingId/received/detail', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + "/cart/orders/" + req.params.listingId + "/received/detail")
    .headers({
        "Content-Type" : "application/json",
        "Authorization" : "Bearer " + req.app.locals.authenticationInfo["access_token"]
    }).end(function(response){
      if (response.code !== undefined && response.code === 200) {
        console.log(response.body);
        res.render('receivedOrdersDetail', {
          "title" : "Received Order Details",
          "data" : response.body
        })
      }
    });
});

module.exports = router;
