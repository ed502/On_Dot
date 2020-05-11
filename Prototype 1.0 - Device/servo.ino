#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <SoftwareSerial.h>
//default address 0x40
Adafruit_PWMServoDriver board1 = Adafruit_PWMServoDriver(0x40);
Adafruit_PWMServoDriver board2 = Adafruit_PWMServoDriver(0x41);
#define BT_RXD 3
#define BT_TXD 2
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
// Watch video below: http://youtu.be/y8X9X10Tn1k
#define SERVOMIN  100 // 'minimum' pulse length count (out of 4096)
#define SERVOMAX  600 // 'maximum' pulse length count (out of 4096)

int servoNumber=0;
static int angleStep=10;
int angle=180;
int angle2=180;
int angle3=180;
int angle4=0;
int angle5=0;
int angle6=0;
char dotarray[10];
int arraynum=0;
int tempangle=0;
int tempangle2=0;

void setup() {
  Serial.begin(9600);
  Serial.println("16 channel Servo test!");
  bluetooth.begin(9600);
  board1.begin();
  board2.begin();  
  board1.setPWMFreq(60);  // Analog servos run at ~60 Hz updates
  board2.setPWMFreq(60);

  for(int i = 0 ; i < 10 ; i++) {
    dotarray[i]='1';
  }
  for(int i=0;i<10;i++){
    Serial.println(dotarray[i]);
  }
}

// robojax PCA9865 16 channel Servo control
void loop() {
//if (angle <=0 || angle >= 180) {
//  angleStep = -angleStep;
//}
//for( int angle =0; angle<181; angle +=80)
//{
//   for(int i=0; i<16; i++)
//        {      
//            board2.setPWM(temp, 0, angleToPulse(angle) );
//            board1.setPWM(temp, 0, angleToPulse(angle) );
//        }
//}
// 보내는건 110010
 while (bluetooth.available()) {       
   dotarray[arraynum]=bluetooth.read();
      Serial.println(dotarray[arraynum]);
   arraynum++;
   if(arraynum==6)
   {
    break;
   }
  }
  arraynum = 0;
//templeangle이 180이면 4 5 6 번 점자가 튀어나오고 90이면 들어감
//templeangle2이 0 이면 1 2 3 번 점자가 튀어나오고 90이면 들어감
  if(dotarray[0]=='0')
  {
    angle4=90;
  }
  else
  {
    angle4=0;
  }
  if(dotarray[1]=='0')
  {
     angle5=90;
  }
  else
  {
     angle5=0;
  }
  if(dotarray[2]=='0')
  {
     angle6=90;
  }
  else
  {
     angle6=0;
  }
  if(dotarray[3]=='0')
  {
     angle=95;
  }
  else
  {
     angle=180;
  }
  if(dotarray[4]=='0')
  {
     angle2=90;
  }
  else
  {
     angle2=180;
  }
  if(dotarray[5]=='0')
  {
     angle3=90;
  }
  else
  {
     angle3=180;
  }
  board1.setPWM(0, 0, angleToPulse(angle) );
  board1.setPWM(1, 0, angleToPulse(angle2) );
  board1.setPWM(2, 0, angleToPulse(angle3) );
  board1.setPWM(3, 0, angleToPulse(angle4) );
  board1.setPWM(4, 0, angleToPulse(angle5) );
  board1.setPWM(5, 0, angleToPulse(angle6) );
  delay(3000);
  tempangle=180;
  tempangle2=0;
  board1.setPWM(0, 0, angleToPulse(tempangle) );
  board1.setPWM(1, 0, angleToPulse(tempangle) );
  board1.setPWM(2, 0, angleToPulse(tempangle) );
  board1.setPWM(3, 0, angleToPulse(tempangle2) );
  board1.setPWM(4, 0, angleToPulse(tempangle2) );
  board1.setPWM(5, 0, angleToPulse(tempangle2) );
  delay(3000);
}

/*
 * angleToPulse(int ang)
 * gets angle in degree and returns the pulse width
 * written Robojax.com
 */
int angleToPulse(int ang){
   int pulse = map(ang,0, 180, SERVOMIN,SERVOMAX);// map angle of 0 to 180 to Servo min and Servo max 
//   Serial.print("Angle: ");Serial.print(ang);
//   Serial.print("pulse: ");Serial.println(pulse);
   return pulse;
}




//#include <Wire.h>
//#include <Adafruit_PWMServoDriver.h>
//Adafruit_PWMServoDriver board1 = Adafruit_PWMServoDriver(0x40);
//Adafruit_PWMServoDriver board2 = Adafruit_PWMServoDriver(0x41);
//
//#define SERVOMIN 150 // this is the 'minimum' pulse length count (out of 4096)
//#define SERVOMAX 600 // this is the 'maximum' pulse length count (out of 4096)
//
//uint8_t servonum = 0;
//int angle =0;
//int angleStep =10;
//
//void setup() {
//Serial.begin(9600);
//Serial.println("16 channel Servo test!");
//board1.begin();
//board2.begin();
//board1.setPWMFreq(60); // Analog servos run at ~60 Hz updates
//board2.setPWMFreq(60);
//}
//
//int angleToPulse(int ang) {
//  int pulse = map(ang,0,180,SERVOMIN,SERVOMAX);
//  Serial.print("angle: "); Serial.print(ang);
//  Serial.print(" pulse: "); Serial.println(pulse);
//  return pulse;
//}
//void setServoPulse(uint8_t n, double pulse) {
//double pulselength;
//pulselength = 1000000; // 1,000,000 us per second
//pulselength /= 60; // 60 Hz
//Serial.print(pulselength); Serial.println(" us per period");
//pulselength /= 4096; // 12 bits of resolution
//Serial.print(pulselength); Serial.println(" us per bit");
//pulse *= 1000;
//pulse /= pulselength;
//Serial.println(pulse);
//pwm.setPWM(n, 0, pulse);
//}
//
//
//void loop() {
//angle = angle + angleStep;
//if (angle <=0 || angle >= 180) {
//  angleStep = -angleStep;
//}
//board2.setPWM(0, 0, angleToPulse(angle) );
//board1.setPWM(15, 0, angleToPulse(angle) );
//delay(100);
//}
//Serial.println(servonum);
//for (uint16_t pulselen = SERVOMIN; pulselen < SERVOMAX; pulselen++) {
//pwm.setPWM(servonum, 0, pulselen);
//}
//
//delay(10000);
//
//for (uint16_t pulselen = SERVOMAX; pulselen > SERVOMIN; pulselen--) {
//pwm.setPWM(servonum, 0, pulselen);
//
//}
//
//
//delay(10000);
//
//servonum ++;
//
//if (servonum > 15) servonum = 0;
//