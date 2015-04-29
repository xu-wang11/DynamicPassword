/**
 * Created by xu on 15/4/28.
 */
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var User = mongoose.model('User');

/*register */

router.get('/', function(req, res) {

    /**
     * remain to finish.
     */
    res.send("no get method support");

});
function randomIntInc (low, high) {
    return Math.floor(Math.random() * (high - low + 1) + low);
}
router.post('/', function(req, res){
    var _userName = req.body.userName;
    var _password = req.body.password;
    var _deviceId = req.body.deviceId;
    var a= "";
    for (var i = 0; i < 8; i++) {
        var x = randomIntInc(0, 9);
        a = a + x;
    }
    var generateKey = a;
    var registerUser = new User({userName:_userName, password:_password, imeiCode:_deviceId, ka:generateKey, challengeCode:""});
    registerUser.Register(function(status, msg){
        res.json({'status':status, 'msg':msg});

    });
});

module.exports = router;