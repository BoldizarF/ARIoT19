#!/usr/bin/env node

var NodeWebCam = require('node-webcam');
var fs = require('fs');
var http = require('http');
var Gpio = require('onoff').Gpio;
var led = new Gpio(17, 'out');
var input = new Gpio(27, 'in', 'falling');

var cloud = {
	host:   'ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com',
	port:   80,
	path:   '/api/v1/images?apikey=666',
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
	},
};

var webcam = NodeWebCam.create({
	width:   352,
	height:  288,
	quality: 100,
	delay:   0,
	skip:    20,
	verbose: false,
});

input.watch( (err, value) => {
	console.log("Got input...");
	if(err === null) {

		led.writeSync(1);
		webcam.capture('temp', (err, data) => {
			if(err === null) {
				var request = http.request(cloud, (res) => {
					console.log(res.statusMessage);	
				});
				request.write(JSON.stringify(fs.readFileSync(data).toString('base64')));
				request.end();

				led.writeSync(0);
			} else {
				console.log("Error:");
				console.log(err);

				led.writeSync(0);
			}
		});
	}
	else {
				console.log("Error:");
				console.log(err);
	}
});

function unexportOnExit() {
	console.log("Exiting...");
	input.unexport();
	led.unexport();
}

process.on('SIGINT', unexportOnExit);
console.log("Started...");
