#!/usr/bin/env node

var NodeWebCam = require('node-webcam');
var fs = require('fs');
var http = require('http');

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

webcam.capture('temp', (err, data) => {
	if(err === null) {
		var request = http.request(cloud, (res) => {
			console.log(res.statusMessage);	
		});
		request.write(JSON.stringify(fs.readFileSync(data).toString('base64')));
		request.end();
	} else {
		console.log("Error:");
		console.log(err);
	}
});
