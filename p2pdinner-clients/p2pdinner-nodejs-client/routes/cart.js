var express = require('express');
var router = express.Router();
var unirest = require('unirest');

/* GET home page. */
router.get('/add/:id', function(req, res, next) {
  var listingId = req.params.id;
  res.render('addToCart', { "listingId" : listingId} );
});

router.post('/add', function(req, res, next){
  var quantity = parseFloat(req.body.quantity);
  var listingId = parseInt(req.body.listingId);
  var request = {};
  request.quantity = quantity;
  request.listing_id = listingId;
  request.profile_id = req.session.profile.id;
  
  if ( req.session.cart !== undefined) {
    console.log(req.session.cart);
    request.cart_id = req.session.cart.response.cartId;
  } else {
    console.log(req.session);
    console.log("Cart undefined. Creating for first time");
  }
  unirest.post(res.locals.rest_endpoint + '/cart/add').headers('Content-Type', 'application/json').send(request).end(function(response){
    console.log(response.body);
    if (response.body.code !== undefined ) {
      res.render('addToCart', { "code" : response.body.code, "message" : response.body.message});
    } else {
      req.session.cart_id = response.body.response.cartId;
      console.log("After adding item to cart" + req.session);
      res.redirect("/listings/current");  
    }
  });
});


router.get('/view', function(req, res, next) {
  unirest.get(res.locals.rest_endpoint + '/cart/items/profile/' + req.session.profile.id).send().end(function(response){
    console.log(response);
    if (response.code !== 200) {
      var error = {};
      error.status = response.code;
      res.render('error', { "message" : response.body.message, "error" : error } );
    }
    console.log(response.body);
    req.session.cart_id = response.body.cartId;
    res.render('cartitems', { 'data' : response.body });
  });
});

router.get("/placeorder", function(req, res, next) {
  console.log(req.session);
  unirest.post(res.locals.rest_endpoint + '/cart/placeorder/' + req.session.profile.id + '/' + req.session.cart_id)
    .headers('Content-Type', 'application/json')
    .send()
    .end(function(response){
      if (response.code !== 200) {
        res.render('addToCart', { "status" : resonse.body.status, "message" : response.body.message});
      } else {
        res.redirect('/listings/current');
      }
    });
});

router.get("/orders", function(req, res, next) {
    unirest.get(res.locals.rest_endpoint + '/cart/received/' + req.session.profile.id)
    .headers('Content-Type', 'application/json')
    .send()
    .end(function(response) {
        console.log(response.body.response.results);
        res.render('orders', { "data" : response.body.response.results });
    })
});

module.exports = router;
