var express = require('express');
var router = express.Router();
var unirest = require('unirest');
var moment = require('moment');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('myorders', { title: 'My Orders' });
});

router.get('/ihavedinner', function(req, res, next){
  var profile = req.session.profile;
  var startDate;
  var endDate;
  if (req.params.start_date !== undefined) {
    startDate = moment(start_date,'MM/DD/YYYY HH:mm:ss');
  } else {
    startDate = moment();
  }
  if (req.params.end_date !== undefined) {
    endDate = moment(start_date,'MM/DD/YYYY HH:mm:ss');
  } else {
    endDate = moment().add(1, 'd');
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
        res.render('ihavedinner', {
          "title": 'I Want Dinner',
          "data" : response.body.results,
          "dates" : dates,
          "currentdate" : moment().format("DD, MMM YYYY")
        });
      }
    }); 
});

router.get('/iwantdinner',function(req, res, next){
  res.render('iwantdinner', {title : 'I Want Dinner'});
});

module.exports = router;
