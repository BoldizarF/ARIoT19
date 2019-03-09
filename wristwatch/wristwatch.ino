 #include "oled.h"
#include "sensors.h"
#include "locals.h"
//#include <WiFi.h>
//#include <ESP8266WiFi.h>
//#include <HTTPClient.h>
#include <WiFi.h>
#include <HTTPClient.h> 
#include <math.h>

float hearbeatSensor(void);

//PulseSensorPlayground pulseSensor;
int Threshold = 3000;
const int PulseWire = 34;
float temp, hb = 0.0f;
int beep = 0, BPM = 0;
//int time_bmp = 0;

#define ONE_WIRE_BUS 27

OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
float Celcius=0;
float Fahrenheit=0;
uint32_t tim = 0;
uint32_t tim2 = 0;
byte count;
byte sensorArray[128];
byte drawHeight;
char filled = 'D'; //decide either filled or dot display (D=dot, any else filled)
char drawDirection = 'R'; //decide drawing direction, from right or from left (L=from left to right, any else from right to left)
char slope = 'W';

byte count2;
byte sensorArray2[128];
byte drawHeight2;
char filled2 = 'D'; //decide either filled or dot display (D=dot, any else filled)
char drawDirection2 = 'R'; //decide drawing direction, from right or from left (L=from left to right, any else from right to left)
char slope2 = 'W';

const char internetSSID[] = "wlankonferanse"; //"NETGEAR97";
const char password[] = "narrowkayak485";
const uint16_t HTTP_PORT = 8888; // ins-server port number
const char SERVER_ADD[] = "ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com"; // ins-server IP address
const int8_t CONNECTION_RETRIES = 20;

const char* streamId   = "/api/v1/healthvalues?api";
const char* privateKey = "666";

//ESP8266WiFiClass wifi;
HTTPClient http;
WiFiClient client;

void setup() {
uint32_t i;

  for (count = 0; count <= 128; count++) //zero all elements
  {
    sensorArray[count] = 0;
  }
    Serial.begin(115200);
  //pinMode(5,OUTPUT); 

  //Network setup
      //wifi.mode(WIFI_STA);
      //wifi.disconnect();
      delay(100);

      Serial.print("Connecting to ");
      WiFi.begin(internetSSID);
      /*if ((WiFi.status() == WL_CONNECTED))
      {
        digitalWrite(5,LOW);
        delay(3000);
        digitalWrite(5,HIGH);
      }*/

        /*digitalWrite(5,HIGH);
        delay(2000);*/
        //digitalWrite(5,HIGH);
        //delay(2000);

      auto attemptsLeft = CONNECTION_RETRIES;
      while ((WiFi.status() != WL_CONNECTED) && (--attemptsLeft > 0)) {
      delay(500); // Wait a bit before retrying
      WiFi.begin(internetSSID);

  }

      //Serial.println("WiFi connected!");
  // SSD1306_SWITCHCAPVCC = generate display voltage from 3.3V internally
  if(!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { // Address 0x3D for 128x64
    //Serial.println(F("SSD1306 allocation failed"));
    for(;;); // Don't proceed, loop forever
  }
  display.clearDisplay();
  delay(2000); 

  sensors.begin();
  
  //pulseSensor.analogInput(PulseWire);   
  //pulseSensor.blinkOnPulse(5);       //auto-magically blink Arduino's LED with heartbeat.
  //pulseSensor.setThreshold(Threshold);  
   
 /*if (pulseSensor.begin()) 
 {display.println("PulseSenor Initialized!");        // un println comme pour écrire sur le port série
 display.display();
 delay(5000);
 display.clearDisplay();
 }*/
  

display.display();
display.clearDisplay();
display.setTextSize(1);                  // setTextSize applique est facteur d'échelle qui permet d'agrandir ou réduire la font
display.setTextColor(WHITE);             // La couleur du texte
display.setCursor((display.width()/2)-30,display.height()/2-10);                  // On va écrire en x=0, y=0
display.println(" Code ");
display.setCursor(display.width()/2-46,display.height()/2+7);
display.println(" Constructors!");        // un println comme pour écrire sur le port série
display.display();
delay(5000);
display.clearDisplay(); 
//for(i=0; i<display.height(); i+=4) {
drawTable();

  //}
  //delay(5000);
//display.clearDisplay(); 

}

void drawTable(void)
{
    display.drawLine(0,display.height()/2, display.width()-1, display.height()/2, WHITE);
    display.setCursor(0,0);
    display.println("\nTemp");
    display.setCursor(0,display.height()/2);
    display.println("\n\nPuls");
    display.display();
}

void loop() {
//digitalWrite(5,LOW);
  // Capture readings

//display.clearDisplay();
//display.setCursor(1,1);
//display.println(hb);        // un println comme pour écrire sur le port série
//display.display();


if ( 1 ) //(hb = hearbeatSensor()) > 0.0f)
{

//hb = hearbeatSensor();
//temp = temperature();

  //
//if (tim2 > 10)
//{
display.clearDisplay();
drawTable();
drawAxises();
drawAxises2();
drawGraph();
drawGraph2();
display.display();

///
  temp = temperature();
  hb = hearbeatSensor();

///
tim2 = 0;
//}
if (tim > 10)
{
//Do this less often
(void)sendData();
tim = 0;
}


}

tim2++;
tim++;
delay(100);
}

float temperature()
{
    sensors.requestTemperatures(); 
  Celcius=sensors.getTempCByIndex(0);
  Fahrenheit=sensors.toFahrenheit(Celcius);
  return Celcius;
}

bool sendData(void)
{

  bool successfulRequest = true;
  String req;
  http.begin("http://ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com/api/v1/healthvalues?apikey=666");
  http.addHeader("Content-Type", "application/json");
  req = "\"";
  req += String((int)(hb));
  req += ":";
  req += String(temp);
  req += "\"";
  Serial.println(req);
  http.POST(req);
  //Serial.println();
  http.end();
  
  //delay(2000);
 
  return successfulRequest;
}


float hearbeatSensor()
{
  int Signal = 0;
  int time_bmp = millis();
  int BPM = 0;

   for (;;)
   {
    Signal = analogRead(PulseWire);  // Read the PulseSensor's value.
                                              // Assign this value to the "Signal" variable.

    Serial.println(Signal);                    // Send the Signal value to Serial Plotter.


   if(Signal > Threshold)
   {                          // If the signal is above "550", then "turn-on" Arduino's on-Board LED.
     BPM++;
     }
    
    if ( (millis() - time_bmp) > 1000*10.0f)
     {

       hb = (BPM/16.0f) * 6; //;
     //Serial.print("BEATPERMINUTE: ");
     //Serial.print(hb);
     //Serial.print(BPM);
     //Serial.print(" ");
     //Serial.print(hb);
       break;
     }
   delay(10);
   }


  
 //int myBPM = pulseSensor.getBeatsPerMinute();  // Calls function on our pulseSensor object that returns BPM as an "int".
                                               // "myBPM" hold this BPM value now. 

/*if (pulseSensor.sawStartOfBeat())
{            // Constantly test to see if "a beat happened". 
  //sendBeatToCloud();
}*/
return (float)hb;
}



void drawGraph()
{
  int spa = 26;
  uint32_t c = 0;

    drawHeight = map((int)(temp), 15, 47, 0, 32 );
  sensorArray[0] = drawHeight;

  for (count = spa; count <= 80+spa; count++ )
  {
    if (filled == 'D' || filled == 'd')
    {
      if (drawDirection == 'L' || drawDirection == 'l')
      {
        display.drawPixel(count, 32 - sensorArray[c - 1], WHITE);
      }
      else //else, draw dots from right to left
      {
        display.drawPixel(80+spa - c, 32 - sensorArray[c - 1], WHITE);
      }
    }


    else
    {
      if (drawDirection == 'L' || drawDirection == 'l')
      {
        if (slope == 'W' || slope == 'w')
        {
          display.drawLine(count, 32, count, 32 - sensorArray[c - 1], WHITE);
        }
        else
        {
          display.drawLine(count, 1, count, 32 - sensorArray[c - 1], WHITE);

        }
      }

      else
      {
        if (slope == 'W' || slope == 'w')
        {
          display.drawLine(80 - count, 32, 80 - count, 32 - sensorArray[c - 1], WHITE);
        }
        else
        {
          display.drawLine(80+spa - count, 1, 80 - count, 32 - sensorArray[c - 1], WHITE);
        }
      }
    }
    c++;
  }

  //drawAxises();
  display.setCursor(90+spa, 0);
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.print((int)(temp));
  //display.display();  //needed before anything is displayed
  //display.clearDisplay(); //clear before new drawing

  for (count = 80; count >= 2; count--) // count down from 160 to 2
  {
    sensorArray[count - 1] = sensorArray[count - 2];
  }

}

void drawAxises()  //separate to keep stuff neater. This controls ONLY drawing background!
{
  int spa = 26;
//  display.setCursor(90+spa, 0);
//  display.setTextSize(1);
//  display.setTextColor(WHITE);
//  display.print(drawHeight);
  display.setCursor(90+spa, 8);
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.print(""); 
  display.print("oC");


  display.drawLine(spa, 0, spa, 32, WHITE);
  display.drawLine(80+spa, 0, 80+spa, 32, WHITE);

  for (count = 0; count < 40; count += 10)
  {
    display.drawLine(80+spa, count, 75+spa, count, WHITE);
    display.drawLine(0+spa, count, 5+spa, count, WHITE);
  }

  for (count = 10; count < 80; count += 10)
  {
    display.drawPixel(count+spa, 0 , WHITE);
    display.drawPixel(count+spa, 31 , WHITE);
  }
}
  ////////////////////////////////////////////////



void drawGraph2()
{
  int he = (display.height()/2)+2;
  int spa = 26;
  uint32_t c = 0;
  
    drawHeight2 = map((int)hb, 32, 96, 0, 32 );
  sensorArray2[0] = drawHeight2;

  for (count2 = spa; count2 <= 80+spa; count2++ )
  {
    if (filled2 == 'D' || filled2 == 'd')
    {
      if (drawDirection2 == 'L' || drawDirection2 == 'l')
      {
        display.drawPixel(count2, 32 - sensorArray2[c - 1]+he, WHITE);
      }
      else //else, draw dots from right to left
      {
        display.drawPixel(80+spa - c, 32 - sensorArray2[c - 1]+he, WHITE);
      }
    }


    else
    {
      if (drawDirection2 == 'L' || drawDirection2 == 'l')
      {
        if (slope2 == 'W' || slope2 == 'w')
        {
          display.drawLine(count2, 32, count2, 32 - sensorArray2[c - 1]+he, WHITE);
        }
        else
        {
          display.drawLine(count2, 1, count2, 32 - sensorArray2[c - 1]+he, WHITE);

        }
      }

      else
      {
        if (slope == 'W' || slope == 'w')
        {
          display.drawLine(80 - count2, 32, 80 - count2, 32 - sensorArray2[c - 1]+he, WHITE);
        }
        else
        {
          display.drawLine(80+spa - count2, 1, 80 - count2, 32 - sensorArray2[c - 1]+he, WHITE);
        }
      }
    }
    c++;
  }

  //drawAxises2();
  display.setCursor(90+spa-6, 0+he);
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.print((int)(hb));
  //display.display();  //needed before anything is displayed
  //display.clearDisplay(); //clear before new drawing

  for (count2 = 80; count2 >= 2; count2--) // count down from 160 to 2
  {
    sensorArray2[count2 - 1] = sensorArray2[count2 - 2];
  }

}

void drawAxises2()  //separate to keep stuff neater. This controls ONLY drawing background!
{
    int spa = 26;
      int he = (display.height()/2)+2;
//  display.setCursor(90+spa-3, 0+he);
//  display.setTextSize(1);
//  display.setTextColor(WHITE);
//  display.print(drawHeight2);
  display.setCursor(90+spa-6, 8+he);
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.print("BPM");


  display.drawLine(spa, 0+he, spa, 32+he, WHITE);
  display.drawLine(80+spa, 0+he, 80+spa, 32+he, WHITE);

  for (count2 = 0; count2 < 40; count2 += 10)
  {
    display.drawLine(80+spa, count2+he, 75+spa, count2+he, WHITE);
    display.drawLine(0+spa, count2+he, 5+spa, count2+he, WHITE);
  }

  for (count2 = 10; count2 < 80; count2 += 10)
  {
    display.drawPixel(count2+spa, 0+he , WHITE);
    display.drawPixel(count2+spa, 31+he , WHITE);
  }
}