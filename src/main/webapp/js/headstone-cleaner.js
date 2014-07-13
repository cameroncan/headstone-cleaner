var hiModule = angular.module('headstone-cleaner', ['angularFileUpload']);

hiModule.directive('headstoneCleaner', ['$http', '$upload', function($http, $upload){
	return {
		restrict: 'A',
		replace: true,
		template: '<div id="headstoneCleaner" class="mainBody">' +
					'<div id="results">' +
						'<div class="originalImage">' +
							'Chosen Image: <br />' +
							'<img id="original" class="originalImage" /></a>' +
						'</div>' +
						'<div ng-show="hasResult">' +
							'<div class="resultImage">' +
								'<img id="normal" class="resultImage" />' +
							'</div>' +
							'<div class="resultImage">' +
								'<img id="inverted" class="resultImage" />' +
							'</div>' +
						'</div>' +
						'<div ng-if="result.processingTime">Processing time: <span ng-bind="result.processingTime" /> seconds</div>' +
					'</div>' +
					'<div>' +
						'<input type="radio" ng-model="inputType" value="upload">upload image</input>' +
						'<input type="radio" ng-model="inputType" value="available">select a pre-uploaded image</input>' +
					'</div>' +
					'<hr />' +
					'<div id="chooseImageDiv" ng-show="inputType===\'available\'">' +
						'<select ng-model="result.inputImage" size="10" ng-options="availableImage as availableImage for availableImage in availableImages" ng-change="selectInputImage()">' +
						'</select>' +
					'</div>' +
					'<div id="uploadImageDiv" ng-show="inputType===\'upload\'">' +
						'<input type="file" ng-file-select="onFileSelect($files)" accept="image/jpeg,image/png"/>' +
					'</div>' +
					'<hr />' +
					'<div>' +
						'<input type="button" id="submitButton" ng-click="submitPhoto()" value="submit" />' +
					'</div>' +
				'</div>',
		link: function(scope, element, attrs){
			var inputImageDir = "data/input-images";
			scope.result = {};
			scope.input = {};
			scope.hasResult = false;
			scope.inputType = "upload";
			scope.submitPhoto = function()
			{
				document.getElementById("submitButton").disabled = true;
				$http.post('/headstone-cleaner/rest/binarize', scope.result.inputImage).
			    success(function(result, status, headers, config) {
			    	document.images.original.src = result.originalPath;
			    	document.images.normal.src = result.binarizedNormalPath;
			    	document.images.inverted.src = result.binarizedInvertedPath;
			    	scope.result.processingTime = Math.round((result.duration * 0.001) * 1000) / 1000;
					document.getElementById("submitButton").disabled = false;
					scope.hasResult = true;
			    }).
			    error(function(data, status, headers, config) {
			    	alert("There was an error making the request");
			    });
			};
			
			scope.onFileSelect = function($files){
				var file = $files[0];
		    	scope.result.processingTime = null;
		    	scope.hasResult = false;
		    	
				scope.upload = $upload.upload({
					url: "/headstone-cleaner/rest/upload",
					file: file
				}).success(function(data, status, headers, config){
			    	document.images.original.src = data;
			    	scope.result.inputImage = data.substr(data.lastIndexOf('/') + 1);
				}).error(function(data, status, headers, config){
					alert("There was an error uploading the image");
				});
			};
			
			scope.getAvailableImages = function(){
				$http.get('/headstone-cleaner/rest/inputImages').
				success(function(data, status, headers, config) {
					scope.availableImages = data;
				}).
				error(function(data, status, headers, config) {
					alert("There was an error getting the available images");
				});
			};
			
			scope.selectInputImage = function(){
				document.images.original.src = inputImageDir + "/" + scope.result.inputImage;
				scope.hasResult = false;
				scope.result.processingTime = null;
			};
			
			scope.getAvailableImages();
		}
	};
}]);