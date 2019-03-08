#include <WiFi.h> 
#include <HTTPClient.h> 

HTTPClient http;
int status = 0;

void setup() {
  Serial.begin(9600);

  while(status != WL_CONNECTED) {
    status = WiFi.begin("wlankonferanse");
    delay(1000);
    
    Serial.println("Retry...");
  }

  Serial.println("Connected...");
  Serial.println(WiFi.localIP());
}

void loop() {
  http.begin("http://ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com/api/v1/healthvalues?apikey=666");
  http.addHeader("Content-Type", "application/json");
  Serial.println(http.POST("\"140:27.3\""));
  http.end();
  
  delay(2000);
}
