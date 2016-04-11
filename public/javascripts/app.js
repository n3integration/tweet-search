(function () {
    "use strict";
    angular.module('example', ["ui.router"])
        .config(["$stateProvider", "$urlRouterProvider",
            function ($stateProvider, $urlRouterProvider) {
                $urlRouterProvider.otherwise("/main");
                $stateProvider.state('main', {
                    url: "/main",
                    controller: "MainController",
                    templateUrl: "/assets/partials/main.html"
                });
            }])
        .filter('escape', function () {
            return window.encodeURIComponent;
        })
        .service("Twitter", ["$http", "$filter",
            function ($http, $filter) {
                return {
                    toggle: function () {
                        return $http.post("/twitter/status");
                    },
                    search: function (text) {
                        var searchPath = "/twitter/search/" + $filter('escape')(text);
                        return $http.post(searchPath);
                    }
                };
            }])
        .controller("NavController", ["$scope", "Twitter",
            function ($scope, Twitter) {
                $scope.action = "Enable";
                $scope.isRunning = false;
                $scope.toggle = function () {
                    Twitter.toggle().then(function (response) {
                        console.log("toggle response -->", response.data);
                        $scope.isRunning = response.data.status;
                        $scope.action = $scope.isRunning ? "Disable" : "Enable";
                    }, function (err) {
                        console.log("Failed to toggle status: ", err.data);
                    });
                };

            }])
        .controller("MainController", ["$scope", "Twitter",
            function ($scope, Twitter) {
                $scope.text = "";
                $scope.searching = false;

                $scope.clear = function() {
                    $scope.result = null;
                };

                $scope.search = function () {
                    if($scope.text) {
                        $scope.clear();
                        Twitter.search($scope.text).then(function () {
                            console.log("Submitted search criteria: '" + $scope.text + "'");
                        }, function (err) {
                            console.log("Failed to submit search: ", err.data);
                        });
                    }
                };

                if (typeof(EventSource) !== "undefined") {
                    var source = new EventSource('/twitter.stream');

                    source.onmessage = function (event) {
                        $scope.result = JSON.parse(event.data);
                        $scope.$apply();
                        console.log($scope.result);
                    };
                }
                else {
                    alert('Dagger! SSE is not supported by browser.');
                }
            }]);
})();
