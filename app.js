var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var session = require('express-session');
var multer = require('multer');
var fs = require('fs');
var unirest = require('unirest');
var app = express();
var router = express.Router();

var routes = require('./routes/index');
var users = require('./routes/users');
var profiles = require('./routes/profiles');
var menuitems = require('./routes/menuitems');
var listings = require('./routes/listings');
var cart = require('./routes/cart');
var options = require('./routes/options');

app.locals.rest_endpoint = process.env.P2PDINNER_ENDPOINT_URL || "http://localhost:9128/services";

var config = fs.readFileSync("./config.json");
var authenticationConfiguration = JSON.parse(config);

app.locals.configuration = authenticationConfiguration['development'];

console.log(app.locals.configuration);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(session({'secret' : '123456', saveUninitialized : true, resave : false}));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use(function(req, res, next){
    res.locals.rest_endpoint = app.locals.rest_endpoint;
    next();
});

app.use(multer({ dest : './uploads/',
         rename: function (fieldname, filename) {
                return filename.replace(/\W+/g, '-').toLowerCase() + Date.now()
            }
}));
app.all("*", function(req, res, next) {
  if (app.locals.authenticationInfo === undefined) {
      unirest.post(app.locals.rest_endpoint + "/oauth/token")
      .headers({
        "Authorization" : "Basic " + app.locals.configuration.base64Encode
      })
      .send({
        "grant_type" : "password",
        "username" : app.locals.configuration.username,
        "password" : app.locals.configuration.password
      })
      .end(function(response) {
        authenticationInfo = response.body;
        app.locals.authenticationInfo = response.body;
      })
  }
  next();
});
app.use('/', routes);
app.use('/users', users);
app.use('/profiles', profiles);
app.use('/menuitems', menuitems);
app.use('/listings', listings);
app.use('/cart', cart);
app.use('/options', options);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});



// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


var port = process.env.PORT || 5000;
app.listen(port, function() {
  console.log("Listening on " + port);
});

module.exports = app;
