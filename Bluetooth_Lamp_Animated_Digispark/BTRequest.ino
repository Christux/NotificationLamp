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

inline void SerialHandle()
{
  if (BTserial.available() > 0)     {
    recvWithStartEndMarkers();
  }

  if (newData) {
    parseData();
  }
}

/*
 * Data incoming on pattern <1,234,023,254>
 */

inline void parseData()
{
  newData = false;

  char animation = num2char(receivedChars[0]);
  char R = num2char(receivedChars[2], receivedChars[3], receivedChars[4]);
  char G = num2char(receivedChars[6], receivedChars[7], receivedChars[8]);
  char B = num2char(receivedChars[10], receivedChars[11], receivedChars[12]);

  if(animation >= 0 && animation < ANIM_COUNT)
  {
    hasChanged = true;
    currentAnim = animation;
    currentColor = RgbColor(R,G,B);
  }
}

inline char num2char(char num)
{
  return num - 48;
}

inline char num2char(char hundreds, char tens, char units)
{
  return num2char(hundreds)*100 + num2char(tens)*10 + num2char(units);
}


