// JavaScript Document
var exec = require('cordova/exec');

var StompClient = {
	reload: function(successCallback, errorCallback){
		cordova.exec(successCallback, errorCallback, 'StompClient', 'reload', [])
	}
}

module.exports =  StompClient;
