var exec = require('cordova/exec');

exports.init = function (success, error) {
    exec(success, error, 'TizenWearApi', 'init', []);
};

exports.getMessages = function (success, error) {
    exec(success, error, 'TizenWearApi', 'getMessages', []);
};

exports.sendMessage = function (msg, success, error) {
    exec(success, error, 'TizenWearApi', 'sendMessage', [msg]);
};
