# ARIoT19 - Cerberus the guard dog
The complete contribution from APTIV Code Constructors Artic IoT Challenge 2019

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

The project uses a large number of different microcontrollers and other platforms.

#### Software
```
Arduino IDE
Android studio, minimum SDK is set to Oreo
SSH
```

#### Hardware
```
Wemos D32
Raspberry pi 3B
Arduino uno
Motorcontroller shield
Robot-03 Car kit for Arduino
IR-sensor
4x Ultrasonic sensors
2x 4-slot AA battery holder
Rotor encoder (used as odometer)
USB camera
OLED display
Heartbeat pulse sensor
Temperature sensor
```

### Installing

The required software can be found through these links:
```
Android studio: https://developer.android.com/studio
Arduino IDE: https://www.arduino.cc/en/Main/Software
SSH is included with most operating systems
```

#### The different hardwares
The car consists of the following components:
```
Raspberry pi 3B
Arduino uno
Motorcontroller shield
Robot-03 Car kit for Arduino
IR-sensor
4x Ultrasonic sensors
2x 4-slot AA battery holder
Rotor encoder (used as odometer)
USB camera
```
And here is how you connect those parts:
![alt text](https://github.com/BoldizarF/ariot19/blob/master/Cerberus%20car%20sketch.png)

The wearable heartbeat monitor consists of these components:
```
Wemos D32
OLED display
Heartbeat pulse sensor
Temperature sensor
```
In addition to those components there is a casing available for 3D printing. The CAD files are available in the "cadfiler" folder in the root of the project.

Print the casing. Fit the components into it. They should all be connected to the wemos. Good luck!

If everything is correctly connected, you should be able to see some images, temperature and heartbeat data in the application. Good luck!

## Project practices

### Test Cases
We understand that writing tests are boring so we as a project have adapted the YOLO mindset.
Skip the test cases and just be happy....

### Code style
Same here, you are unique as an individual. All code is beautiful!

## Deployment
Information about how to get the system up n running

### Cloud
```
1. Build the WatchDogApi
2. Create a zip archive of: 
    - WatchDogApi folder 
    - Dockerfile
    - WatchDogApi.sln
3. Upload the zip to your Elastic Beanstalk on AWS 
```

### Android Dashboard
```
1. Open the app-android project in Android Studio
2. Connect your tablet
3. Press 'Run'
```

### Arduino
TODO: add something about arduino here

## Contributing
If you want to contribute, send us a PR and we most likely won't have a look at it.

## Versioning
We use [SemVer](http://semver.org/) for versioning.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments
Thanks to the guys who arranged the ARIoT 2019 event in Oslo, without them this project would never happened.
A special thanks also to Dimitrios Platis, who coached the team and assisted remotely even during the event.
