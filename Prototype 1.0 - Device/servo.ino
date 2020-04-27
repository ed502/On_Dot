
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <SoftwareSerial.h>
// called this way, it uses the default address 0x40
Adafruit_PWMServoDriver board1 = Adafruit_PWMServoDriver(0x40);
Adafruit_PWMServoDriver board2 = Adafruit_PWMServoDriver(0x41);
#define BT_RXD 3
#define BT_TXD 2
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
// Depending on your servo make, the pulse width min and max may vary, you 
// want these to be as small/large as possible without hitting the hard stop
// for max range. You'll have to tweak them as necessary to match the servos you
// have!
// Watch video V1 to understand the two lines below: http://youtu.be/y8X9X10Tn1k
#define SERVOMIN  100 // this is the 'minimum' pulse length count (out of 4096)
#define SERVOMAX  600 // this is the 'maximum' pulse length count (out of 4096)

int angle=0;
int servoNumber = 0;
static int angleStep =10;
  int tempangle=0;
  
void setup() {
  Serial.begin(9600);
  Serial.println("16 channel Servo test!");
  bluetooth.begin(9600);
  board1.begin();
  board2.begin();  
  board1.setPWMFreq(60);  // Analog servos run at ~60 Hz updates
  board2.setPWMFreq(60);
  //yield();

}

// the code inside loop() has been updated by Robojax
void loop() {
int temp=0;
//angle = angle + angleStep;
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
//
tempangle=0;



 if (bluetooth.available()) {       
    Serial.write(bluetooth.read());  //블루투스측 내용을 시리얼모니터에 출력
  }
  if (Serial.available()) {         
    bluetooth.write(Serial.read());  //시리얼 모니터 내용을 블루추스 측에 WRITE
  }




 if (bluetooth.available()) {       
    Serial.write(bluetooth.read());  //블루투스측 내용을 시리얼모니터에 출력
    board2.setPWM(temp, 0, angleToPulse(tempangle) );
    board1.setPWM(temp, 0, angleToPulse(tempangle) );
  }

// robojax PCA9865 16 channel Servo control
  delay(1000);
tempangle=180;
board2.setPWM(temp, 0, angleToPulse(tempangle) );
board1.setPWM(temp, 0, angleToPulse(tempangle) );
  delay(1000);
 
}

/*
 * angleToPulse(int ang)
 * gets angle in degree and returns the pulse width
 * also prints the value on seial monitor
 * written by Ahmad Nejrabi for Robojax, Robojax.com
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
////void setServoPulse(uint8_t n, double pulse) {
////double pulselength;
////pulselength = 1000000; // 1,000,000 us per second
////pulselength /= 60; // 60 Hz
////Serial.print(pulselength); Serial.println(" us per period");
////pulselength /= 4096; // 12 bits of resolution
////Serial.print(pulselength); Serial.println(" us per bit");
////pulse *= 1000;
////pulse /= pulselength;
////Serial.println(pulse);
////pwm.setPWM(n, 0, pulse);
////}
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
////Serial.println(servonum);
////for (uint16_t pulselen = SERVOMIN; pulselen < SERVOMAX; pulselen++) {
////pwm.setPWM(servonum, 0, pulselen);
////}
////
////delay(10000);
////
////for (uint16_t pulselen = SERVOMAX; pulselen > SERVOMIN; pulselen--) {
////pwm.setPWM(servonum, 0, pulselen);
////
////}
////
////
////delay(10000);
////
////servonum ++;
////
////if (servonum > 15) servonum = 0;
//