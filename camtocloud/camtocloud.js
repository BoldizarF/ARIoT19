#!/usr/bin/env node

var NodeWebCam = require('node-webcam');
var fs = require('fs');
var http = require('http');
var Gpio = require('onoff').Gpio;
var led = new Gpio(17, 'out');
var input = new Gpio(27, 'in', 'falling');
var nodemailer = require('nodemailer');

var mailOptions = {
  from: 'solenyatablet@gmail.com',
  to: 'solenyatablet@gmail.com',
  subject: 'Intruder detected',
  text: 'Please check you APP',
};

var transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'solenyatablet@gmail.com',
    pass: 'Aptiv2018!'
  }
});

var cloud = {
	host:   'watchdog-env.hczkygreee.eu-north-1.elasticbeanstalk.com',
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
				transporter.sendMail(mailOptions, function(error, info){
					if (error) {
				 		console.log(error);
				 	 } else {
				    		console.log('Email sent: ' + info.response);
				  	}
				});

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



