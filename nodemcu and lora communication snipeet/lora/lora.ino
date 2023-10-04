#include <ESP8266WiFi.h>
#include <DNSServer.h> 
#include <ESP8266WebServer.h>
#include <LoRa.h>

const char *ssid = "BlackBox";     
const char *password = "password"; 
ESP8266WebServer server(80);
const int ledPin = D3;

#define ss 15
#define rst 16
#define dio0 2

void setup() {
  pinMode(ledPin, OUTPUT);
  void handleGetRequest();
  Serial.begin(115200);
  LoRa.setPins(ss, rst, dio0);
  WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid, password);
  String mac=WiFi.macAddress();
  String IP=WiFi.softAPIP().toString();
  Serial.println("MAC: " + mac+" IP: "+IP);
  server.on("/get", HTTP_GET, handleGetRequest);
  server.begin();

  if (!LoRa.begin(433E6))
  { 
    Serial.println("LoRa initialization failed. Check your connections.");
    while (1);
  }
  Serial.println("LoRa initialization successful.");
  
}

void loop() {
  server.handleClient();
  LoRa.onReceive(onReceive);
}
void onReceive(int packetSize) {
  LoRa.receive();
 
  Serial.println(LoRa.packetRssi());
  while (LoRa.available()) {
    Serial.print("Received packet '");
    for (int i = 0; i < packetSize; i++)
    {
      Serial.print((char)LoRa.read());
    }
    }
  }
void handleGetRequest() {
  String dataReceived = server.arg("data");
  Serial.println("Received GET data: " + dataReceived);
  LoRa.beginPacket();
  LoRa.print(dataReceived);
  LoRa.endPacket();
  Serial.println("Message sent: " + dataReceived);
  digitalWrite(ledPin, HIGH);
  delay(1000); 
  server.send(200, "text/plain", "Sure we are sending aid as soon as possible");
  digitalWrite(ledPin, LOW);
 
  
}
