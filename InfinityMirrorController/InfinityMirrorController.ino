// https://forum.arduino.cc/index.php?topic=214378.0
// http://forum.arduino.cc/index.php?topic=214331.msg1570083#msg1570083

// This version uses a relay file between the server and the
// arduino. I thought it would be easier to use the relay
// instead of getting the arduino to communicate with my 
// server code, but I may be wrong...

#include <SPI.h>
#include <Ethernet.h>

// server location
char serverName[] = "eaglezrserver.ddns.net";

// local internet settings
byte ip[] = {192, 168, 1, x}; // don't be an idiot and forget about this...
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xED, 0xAD };
byte gateway[] = {192, 168, 1, 1};
byte subnet[] = {255, 255, 255, 0};

EthernetClient client;
String command;
int commandLength = 64;
int delayTime = 100;
int currTime = 0;

// Establish the Ethernet connection
void setup() {
  if (Ethernet.begin(mac, ip) == 0 {
    errorLights(1);
    Serial.println("Ethernet failed to configure.");
    while (true);
  }

  Serial.begin(9600);
  Serial.println("Setup...");
  // run setup light pattern? :D
}

// 1. (sometimes) receive new command
// 2. Send update signal to the lights
void loop() {
  if (currTime % delayTime == 0) { // Don't want to waste time checking too often
    currTime = 0; //lol, almost forgot this xD
    getCommand();
  }
  currTime++;
}

// Receive the command from the server
void getCommand(){
  // 1. connect to server
  if(client.connect(serverName, 11896) { 
     Serial.println("Connected");
     
  } else {
    Serial.println("Unable to connect");
    errorLights(2);
  }

  // 2. download the command
  if (client.connected()) {
    for (int i = 0; i < commandLength; i++) {
      command += client.read();
    }
  } else {
    errorLights(3);
  }
}

// Interpret the command and send to the lights
void commandLights() {

}

// Display a light pattern that commincates the error
void errorLights(int error) {

}
