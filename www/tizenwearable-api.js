module.exports = {    
    getMessages: function(success, error) {  
        cordova.exec(success, error, 'TizenWearApi', 'getMessages', []);
    },
    sendMessage: function(msg, success, error){
        cordova.exec(success, error, 'TizenWearApi', 'sendMessage', [msg]);
    }
}