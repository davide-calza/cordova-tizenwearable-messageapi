# cordova-tizenwearable-messageapi
A Cordova plugin for the communication with a Tizen Wearable device.
It allows you to send and receive messages from the nodes to which the device is connected.
The service works also if the application gets killed.

## Installation
With Cordova CLI, from npm:
```
$ cordova plugin add https://github.com/davide-calza/cordova-tizenwearable-messageapi
```

After building for Android with
```
$ cordova platform add android
```
copy the .xml file **/plugins/cordova-tizenwearable-messageapi/xml/accessoryservices.xml** to **/platforms/android/app/src/main/res/xml/accessoryservices.xml**

## Platform

* Android

## Using

```javascript
TizenWearApi.getMessages(success, error)

```
```javascript
TizenWearApi.sendMessage(msg, success, error)
```

### Example
  ```javascript
  var app = {
    initialize: function () {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    onDeviceReady: function () {
        this.receivedEvent('deviceready');

        document.getElementById("btnsend").addEventListener("click", function () { send('hello'); }, false);

        TizenWearApi.getMessages(function (data) {
            alert(data);
        });
    },

    receivedEvent: function (id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');
    },
};
app.initialize();

function send(msg) {
    TizenWearApi.sendMessage(msg, function (data) {
        Console.log(data);
    });
}
  ```
