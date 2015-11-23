var app = angular.module('myApp', []);
var authStringBase64 = 'Basic YTJkY2Q3ZDEtYzZkZS00ODAxLWFhYWYtNGM5ODVmM2M0NDg5LWJsdWVtaXg6NzNmYThlOTFiMzllN2M3MTk1ODNjODI0M2M5ZjNiN2M1YjFjMzUxNDA5MjIwZmNjMGNiNjNkOGE1YTVhNDFlNQ==';

// all docs query
app.controller('MainCtrl', function($scope, $http) {
    $http.defaults.headers.common['Authorization'] = authStringBase64; 
    $http.defaults.headers.common['Accept']= "application/json";
    $http.defaults.headers.common['Content-Type']= "application/json";
    $http({
      url : "https://a2dcd7d1-c6de-4801-aaaf-4c985f3c4489-bluemix.cloudant.com/water/_all_docs",
      method : 'GET',
      })
    .success(function(data, status) {
      $scope.status = status;
      $scope.data = data;
      $scope.records = data.rows;
      $scope.result = "ok";
    }).error(function(data) {
        $scope.result = "ko";
    });
});


//generic post query for id > 0, fetches all sensor events
app.controller('PostCtrl', function($scope, $http) {
    $http.defaults.headers.common['Authorization']= authStringBase64;
    $http.defaults.headers.common['Accept']= "application/json";
    $http.defaults.headers.common['Content-Type']= "application/json";
    
    var query = '{"selector": {"_id": {"$gt": 0 } },"fields": ["_id","flowRate","occuredAt" ],"sort": [ {"_id": "asc"    }  ] }';
    $http({
      url : "https://a2dcd7d1-c6de-4801-aaaf-4c985f3c4489-bluemix.cloudant.com/water/_find",
      method : 'POST',
      data : query
      })
    .success(function(data, status) {
      $scope.status = status;
      $scope.data = data;
      $scope.records = data.docs;
      $scope.result = "ok";
      $scope.orderByField = 'flowRate';
      $scope.reverseSort = false;
    }).error(function(data) {
        $scope.result = "ko";
    });
});


