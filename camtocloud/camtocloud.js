#!/usr/bin/env node

var NodeWebCam = require('node-webcam');
var fs = require('fs');
var http = require('http');
var webcam = NodeWebCam.create({});

var cloud = {
	host: 'ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com',
	port: 80,
	path: '/api/v1/images?apikey=666',
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
	},
};

webcam.capture('temp', (err,data) => {
	if(err === null) {
		console.log(err);
		console.log(Buffer.from(data).toString('base64'));
	
		var request = http.request(cloud, (res) => {
			console.log(res);	
		});
		request.write(JSON.stringify(fs.readFileSync(data).toString('base64')));
		request.end();
	} else {
		console.log(err);
	}
});

