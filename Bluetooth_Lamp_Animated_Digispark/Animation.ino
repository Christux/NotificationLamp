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

inline RgbColor applyBrightness(RgbColor color, uint8_t bright) {

 uint8_t newR = int( (float)color.R * ((float)bright / 255.0) );
 uint8_t newG = int( (float)color.G * ((float)bright / 255.0) );
 uint8_t newB = int( (float)color.B * ((float)bright / 255.0) );

 return RgbColor(newR, newG, newB);
}

inline void Show(RgbColor color)
{
  showColor(color.R,color.G,color.B);
}

inline void breathing()
{
  if(hasChanged)
  {
    hasChanged=false;
    startTime = millis();
  }

  float val = (exp(sin((millis()-startTime+3000.0) / 2000.0 * PI)) - 0.36787944) * 90 + 10;
  Show(applyBrightness(currentColor, (uint8_t)val));
}

inline void off()
{
  if(hasChanged)
  {
    hasChanged=false;
    Show(RgbColor(0,0,0));
  }
}

inline void uniColor()
{
  if(hasChanged)
  {
    hasChanged=false;
    Show(currentColor);
  }
}

inline void blink()
{
  if(hasChanged)
  {
    hasChanged=false;
    step = 0;
  }

  if(step==0) Show(currentColor);
  if(step==(1000/TEMP)) Show(blank);

  step++;
  step = step < (2*1000/TEMP) ? step : 0;
}


inline void rainbow() {

  if(hasChanged)
  {
    hasChanged=false;
    step = 0;
  }

  Show(rainbowColor(step++));

  step = step < 360 ? step : 0;
}



