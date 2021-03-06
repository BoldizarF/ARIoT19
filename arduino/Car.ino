#include <Smartcar.h>
#include <NewPing.h>

const unsigned int FRONT_TRIG_PIN = 6;
const unsigned int FRONT_ECHO_PIN = 7;
const unsigned int LEFT_TRIG_PIN = 4;
const unsigned int LEFT_ECHO_PIN = 5;
const unsigned int RIGHT_TRIG_PIN = 3;
const unsigned int RIGHT_ECHO_PIN = 2;

NewPing front(FRONT_TRIG_PIN, FRONT_ECHO_PIN, 200);
NewPing left(LEFT_TRIG_PIN, LEFT_ECHO_PIN, 200);
NewPing right(RIGHT_TRIG_PIN, RIGHT_ECHO_PIN, 200);

int currentSpeed = 0;

int frontDistance;

int leftDistance;
int rightDistance;

#define FACING_LEFT = 2;
#define FACING_FORWARD = 1;
#define FACING_RIGHT = 3;
#define CAR_POSITION = 1;

DirectionlessOdometer odometer(80); // 80 pulses per meter
const int odometerPin = 2;

int sensorPin = A5;
int sensorValue = 0; // variable to store the value coming from the sensor

String driving = "Stopped";
int cameraPos = 0;    // variable to store the servo position


Servo cameraServo;  // create servo object to control a servo

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);
int direction;

void setup() {
  Serial.begin(9600);
  digitalWrite(1, LOW);

  pinMode(A0, OUTPUT);
  pinMode(A1, INPUT);
  direction = 0;

  odometer.attach(odometerPin, []() {
    odometer.update();
  });

  cameraServo.attach(3);  // attaches the servo on pin 9 to the servo object
}

void loop() {

  if(digitalRead(A1) == HIGH){
    attack();
  }
  checkFront();
  checkLeft();
  checkRight();

  if (odometer.getDistance() > 200) {
    currentSpeed = 0;
    driving = "Stopped";
  }

  Serial.print("Front Ultrasonic sensor: ");
  Serial.println(frontDistance);

  Serial.print("Left Ultrasonic sensor: ");
  Serial.println(leftDistance);

  Serial.print("Right Ultrasonic sensor: ");
  Serial.println(rightDistance);


  Serial.print("IR sensor: ");
  Serial.println(analogRead(sensorPin));


  Serial.print("Odometer sensor: ");
  Serial.println(odometer.getDistance());

  Serial.print("Driving state: ");
  Serial.println(driving);


  car.setSpeed(currentSpeed);


  frontDistance = 0;
  leftDistance = 0;
  rightDistance = 0;

  delay(50);
 

}


void frontDetection() {
  sendSignalToPi();
}

void leftDetection() {

  moveCarLeft();
  sendSignalToPi();

}

void rightDetection() {
  moveCarRight();
  sendSignalToPi();
}

void attack() {
  Serial.println("ATTACKING!!!!!");
  
  //currentSpeed = 100;
}

void moveCarLeft() {
  Serial.println("Moving Car to left");
  car.overrideMotorSpeed(100, -100);
  delay(600);
  car.setSpeed(0);
}

void moveCarRight() {
  Serial.println("Moving Car to Right");
  car.overrideMotorSpeed(-100, 100);
  delay(600);
  car.setSpeed(0);

}

void checkFront() {
  frontDistance = front.ping_cm();
  if (frontDistance < 20 && frontDistance != 0) {
    frontDetection();
  }

}

void checkLeft() {

  leftDistance = left.ping_cm();
  if (leftDistance < 50 && leftDistance != 0) {
    leftDetection();
  }
}

void checkRight() {
  rightDistance = right.ping_cm();
  if (rightDistance < 50 && rightDistance != 0) {
    rightDetection();
  }
}

void sendSignalToPi() {
  Serial.println("Sending signal to PI");
  digitalWrite(A0, HIGH);
  delay(1000);
  digitalWrite(A0, LOW);



}
