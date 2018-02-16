/* 
 * Copyright (c) 2018 Christophe Rubeck.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU Lesser General Public License as   
 * published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * Lesser General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

#include <SoftwareSerial.h>
#include <ChristuxUtils.h>
#include <NeoPixelBus.h>
#include <ChristuxAnimation.h>
#define DEBUG true
#define RxPin 6
#define TxPin 7 // (unused here, not connected)

BlinkLed bl = BlinkLed(LED_BUILTIN,1000);
NeoPixelAdapter<NeoGrbFeature, Neo800KbpsMethod> strip(1, 8); // pixelNumber, pixelPin

Animator animator;

Off off(1, &strip);
UniColor unicolor(1, &strip);
Breathing breath(1, &strip);
RainbowLamp rainbow(1, &strip);
Blink blink(1, &strip, 1000);

SoftwareSerial BTserial(RxPin, TxPin);

// max length of command is 20 chrs
const byte numChars = 20;
char receivedChars[numChars];
boolean newData = false;

void setup() {

  bl.setup();

#if DEBUG
  Serial.begin(9600);
  Serial.println("Arduino is ready");
#endif

  strip.Begin();

  animator.add(&off);
  animator.add(&unicolor);
  animator.add(&breath);
  animator.add(&rainbow);
  animator.add(&blink);

  // Do not forget to set a color !
  animator.setColor(Color::blank);
  animator.setAnimation(0);

  // The default baud rate for the HC-06s I have is 9600. Other modules may have a different speed. 38400 is common.
  BTserial.begin(9600);
}

void loop() {
  
  bl.handle();
  animator.handle();
  SerialHandle();
}

