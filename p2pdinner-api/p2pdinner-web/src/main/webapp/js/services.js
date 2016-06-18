var p2pdinnerServices = angular.module('p2pdinnerServices', []);

p2pdinnerServices.factory('userService', ['$http', '$window', '$q',
function($http, $window, $q) {
	var services = {
		createProfile : function(profile) {
			var deferred = $q.defer();
			
			$http.post('/services/profile', {
				"emailAddress" : profile.emailAddress,
				"addressLine1" : profile.addressLine1,
				"addressLine2" : profile.addressLine2,
				"city" : profile.city,
				"state" : profile.state,
				"zip" : profile.zip,
				"password" : profile.password
			}).success(function(d) {
				deferred.resolve(d);
				//$window.location.href = '#/signin';
			}).error(function(d) {
				deferred.reject(d);
			});
			return deferred.promise;
		}
	};
	return services;
}]);

p2pdinnerServices.factory('menuService', ['$http', '$q',
	function($http, $q) {
	var services = {
		createMenuItem : function(menuItem) {
			var deferred = $q.defer();
			$http.post('/services/menu/add', {
				'title' : menuItem.title,
				'description' : menuItem.description,
				'userId' : menuItem.userId,
				'dinnerCategories' : menuItem.dinner_category,
				'dinnerSpecialNeeds' : menuItem.dinner_special_needs === undefined ? '' : menuItem.dinner_special_needs,
				'dinnerDelivery' : menuItem.dinner_delivery === undefined ? '' : menuItem.dinner_delivery
			}).success(function(data){
				deferred.resolve(data);
			}).error(function(reason){
				deferred.reject(reason);
			});
			return deferred.promise;
		},
		menuByProfileId : function(profileId) {
			$http.get('/sevices/menu/view/' + profileId).success(function(data){
				results = data;
			});
		}
	};
	return services;
}]);

p2pdinnerServices.factory('dinnerCartService', ['$http', '$q', function($http, $q) {
	var services = {
		addToCart : function(cart_item) {
			var deferred = $q.defer();
			$http.post('/services/cart/add', cart_item).success(function(data){
				deferred.resolve(data);
			}).error(function(reason){
				deferred.reject(reason);
			});
			return deferred.promise;
		},
		countCartItems : function(cartId) {
			var deferred = $q.defer();
			$http.get('/services/cart/items/' + cartId + '/count').success(function(data) {
				deferred.resolve(data);
			}).error(function(reason) {
				deferred.reject(reason);
			});
			return deferred.promise;
		},
		getCartItems : function(cartId) {
			var deferred = $q.defer();
			$http.get('/services/cart/items/' + cartId).success(function(data) {
				deferred.resolve(data);
			}).error(function(reason) {
				deferred.reject(reason);
			});
			return deferred.promise;	
		},
		placeOrder : function(order) {
			var deferred = $q.defer();
			$http.post('/services/cart/placeorder/' + order.profile_id + '/' + order.cart_id, order).success(function(data){
				deferred.resolve(data);
			}).error(function(reason){
				deferred.reject(reason);
			});
			return deferred.promise;
		}
	};
	return services;
}]);
