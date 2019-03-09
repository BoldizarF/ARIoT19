#!/usr/bin/env node

var http = require('http');
var player = require('play-sound')(opts = {})
var Gpio = require('onoff').Gpio;
var bark = new Gpio(22, 'out');
var awkward = new Gpio(21, 'in', 'falling');

var cloud = {
	host:   'ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com',
	port:   80,
	path:   '/api/v1/attack?apikey=000',
	method: 'GET',
};

bark.writeSync(0);

awkward.watch( (err, value) => {
	console.log("Got awkward...");
	if(err === null) {
		bark.writeSync(1);
		player.play("ariot_dog_barking.wav", (err) => {
			console.log(err);	
		});
		setTimeout(() => {
			bark.writeSync(0);
		}, 500, 'awkward');
	} else {
		console.log(err);
	}
});

setInterval(() => {
	console.log("Checking cloud...");

	var request = http.request(cloud, (res) => {
		console.log(res.statusCode);	

		res.on('data', (data) => {
			if(JSON.parse(data) === false) {
				console.log("Alles gut");
			} else {
				console.log("Attack");
				bark.writeSync(1);
				player.play("ariot_dog_barking.wav", (err) => {
					console.log(err);	
				});
				setTimeout(() => {
					bark.writeSync(0);
				}, 500, 'bark');
			}
		});
	});
	request.end();

}, 1000, 'mytimer');


function unexportOnExit() {
	console.log("Exiting...");
	bark.writeSync(0);
	bark.unexport();
}

process.on('SIGINT', unexportOnExit);
console.log("Started...");
