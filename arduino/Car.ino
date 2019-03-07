#include <Smartcar.h>
const unsigned int TRIG_PIN = 6;
const unsigned int ECHO_PIN = 7;

DirectionlessOdometer odometer(80); // 80 pulses per meter
const int odometerPin = 2;

int sensorPin = A5;
int sensorValue = 0; // variable to store the value coming from the sensor

const int fSpeed = 70; //70% of the full speed forward
const int bSpeed = -70; //70% of the full speed backward
const int lDegrees = -75; //degrees to turn left
const int rDegrees = 75; //degrees to turn right

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

void setup() {
  Serial.begin(9600);
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  odometer.attach(odometerPin, []() {
    odometer.update();
  });
}

void loop() {
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);



  const unsigned long duration = pulseIn(ECHO_PIN, HIGH);
  int distance = duration / 29 / 2;
  if (duration == 0) {
    Serial.println("Warning: no pulse from sensor");
  }
  else {

    Serial.print("Ultrasonic sensor: ");
    Serial.println(distance);


    Serial.print("IR sensor: ");
    Serial.println(analogRead(sensorPin));


    Serial.print("Odometer sensor: ");
    Serial.println(odometer.getDistance());
    //    Serial.print("distance to nearest object:");
    //    Serial.println(distance);
    //    Serial.println(" cm");
    if (distance < 80) {
      //car.setSpeed(100);
    }

    if (analogRead(sensorPin) != 1) {
      car.setSpeed(0);
    }

    if (odometer.getDistance() < 100) {
      car.setSpeed(0);
    }
    delay(1000);
  }
}
