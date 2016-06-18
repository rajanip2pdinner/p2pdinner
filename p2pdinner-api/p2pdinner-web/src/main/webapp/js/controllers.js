var p2pdinnerControllers = angular.module('p2pdinnerControllers', []);

p2pdinnerControllers.controller('KeyValueCtrl', ['$scope', '$http', '$routeParams',
function($scope, $http, $routeParams) {

	$scope.init = function() {
		$http.get('/api/v1/keyvalue/view').success(function(data) {
			$scope.keyValuePairs = data;
		});
	};

	$scope.addKeyValue = function() {
		$http.post('/api/v1/keyvalue/add', {
			"key" : $scope.key,
			"value" : $scope.value
		}).success(function() {
			$http.get('/api/v1/keyvalue/view').success(function(data) {
				$scope.keyValuePairs = data;
			});
		});
	};
}]);

p2pdinnerControllers.controller('DinnerCategoryCtrl', ['$scope', '$http', '$routeParams',
function($scope, $http, $routeParams) {

	$scope.init = function() {
		$http.get('/api/v1/dinnercategory/view').success(function(data) {
			$scope.categories = data;
		});
	};

	$scope.addCategory = function() {
		$http.post('/api/v1/dinnercategory/add', {
			"name" : $scope.name
		}).success(function() {
			$http.get('/api/v1/dinnercategory/view').success(function(data) {
				$scope.categories = data;
			});
		});
	};
}]);

p2pdinnerControllers.controller('DinnerDeliveryCtrl', ['$scope', '$http', '$routeParams',
function($scope, $http, $routeParams) {

	$scope.init = function() {
		$http.get('/api/v1/delivery/view').success(function(data) {
			$scope.deliveries = data;
		});
	};

	$scope.addDeliveryMethod = function() {
		$http.post('/api/v1/delivery/add', {
			"name" : $scope.name
		}).success(function() {
			$http.get('/api/v1/delivery/view').success(function(data) {
				$scope.deliveries = data;
			});
		});
	};
}]);

p2pdinnerControllers.controller('DinnerSpecialNeedCtrl', ['$scope', '$http', '$routeParams',
function($scope, $http, $routeParams) {

	$scope.init = function() {
		$http.get('/api/v1/specialneed/view').success(function(data) {
			$scope.deliveries = data;
		});
	};

	$scope.addDeliveryMethod = function() {
		$http.post('/api/v1/specialneed/add', {
			"name" : $scope.name
		}).success(function() {
			$http.get('/api/v1/specialneed/view').success(function(data) {
				$scope.deliveries = data;
			});
		});
	};
}]);

p2pdinnerControllers.controller('DinnerListingCtrl', ['$scope', '$http', '$routeParams',
function($scope, $http, $routeParams) {

	$scope.init = function() {
		$http.get('/api/v1/listing/view/current').success(function(data) {
			$scope.currentListings = data;
		});
	};

	$scope.addDeliveryMethod = function() {
		$http.post('/api/v1/listing/add', {
			"name" : $scope.name
		}).success(function() {
			$http.get('/api/v1/listing/view/current').success(function(data) {
				$scope.currentListings = data;
			});
		});
	};
}]);

p2pdinnerControllers.controller('UserProfileCtrl', ['$rootScope', '$scope', '$http', '$routeParams', '$location', '$window', 'userService',
function($rootScope, $scope, $http, $routeParams, $location, $window, userService) {
	$scope.has_signed_in = false;
	$scope.createProfile = function() {
		var profile = {};
		var status = {};
		profile.emailAddress = $scope.emailAddress;
		profile.addressLine1 = $scope.addressLine1;
		profile.addressLine2 = $scope.addressLine2;
		profile.city = $scope.city;
		profile.state = $scope.state;
		profile.zip = $scope.zip;
		profile.password = $scope.password;
		userService.createProfile(profile).then(function(data) {
			console.log(data);
			$scope.isSuccess = true;
			$scope.message = "Registration Successful";
		}, function(err) {
			$scope.isError = true;
			console.log(err);
			$scope.message = err.message;
		});
	};

	$scope.showLogin = function() {
		$window.location.href = '#/signin';
	},

	$scope.init = function() {
		$http.get('/api/v1/profile/states').success(function(data) {
			$scope.states = data;
		});
	};

	$scope.validateUser = function() {
		$http.get('/api/v1/profile/validate', {
			params : {
				"emailAddress" : $scope.emailAddress,
				"password" : $scope.password
			}

		}).success(function(data) {
			console.log(data);
			$scope.has_signed_in = true;
			$scope.profileId = data.id;
			window.location = '#/menu/' + data.id;
		}).error(function(data) {
			console.log(data);
			$scope.hasError = true;
			$scope.errorMessage = data.message;
			$scope.has_signed_in = false;
		});
	};
}]);

p2pdinnerControllers.controller('MenuItemCtrl', ['$scope', '$http', '$routeParams', 'menuService', '$stateParams', 'dinnerCartService', '$rootScope',
function($scope, $http, $routeParams, menuService, $stateParams, dinnerCartService, $rootScope) {
	$scope.createMenuItem = function() {
		var item = {};
		var status = {};
		$scope.profileId = $stateParams.profileId;
		item.title = $scope.title;
		item.description = $scope.description;
		item.userId = $scope.userId;
		item.dinner_special_needs = $scope.dinner_special_need === undefined ? '' : $scope.dinner_special_need.name;
		item.dinner_category = $scope.dinner_category.name;
		item.dinner_delivery = $scope.dinner_delivery === undefined ? '' : $scope.dinner_delivery.name;
		item.userId = $scope.profileId;
		menuService.createMenuItem(item).then(function(data) {
			$http.get('/api/v1/menu/view/' + $stateParams.profileId).success(function(data) {
				$scope.menu = data;
			});
		});
	};

	$http.get('/api/v1/dinnercategory/view').success(function(data) {
		$scope.dinner_categories = data;
	});

	$http.get('/api/v1/delivery/view').success(function(data) {
		$scope.dinner_deliveries = data;
	});

	$http.get('/api/v1/specialneed/view').success(function(data) {
		$scope.dinner_special_needs = data;
	});

	$scope.init = function() {
		$http.get('/api/v1/menu/view/' + $stateParams.profileId).success(function(data) {
			$scope.menu = data;
		});
		$http.get('/api/v1/listing/view/current').success(function(data) {
			$scope.current_listings = data;
		});
		if ( $stateParams.cart_id !== undefined) {
			dinnerCartService.countCartItems($stateParams.profileId).then(function(data){
				$scope.cartItemsCount = data.count;
			}, function(reason) {
				console.log(reason);
			});
		}
		
	};
	$scope.showAddToDinnerListDialog = function(listingId) {
		// alert("Listing Id: " + listingId);
		$scope.listingId = listingId;
		$('#myModal').modal('show');
	};

	$scope.addToDinnerList = function() {
		var dinner_listing = {};
		dinner_listing.costPerItem = $scope.cost_per_item;
		dinner_listing.menuItemId = $scope.listingId;
		dinner_listing.availableQuantity = $scope.available_quantity;
		dinner_listing.startTime = Date.parse($scope.start_time);
		dinner_listing.endTime = Date.parse($scope.end_time);
		dinner_listing.closeTime = Date.parse($scope.close_time);
		$http.post('/api/v1/listing/add', dinner_listing).success(function() {
			$('#myModal').modal('hide');
			$http.get('/api/v1/listing/view/current').success(function(data) {
				$scope.current_listings = data;
			});
		}).error(function() {
			alert("Failed to add listng");
		});
	};

	$scope.showAddToCartDialog = function(listingId) {
		// alert("Listing Id: " + listingId);
		console.log(listingId);
		$scope.listing_id = listingId;
		$('#addToCartDialog').modal('show');
	};

	$scope.addToCart = function() {
		var cart_item = {};
		cart_item.listing_id = $scope.listing_id;
		cart_item.profile_id = parseInt($stateParams.profileId, 10);
		cart_item.quantity = parseInt($scope.quantity, 10);
		if ($rootScope.cart_id !== undefined ) {
			cart_item.cart_id = $rootScope.cart_id;
		}
		dinnerCartService.addToCart(cart_item).then(function(data) {
			console.log(data);
			$rootScope.cart_id = data.response.cartId;
			$('#myModal').modal('hide');
			dinnerCartService.countCartItems($rootScope.cart_id).then(function(data){
				$scope.cartItemsCount = data.response.count;
			}, function(reason) {
				console.log(reason);
			});
		}, function(reason) {
			console.log(reason);
		});
	};
	
	$scope.showCartItems = function() {
		dinnerCartService.getCartItems($rootScope.cart_id).then(function(data) {
			$scope.cartItems = data.results;
			$scope.cartTotal = data.total_price;
			$('#cartItemsDialog').modal('show');
		}, function(reason) {
			console.log(reason);
		});
	};
	
	$scope.placeOrder = function() {
		$('#paymentsDialog').modal('show');
	}
	
	$scope.pay = function(){
		var order = {};
		order.profile_id = parseInt($stateParams.profileId, 10);
		order.cart_id = $rootScope.cart_id;
		if ($scope.card !== undefined) {
			order.card_details = {};
			order.card_details.card = $scope.card;
			order.card_details.expiry_date = $scope.expiry_date;
			order.card_details.address_line_1 = $scope.address_line_1;
			order.card_details.address_line_2 = $scope.address_line_2;
			order.card_details.address_city = $scope.city;
			order.card_details.address_state = $scope.state;
			order.card_details.address_country = $scope.country;
			order.card_details.address_zip = $scope.zip;
		}
		console.log(order);
		dinnerCartService.placeOrder(order).then(function(data){
			$scope.place_order_success = true;
			$scope.place_order_message = data.response.message;
		}, function(reason) {
			$scope.place_order_success = false;
			$scope.place_order_message = reason.message;
		});
	}
	
}]);

p2pdinnerControllers.controller('DinnerSearchCtrl', function($scope, $http) {
	$scope.map = {
		center : {
			latitude : 37.3736118,
			longitude : -122.0205569
		},
		zoom : 8
	};
	$scope.options = {
		scrollwheel : false
	};
	$scope.marker = {
		id : 0,
		coords : {
			latitude : 37.3736118,
			longitude : -122.0205569
		}
	};

	$scope.getCurrentListings = function() {
		var lat = $scope.latitude;
		var lng = $scope.longitude;
		$http.get('/api/v1/places/nearbysearch', {
			params : {
				latitude : lat,
				longitude : lng
			}
		}).success(function(data) {
			var locationMarkers = [];
			angular.forEach(data.results, function(item, key) {
				var marker = {};
				marker.id = key;
				marker.latitude = item.location.lat;
				marker.longitude = item.location.lng;
				marker.title = item.profile_id;
				marker.labelContent = item.menu_items[0].title;
				marker.show = false;
				marker.click = function(event) {
					console.log("marker clicked => " + marker.title);
					$http.get('/api/v1/listing/view/current/' + marker.title).success(function(data) {
						$scope.current_listings = data;
					}).error(function(error) {
						console.log("Error");
					});
				};
				locationMarkers.push(marker);
			});
			console.log(locationMarkers);
			$scope.locationMarkers = locationMarkers;
		});
	};

	$scope.windowOptions = {
		visible : false
	};

	$scope.closeClick = function() {
		$scope.windowOptions.visible = !$scope.windowOptions.visible;
	};
});

