/*
 * Simple Demo for MyopenLab and Arduino
 * by Carmelo Salafia cswi@gmx.de
 * Updated to Arduino IDE 1.8.5 by:
 * Javier Velásquez Bogotá DC - 18-APRIL-2018
 * javiervelasquez125@gmail.com
 * 
 * Please use the 104 ceramic capacitor between RESET and GND to avoid auto-reset.
 */

int val;
byte cmd;

int in1=2;
int in2=3;
int in3=4;
int in4=5;
int in5=6;
int in6=7;

int out1=8;
int out2=9;
int out3=10;
int out4=11;
int out5=12;
int out6=13;

unsigned char inputs;
unsigned long previousMillis = 0;      // will store last time custom task execution
const long interval = 1000;           // interval at which execute custom task (milliseconds)


void setup()
{
  Serial.begin(115200); // Do Not Modify BaudRate!
  delay(125);

  //SET INPUTS WITH PULLUP   
  pinMode(in1, INPUT_PULLUP);
  pinMode(in2, INPUT_PULLUP);
  pinMode(in3, INPUT_PULLUP);
  pinMode(in4, INPUT_PULLUP);
  pinMode(in5, INPUT_PULLUP);
  pinMode(in6, INPUT_PULLUP);

  //SET OUTPUTS
  pinMode(out1, OUTPUT);
  pinMode(out2, OUTPUT);
  pinMode(out3, OUTPUT);
  pinMode(out4, OUTPUT);  
  pinMode(out5, OUTPUT);
  pinMode(out6, OUTPUT);  
  
  //All OUTPUTS OFF
  digitalWrite(out1,LOW);
  digitalWrite(out2,LOW);
  digitalWrite(out3,LOW);
  digitalWrite(out4,LOW);  
  digitalWrite(out5,LOW);
  digitalWrite(out6,LOW);  
 
  Serial.flush();
  // Arduino is Ready now
}


void sendShort(int val)
{
Serial.write(val>>8); // HIGH Byte
Serial.write(val & 0xFF); // LOW Byte      
}

void loop()
{ 
   unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) {
    // save the last time you executed this custom task
    previousMillis = currentMillis;

    // Do your Custom Task and Do not use SerialPrint()
  }
 
}

void serialEvent(){
  if (Serial.available())  // if Arduino is Polled!
  {
    cmd = Serial.read();
    if ( (cmd & (1<<0))>0) digitalWrite(out1,1); else digitalWrite(out1,0);
    if ( (cmd & (1<<1))>0) digitalWrite(out2,1); else digitalWrite(out2,0);
    if ( (cmd & (1<<2))>0) digitalWrite(out3,1); else digitalWrite(out3,0);
    if ( (cmd & (1<<3))>0) digitalWrite(out4,1); else digitalWrite(out4,0);
    if ( (cmd & (1<<4))>0) digitalWrite(out5,1); else digitalWrite(out5,0);
    if ( (cmd & (1<<5))>0) digitalWrite(out6,1); else digitalWrite(out6,0);
    
    // Read Inputs
    inputs=0;
    if(digitalRead(in1)==LOW) inputs|=(1<<0); else inputs&=~(1<<0);
    if(digitalRead(in2)==LOW) inputs|=(1<<1); else inputs&=~(1<<1);
    if(digitalRead(in3)==LOW) inputs|=(1<<2); else inputs&=~(1<<2);
    if(digitalRead(in4)==LOW) inputs|=(1<<3); else inputs&=~(1<<3);
    if(digitalRead(in5)==LOW) inputs|=(1<<4); else inputs&=~(1<<4);
    if(digitalRead(in6)==LOW) inputs|=(1<<5); else inputs&=~(1<<5);
    inputs=~inputs;
    // Send input Values Binary
    Serial.write(inputs);
    
    // Send ADC 0-5 values
    sendShort(analogRead(0));
    sendShort(analogRead(1));   
    sendShort(analogRead(2));    
    sendShort(analogRead(3));
    sendShort(analogRead(4));  
    sendShort(analogRead(5)); 
  }
}

