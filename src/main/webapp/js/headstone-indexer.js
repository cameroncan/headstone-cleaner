var hiModule = angular.module('headstone-indexer', []);

hiModule.directive('headstoneIndexer', ['$http', function($http){
	return {
		restrict: 'A',
		replace: true,
		template: '<div>' +
					'<div>' +
						'<input type="text" ng-model="result.inputImage" />' +
						'<input type="button" id="submitButton" ng-click="submitPhoto()" value="submit" />' +
					'</div>' +
					'<div>' +
						'<div class="original">' +
							'<img id="original" class="result" /></a>' +
						'</div>' +
						'<div class="result">' +
							'<img id="normal" class="result" />' +
						'</div>' +
						'<div class="result">' +
							'<img id="inverted" class="result" />' +
						'</div>' +
						'<div ng-if="result.processingTime">Processing time: <span ng-bind="result.processingTime" /> seconds</div>' +
					'</div>' +
				'</div>',
		link: function(scope, element, attrs){
			scope.result = {};
			scope.result.inputImage = "001.JPG";
			scope.submitPhoto = function()
			{
				document.getElementById("submitButton").disabled = true;
//				$("#submitButton").prop( "disabled", true );
				$http.post('/headstone-indexer/rest/binarize', scope.result.inputImage).
			    success(function(result, status, headers, config) {
//			    	scope.result.outputImageNormal = result.binarizedNormalPath;
//			    	scope.result.outputImageInverse = results.binarizedInvertedPath;
			    	document.images.original.src = result.originalPath;
			    	document.images.normal.src = result.binarizedNormalPath;
			    	document.images.inverted.src = result.binarizedInvertedPath;
			    	scope.result.processingTime = Math.round((result.duration * 0.001) * 1000) / 1000;
//					$("#submitButton").prop( "disabled", false );
					document.getElementById("submitButton").disabled = false;
			    }).
			    error(function(data, status, headers, config) {
			    	alert("There was an error making the request");
			    });
			};
			
			scope.goToImage = function(){
				alert(scope.result.imageRef);
			}
		}
	}
}]);