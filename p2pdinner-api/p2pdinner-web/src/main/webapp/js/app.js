var p2pdinnerApp = angular.module('p2pdinnerApp', ['ngTable', 'ngRoute', 'ui.router', 'uiGmapgoogle-maps','p2pdinnerControllers', 'p2pdinnerServices']);


/*p2pdinnerApp.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when("/key-value-services-client", {
			templateUrl : 'partials/keyvalue-services.html',
			controller: 'KeyValueCtrl'
		}).
		when("/dinner-category-services-client", {
			templateUrl : 'partials/dinnercategory-services.html',
			controller: 'DinnerCategoryCtrl'
		}).
		when("/dinner-delivery-services-client", {
			templateUrl : 'partials/dinnerdelivery-services.html',
			controller: 'DinnerDeliveryCtrl'
		}).
		when("/dinner-specialneed-services-client", {
			templateUrl : 'partials/dinnerspecialneed-services.html',
			controller: 'DinnerSpecialNeedCtrl'
		}).
		when("/dinner-listing-services-client", {
			templateUrl : 'partials/dinner-listing-services.html',
			controller: 'DinnerListingCtrl'
		}).
		when("/signup", {
			templateUrl : 'partials/user-signup.html',
			controller : 'UserProfileCtrl'
		}).
		when("/menu", {
			templateUrl : 'partials/menu-items.html',
			controller : 'MenuItemCtrl'
		});
}]);*/

p2pdinnerApp.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/signin');
	
	$stateProvider
		.state('signin', {
			url : '/signin',
			templateUrl : '/partials/user-signin.html',
			controller : 'UserProfileCtrl'
	})
		.state('register', {
			url : '/register',
			templateUrl : '/partials/user-signup.html',
			controller : 'UserProfileCtrl'
	}) 
		.state('menu', {
			url : '/menu/:profileId',
			templateUrl : '/partials/menu-items.html',
			controller : 'MenuItemCtrl'
	})
		.state('signout', {
			url : '/signin',
			templateUrl : '/partials/user-signin.html',
			controller : 'UserProfileCtrl'
	})
		.state('search', {
			url : '/search',
			templateUrl : '/partials/search-dinner.html',
			controller : 'DinnerSearchCtrl'
	});
});

p2pdinnerApp.config(function(uiGmapGoogleMapApiProvider){
	uiGmapGoogleMapApiProvider.configure({
		key : 'AIzaSyBGg6t140kiCfZRtlfoaC1S7_miTAkwrqA',
		v : '3.17',
		libraries: 'weather, geometry, visualization'
	});
})



