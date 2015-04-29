var mongoose = require('mongoose');
var crypto = require('crypto');
var Schema = mongoose.Schema;
	//database define.
var UserSchema = new Schema({
	userName:String,
	password:String,
	imeiCode:String,
	ka:String,
	challengeCode:String,
	ExpireTime:Date
});

UserSchema.methods.Register = function(callback){
	console.log(this.userName);
	var that = this;
	this.model('User').findOne({userName:this.userName}, function(err, kitten){
		if(err){
			callback(0, err);
		}
		if(kitten){
		
			callback(0, "username has been registered.");
		}
		else
		{
			console.log(this);
			that.save(function(err){
				if(err){
					callback(0, "save user error.");
				}
				callback(1, that);
			});
		}
	});
}

UserSchema.methods.Login = function(callback){
	var that = this;
	this.model('User').findOne({userName:that.userName}, function(err, kitten){
		if(err){
			callback(0, err);
		}
		if(kitten){
			var val = {};
			val.ka = kitten.ka;
			if(kitten.password === that.password){
				callback(1, val);
			}
			else{
				callback(0, 'password error.');
			}

		}
		else
		{
			callback(0, 'user doesn\'t exist.');
		}
	});
}

function randomIntInc (low, high) {
	return Math.floor(Math.random() * (high - low + 1) + low);
}

UserSchema.methods.AskForCode = function(callback){
	var that = this;
	this.model('User').findOne({userName:that.userName}, function(err, kitten){
		if(err){
			callback(0, err);
		}
		if(kitten){


			var a= "";
			for (var i = 0; i < 6; i++) {
				a = a + randomIntInc(0, 9);
			}
			kitten.challengeCode = a;
			var time = new Date(Date.now());
			time.setMinutes(time.getMinutes() + 1);//过期时间设置为1分钟
			kitten.ExpireTime = time;
			kitten.save();
			var val = {};
			val.challengeCode = a;
			if(kitten.password === that.password){
				callback(1, val);
			}
			else {
				callback(0, 'password error.');
			}
		}
		else
		{
			callback(0, 'user doesn\'t exist.');
		}
	});
}
function test_des(param) {
	var key = new Buffer(param.key);
	var iv = new Buffer(param.iv ? param.iv : 0)
	var plaintext = param.plaintext
	var alg = param.alg
	var autoPad = param.autoPad

	//encrypt
	var cipher = crypto.createCipheriv(alg, key, iv);
	cipher.setAutoPadding(autoPad)  //default true
	var ciph = cipher.update(plaintext, 'utf8', 'hex');
	ciph += cipher.final('hex');
	console.log(alg, ciph)
	return ciph;
}

UserSchema.methods.Validate = function(checkcode, callback){
	var that = this;
	this.model('User').findOne({userName:that.userName}, function(err, kitten){
		if(err){
			callback(0, err);
		}
		if(kitten){
			if(kitten.password === that.password){
				var dateNow = new Date(Date.now());
				if(kitten.ExpireTime < dateNow){
					callback(0, "validate time expired.")
					return;
				}
				var challengeCode = kitten.challengeCode;
				var ka = kitten.ka;
				var imeicode = kitten.imeiCode;
				var d1 = test_des({
					alg: 'des-ecb',
					autoPad: true,
					key: ka,
					plaintext: challengeCode,
					iv: null
				});
				var d2 = test_des({
					alg: 'des-ecb',
					autoPad: true,
					key: ka,
					plaintext: imeicode,
					iv: null
				});
				var shasum = crypto.createHash('sha1');
				shasum.update(d1);
				var r1 = shasum.digest('hex');
				shasum = crypto.createHash('sha1');
				shasum.update(d2);
				var r2 = shasum.digest('hex');
				var i = 0;
				var result = "";
				for(i = 0; i < 8; i ++){
					var num1 = parseInt(r1.substr(i * 5, 5), 16);
					var num2 = parseInt(r2.substr(i * 5, 5), 16);
					var num3 = num1 ^ num2;
					result = result + (num3 % 10);

				}
				if(checkcode === result){
					callback(1, "success!");
				}
				else{
					callback(0, "validate error.");
				}


			}
			else {
				callback(0, 'password error.');
			}
		}
		else
		{
			callback(0, 'user doesn\'t exist.');
		}
	});
}

mongoose.model('User', UserSchema);