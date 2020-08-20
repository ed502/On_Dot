
/*
★★★★★  꼭 명심하기
★★★★★  1번점자 4번 소리가 나서 각도를 10으로설정함
★★★★★  꼭 다시 0도로 돌려놓기 ( defaultArray 와 setup에서 꼭 변경해주기 )
*/


#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <SoftwareSerial.h>
//default address 0x40
Adafruit_PWMServoDriver board1 = Adafruit_PWMServoDriver(0x40);
Adafruit_PWMServoDriver board2 = Adafruit_PWMServoDriver(0x41);
Adafruit_PWMServoDriver board3 = Adafruit_PWMServoDriver(0x42);
Adafruit_PWMServoDriver board4 = Adafruit_PWMServoDriver(0x43);
#define BT_RXD 3
#define BT_TXD 2
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
#define SERVOMIN  125 // this is the 'minimum' pulse length count (out of 4096)
#define SERVOMAX  575 // this is the 'maximum' pulse length count (out of 4096)
int servoNumber=0;
static int angleStep=10;

char dotarray[48]; // 블루투스로 숫자 받는 배열
int angleArray[48]={40,40,90,70,90,130,75,90,90,60,90,90,70,70,90,90,90,90,90,80,90,90,90,90,70,70,70,110,120,120,110,110,80,80,90,90,80,90,80,110,90,110,90,90,90,90,90,100};
//점자 "내려"가는 각도 각 서보모터마다 넣은 배열
int defaultArray[48]={180,180,180,10,0,0,0,0,0,180,180,180,180,180,180,0,0,0,0,0,0,180,180,180,180,180,180,0,0,0,0,0,0,180,180,180,180,180,180,0,0,0,0,0,0,180,180,180};
//점자 "올라"가는 각도 각 서보모터마다 넣은 배열
int arraynum=0;
int boardnum=1;
int 4board=0;

void setup() {
  Serial.begin(9600);
  Serial.println("16 channel Servo test!");
  bluetooth.begin(9600);
  board1.begin();
  board2.begin();  
  board3.begin(); 
  board4.begin();
  board1.setPWMFreq(60);  // Analog servos run at ~60 Hz updates
  board2.setPWMFreq(60);
  board3.setPWMFreq(60);
  board4.setPWMFreq(60);

  for(int i = 0 ; i < 48 ; i++) {
    dotarray[i]='2';
  }
  for(int i=0;i<47;i++){
    Serial.println(dotarray[i]);
  }

  //초기각도설정
  //1번점자 보드 1에 0 1 2 3 4 5
  //1 2 3 번은 180도(점자올라감) 0도(점자내려감)
  //2 3 번은 0도까지 다꺾기
  //4 5 6 번은 0도(점자올라감) 180도(점자내려감)
  // ★2번점자 텐션이 약하니 시작각도 180도보다 조금 더뒤로 꺾기★
  // ★3번점자 텐션이 약하니 시작각도 180도보다 조금 더뒤로 꺾기★
    board1.setPWM(0, 0, angleToPulse(180) );
    board1.setPWM(1, 0, angleToPulse(180) );
    board1.setPWM(2, 0, angleToPulse(180) );
    board1.setPWM(3, 0, angleToPulse(10) );
    board1.setPWM(4, 0, angleToPulse(0) );
    board1.setPWM(5, 0, angleToPulse(0) );
//    delay(2000);//테스트코드 잘 돌아가나 확인함
//    board1.setPWM(3, 0, angleToPulse(180) );
//    board1.setPWM(4, 0, angleToPulse(180) );
//    board1.setPWM(5, 0, angleToPulse(180) );

    
  //2번점자 보드 1에 6 7 8 9 10 11
  //1 2 3 번은 0도(점자올라감) 180도(점자내려감)
  //4 5 6 번은 180도(점자올라감) 0도(점자내려감)
    board1.setPWM(6, 0, angleToPulse(0) );
    board1.setPWM(7, 0, angleToPulse(0) );
    board1.setPWM(8, 0, angleToPulse(0) );
    board1.setPWM(9, 0, angleToPulse(180) );
    board1.setPWM(10, 0, angleToPulse(180) );
    board1.setPWM(11, 0, angleToPulse(180) );



  //★보드1에 12 13 14 15 가 보드4의 0 1 2 3 으로 매핑 => 코드수정  
  //3번점자 보드 1에 12 13 14 15 보드2에 0 1
  //1 2 3 번은 180도(점자올라감) 0도(점자내려감)
  //4 5 6 번은 0도(점자올라감) 180도(점자내려감)
//    board1.setPWM(12, 0, angleToPulse(180) );
//    board1.setPWM(13, 0, angleToPulse(180) );
//    board1.setPWM(14, 0, angleToPulse(180) );
//    board1.setPWM(15, 0, angleToPulse(0) );
    board4.setPWM(0, 0, angleToPulse(180) );
    board4.setPWM(1, 0, angleToPulse(180) );
    board4.setPWM(2, 0, angleToPulse(180) );
    board4.setPWM(3, 0, angleToPulse(0) );

    board2.setPWM(0, 0, angleToPulse(0) );
    board2.setPWM(1, 0, angleToPulse(0) );
//    delay(2000);//테스트코드 잘 돌아가나 확인함
//    board2.setPWM(0, 0, angleToPulse(180) );
//    board2.setPWM(1, 0, angleToPulse(180) );


  //4번점자 보드 2에 2 3 4 5 6 7
  //1 2 3 번은 0도(점자올라감) 180도(점자내려감)
  //4 5 6 번은 180도(점자올라감) 0도(점자내려감)
    board2.setPWM(2, 0, angleToPulse(0) );
    board2.setPWM(3, 0, angleToPulse(0) );
    board2.setPWM(4, 0, angleToPulse(0) );
    board2.setPWM(5, 0, angleToPulse(180) );
    board2.setPWM(6, 0, angleToPulse(180) );
    board2.setPWM(7, 0, angleToPulse(180) );

  //★보드2에 12 13 가 보드4의 4 5으로 매핑 => 코드수정  
  //5번점자 3번이랑 6번이랑 핀 바꿔야함 << 바꾸는중 << 바꿧니? 
  //5번점자 보드 2에 8 9 10 11 12 13
  //1 2 3 번은 180도(점자올라감) 0도(점자내려감)
  //4 5 6 번은 0도(점자올라감) 180도(점자내려감)
    board2.setPWM(8, 0, angleToPulse(180) );
    board2.setPWM(9, 0, angleToPulse(180) );
    board2.setPWM(10, 0, angleToPulse(180) );
    board2.setPWM(11, 0, angleToPulse(0) );
    
//    board2.setPWM(12, 0, angleToPulse(0) );
//    board2.setPWM(13, 0, angleToPulse(0) );

    board4.setPWM(4, 0, angleToPulse(0) );
    board4.setPWM(5, 0, angleToPulse(0) );
//        delay(2000);//테스트코드 잘 돌아가나 확인함
//    board2.setPWM(12, 0, angleToPulse(180) );
//    board2.setPWM(13, 0, angleToPulse(180) );

  //★보드2에 14 15 가 보드4의 6 7으로 매핑 => 코드수정  
  //6번점자 보드 2에 14 15 보드 3에 0 1 2 3
  //1 2 3 번은 0도(점자올라감) 180도(점자내려감)
  //4 5 6 번은 180도(점자올라감) 0도(점자내려감)
//    board2.setPWM(14, 0, angleToPulse(0) );
//    board2.setPWM(15, 0, angleToPulse(0) );
    board4.setPWM(6, 0, angleToPulse(0) );
    board4.setPWM(7, 0, angleToPulse(0) );
    board3.setPWM(0, 0, angleToPulse(0) );
    board3.setPWM(1, 0, angleToPulse(180) );
    board3.setPWM(2, 0, angleToPulse(180) );
    board3.setPWM(3, 0, angleToPulse(180) );

    
  //7번점자 보드 3에 4 5 6 7 8 9
  //1 2 3 번은 ___도(점자올라감) ___도(점자내려감)
  //4 5 6 번은 ___도(점자올라감) ___도(점자내려감)
    board3.setPWM(4, 0, angleToPulse(180) );
    board3.setPWM(5, 0, angleToPulse(180) );
    board3.setPWM(6, 0, angleToPulse(180) );
    board3.setPWM(7, 0, angleToPulse(0) );
    board3.setPWM(8, 0, angleToPulse(0) );
    board3.setPWM(9, 0, angleToPulse(0) );
//        delay(2000);//테스트코드 잘 돌아가나 확인함
//    board3.setPWM(8, 0, angleToPulse(180) );
//    board3.setPWM(9, 0, angleToPulse(180) );

  //★보드3에 12 13 14 15 가 보드4의 8 9 10 11으로 매핑 => 코드수정  
  //8번점자 보드 3에 10 11 12 13 14 15
    //1 2 3 번은 0도(점자올라감)180도(점자내려감)
  //4 5 6 번은 180도(점자올라감) 0도(점자내려감)
    board3.setPWM(10, 0, angleToPulse(0) );
    board3.setPWM(11, 0, angleToPulse(0) );
//    board3.setPWM(12, 0, angleToPulse(0) );
//    board3.setPWM(13, 0, angleToPulse(180) );
//    board3.setPWM(14, 0, angleToPulse(180) );
//    board3.setPWM(15, 0, angleToPulse(180) );

    board4.setPWM(8, 0, angleToPulse(0) );
    board4.setPWM(9, 0, angleToPulse(180) );
    board4.setPWM(10, 0, angleToPulse(180) );
    board4.setPWM(11, 0, angleToPulse(180) );
    
    delay(2000);//테스트코드 잘 돌아가나 확인함
//    board3.setPWM(14, 0, angleToPulse(0) );
//    board3.setPWM(15, 0, angleToPulse(0) );

  
}


void loop() {
 블루투스 수신코드
 while (bluetooth.available()) {       
   dotarray[arraynum]=bluetooth.read();
   delay(5);
   Serial.println(dotarray[arraynum]);
   arraynum++;
   if(arraynum==48)
   {
    break;
   }
  }
  arraynum = 0;
  4board = 0;

  for(int i = 0 ; i < 48 ; i++)
  {
    if(i!=0&&(i%16==0))
    {
      boardnum++;
    }
//     Serial.print("boardnum =");
//     Serial.println(boardnum);
    if(boardnum==1)
    {
      //0부터 15까지 중 12 13 14 15 번이 4번보드 0 1 2 3 번

      if(i==12||i==13||i==14||i==15)
      {
        if(dotarray[i]=='1')
        {
          board4.setPWM(4board, 0, angleToPulse(angleArray[i]) );
          4board++
        }
        else if(dotarray[i]=='2')
        {
          board4.setPWM(4board, 0, angleToPulse(defaultArray[i]) );
          4board++
        }          
      }
      if(dotarray[i]=='1')
      {
        board1.setPWM(i, 0, angleToPulse(angleArray[i]) );
      }
      else if(dotarray[i]=='2')
      {
        board1.setPWM(i, 0, angleToPulse(defaultArray[i]) );
      }
    }

    if(boardnum==2)
    {
      //16부터 31까지 중 28 29 30 31 번이 4번보드 4 5 6 7 번

      if(i==28||i==29||i==30||i==31)
      {
        if(dotarray[i]=='1')
        {
          board4.setPWM(4board, 0, angleToPulse(angleArray[i]) );
          4board++
        }
        else if(dotarray[i]=='2')
        {
          board4.setPWM(4board, 0, angleToPulse(defaultArray[i]) );
          4board++
        }  
      }
      
      if(dotarray[i]=='1')
      {
        board2.setPWM(i-16, 0, angleToPulse(angleArray[i]) );
      }
      else if(dotarray[i]=='2')
      {
        board2.setPWM(i-16, 0, angleToPulse(defaultArray[i]) );
      }
    }

    if(boardnum==3)
    {
      //31부터 47까지 중 44 45 46 47 번이 4번보드 8 9 10 11 번

      if(i==44||i==45||i==46||i==47)
      {
        if(dotarray[i]=='1')
        {
          board4.setPWM(4board, 0, angleToPulse(angleArray[i]) );
          4board++
        }
        else if(dotarray[i]=='2')
        {
          board4.setPWM(4board, 0, angleToPulse(defaultArray[i]) );
          4board++
        }  
      }
      
      if(dotarray[i]=='1')
      {
        board3.setPWM(i-32, 0, angleToPulse(angleArray[i]) );
      }
      else if(dotarray[i]=='2')
      {
        board3.setPWM(i-32, 0, angleToPulse(defaultArray[i]) );
      }
    }
  }
  boardnum=1;



//  while(true)
//  { 
//    board1.setPWM(6, 0, angleToPulse(75) );
//    board1.setPWM(7, 0, angleToPulse(90) );
//    board1.setPWM(8, 0, angleToPulse(90) );
//    board2.setPWM(2, 0, angleToPulse(90) );
//    board2.setPWM(3, 0, angleToPulse(80) );
//    board2.setPWM(4, 0, angleToPulse(90) );
//    board3.setPWM(5, 0, angleToPulse(90) );
//    board3.setPWM(6, 0, angleToPulse(80) );
//    board3.setPWM(7, 0, angleToPulse(110) );
//delay(1500);
//    board1.setPWM(6, 0, angleToPulse(0) );
//    board1.setPWM(7, 0, angleToPulse(0) );
//    board1.setPWM(8, 0, angleToPulse(0) );
//    board2.setPWM(2, 0, angleToPulse(0) );
//    board2.setPWM(3, 0, angleToPulse(0) );
//    board2.setPWM(4, 0, angleToPulse(0) );
//    board3.setPWM(5, 0, angleToPulse(180) );
//    board3.setPWM(6, 0, angleToPulse(180) );
//    board3.setPWM(7, 0, angleToPulse(0) );
//    delay(1500);
//  }




//
//
//점자 1번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board1.setPWM(0, 0, angleToPulse(40) );
//    board1.setPWM(1, 0, angleToPulse(40) );
//    board1.setPWM(2, 0, angleToPulse(90) );
//    board1.setPWM(3, 0, angleToPulse(70) );
//    board1.setPWM(4, 0, angleToPulse(90) );
//    board1.setPWM(5, 0, angleToPulse(130) );
////////점자 2번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board1.setPWM(6, 0, angleToPulse(75) );
//    board1.setPWM(7, 0, angleToPulse(90) );
//    board1.setPWM(8, 0, angleToPulse(90) );
//    board1.setPWM(9, 0, angleToPulse(60) );
//    board1.setPWM(10, 0, angleToPulse(90) );
//    board1.setPWM(11, 0, angleToPulse(90) );
    
////점자 3번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board1.setPWM(12, 0, angleToPulse(70) );
//    board1.setPWM(13, 0, angleToPulse(70) );
//    board1.setPWM(14, 0, angleToPulse(90) );
//    board1.setPWM(15, 0, angleToPulse(90) );


//    board4.setPWM(0, 0, angleToPulse(70) );
//    board4.setPWM(1, 0, angleToPulse(70) );
//    board4.setPWM(2, 0, angleToPulse(90) );
//    board4.setPWM(3, 0, angleToPulse(90) );
//    board2.setPWM(0, 0, angleToPulse(90) );
//    board2.setPWM(1, 0, angleToPulse(90) );


////점자 4번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board2.setPWM(2, 0, angleToPulse(90) );
//    board2.setPWM(3, 0, angleToPulse(80) );
//    board2.setPWM(4, 0, angleToPulse(90) );
//    board2.setPWM(5, 0, angleToPulse(90) );
//    board2.setPWM(6, 0, angleToPulse(90) );
//    board2.setPWM(7, 0, angleToPulse(90) );
////점자 5번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board2.setPWM(8, 0, angleToPulse(70) );
//    board2.setPWM(9, 0, angleToPulse(70) );
//    board2.setPWM(10, 0, angleToPulse(70) );
//    board2.setPWM(11, 0, angleToPulse(110) );
////    board2.setPWM(12, 0, angleToPulse(120) );
//    board2.setPWM(13, 0, angleToPulse(120) );
////점자 6번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board2.setPWM(14, 0, angleToPulse(110) );
//    board2.setPWM(15, 0, angleToPulse(110) );
//    board3.setPWM(0, 0, angleToPulse(80) );
//    board3.setPWM(1, 0, angleToPulse(80) );
//    board3.setPWM(2, 0, angleToPulse(90) );
////    board3.setPWM(3, 0, angleToPulse(90) );
////점자 7번 ( 123번 180위-90아래 , 456번 0위-90아래 )
////    board3.setPWM(4, 0, angleToPulse(80) );
//    board3.setPWM(5, 0, angleToPulse(90) );
////    board3.setPWM(6, 0, angleToPulse(80) );
//    board3.setPWM(7, 0, angleToPulse(110) );
//    board3.setPWM(8, 0, angleToPulse(90) );
//    board3.setPWM(9, 0, angleToPulse(110) );
////점자 8번 ( 123번 0위-90아래 , 456번 180위-90아래 )
////    board3.setPWM(10, 0, angleToPulse(90) );
//    board3.setPWM(11, 0, angleToPulse(90) );
//    board3.setPWM(12, 0, angleToPulse(90) );
//    board3.setPWM(13, 0, angleToPulse(90) );
//    board3.setPWM(14, 0, angleToPulse(90) );
//    board3.setPWM(15, 0, angleToPulse(100) );




////점자 1번 ( 123번 180위-90아래 , 456번 0위-90아래 )
////    board1.setPWM(0, 0, angleToPulse(40) );
//    board1.setPWM(1, 0, angleToPulse(40) );
////    board1.setPWM(2, 0, angleToPulse(90) );
//    board1.setPWM(3, 0, angleToPulse(70) );
////    board1.setPWM(4, 0, angleToPulse(90) );
//    board1.setPWM(5, 0, angleToPulse(130) );
////점자 2번 ( 123번 0위-90아래 , 456번 180위-90아래 )
////    board1.setPWM(6, 0, angleToPulse(90) );
//    board1.setPWM(7, 0, angleToPulse(90) );
////    board1.setPWM(8, 0, angleToPulse(90) );
//    board1.setPWM(9, 0, angleToPulse(60) );
////    board1.setPWM(10, 0, angleToPulse(90) );
//    board1.setPWM(11, 0, angleToPulse(90) );
////점자 3번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board1.setPWM(12, 0, angleToPulse(70) );
////    board1.setPWM(13, 0, angleToPulse(70) );
//    board1.setPWM(14, 0, angleToPulse(90) );
////    board1.setPWM(15, 0, angleToPulse(90) );
//    board2.setPWM(0, 0, angleToPulse(90) );
////    board2.setPWM(1, 0, angleToPulse(90) );
////점자 4번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board2.setPWM(2, 0, angleToPulse(90) );
////    board2.setPWM(3, 0, angleToPulse(80) );
//    board2.setPWM(4, 0, angleToPulse(90) );
////    board2.setPWM(5, 0, angleToPulse(90) );
//    board2.setPWM(6, 0, angleToPulse(90) );
////    board2.setPWM(7, 0, angleToPulse(90) );
////점자 5번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board2.setPWM(8, 0, angleToPulse(70) );
////    board2.setPWM(9, 0, angleToPulse(70) );
//    board2.setPWM(10, 0, angleToPulse(70) );
////    board2.setPWM(11, 0, angleToPulse(110) );
//    board2.setPWM(12, 0, angleToPulse(120) );
////    board2.setPWM(13, 0, angleToPulse(120) );
////점자 6번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board2.setPWM(14, 0, angleToPulse(110) );
////    board2.setPWM(15, 0, angleToPulse(110) );
//    board3.setPWM(0, 0, angleToPulse(80) );
////    board3.setPWM(1, 0, angleToPulse(80) );
//    board3.setPWM(2, 0, angleToPulse(90) );
////    board3.setPWM(3, 0, angleToPulse(90) );
////점자 7번 ( 123번 180위-90아래 , 456번 0위-90아래 )
//    board3.setPWM(4, 0, angleToPulse(80) );
////    board3.setPWM(5, 0, angleToPulse(90) );
//    board3.setPWM(6, 0, angleToPulse(80) );
////    board3.setPWM(7, 0, angleToPulse(110) );
//    board3.setPWM(8, 0, angleToPulse(90) );
////    board3.setPWM(9, 0, angleToPulse(110) );
////점자 8번 ( 123번 0위-90아래 , 456번 180위-90아래 )
//    board3.setPWM(10, 0, angleToPulse(90) );
////    board3.setPWM(11, 0, angleToPulse(90) );
//    board3.setPWM(12, 0, angleToPulse(90) );
////    board3.setPWM(13, 0, angleToPulse(90) );
//    board3.setPWM(14, 0, angleToPulse(90) );
////    board3.setPWM(15, 0, angleToPulse(100) );

  delay(1000);
}


int angleToPulse(int ang){
   int pulse = map(ang,0, 180, SERVOMIN,SERVOMAX);// map angle of 0 to 180 to Servo min and Servo max 
//   Serial.print("Angle: ");Serial.print(ang);
//   Serial.print("pulse: ");Serial.println(pulse);
   return pulse;
}