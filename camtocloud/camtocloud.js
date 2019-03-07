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
        width:   400,
        height:  300,
        quality: 50,
        delay:   0,
        skip:    20,
});

webcam.capture('temp', (err,data) => {
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
