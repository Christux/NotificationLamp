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

#include <SoftSerial.h>
#include <ChristuxUtils.h>

#define LED_BUILTIN 1
#define RxPin 2
#define TxPin 5 // (unused here, not connected)
#define PIXELS 5
#define TEMP 40

#define OFF 0
#define STATIC 1
#define BREATHING 2
#define RAINBOW 3
#define BLINK 4
#define ANIM_COUNT 5

BlinkLed bl = BlinkLed(LED_BUILTIN,1000);
SoftSerial BTserial(RxPin, TxPin);

// max length of command is 20 chrs
const byte numChars = 20;
char receivedChars[numChars];
boolean newData = false;

// RGB colors structure
struct RgbColor {
  uint8_t R;
  uint8_t G;
  uint8_t B;

  RgbColor():R(0),G(0),B(0) {};
  RgbColor(uint8_t r, uint8_t g, uint8_t b):R(r),G(g),B(b) {};
};

const RgbColor blank(0,0,0);
RgbColor currentColor = blank;

// Animation
unsigned long nextFlicker = 0;
unsigned long startTime = 0;
bool hasChanged = true;
char currentAnim = RAINBOW;
uint16_t step = 0;

void setup() {

  bl.setup();
  ledsetup();
  Show(currentColor);

  // The default baud rate for the HC-06s I have is 9600. Other modules may have a different speed. 38400 is common.
  BTserial.begin(9600);
}

void loop() {
  
  bl.handle();
  animatorHandle();
  SerialHandle();
}

inline void animatorHandle() {

  unsigned long currTime = millis();
  if (currTime >= nextFlicker) {

    switch(currentAnim)
    {
      case OFF:
        off();
        break;
      case STATIC:
        uniColor();
        break;
      case BREATHING:
        breathing();
        break;
      case RAINBOW:
        rainbow();
        break;
      case BLINK:
        blink();
        break;
      default:
        off();
    }
    
    nextFlicker = currTime + TEMP;
  }
}

