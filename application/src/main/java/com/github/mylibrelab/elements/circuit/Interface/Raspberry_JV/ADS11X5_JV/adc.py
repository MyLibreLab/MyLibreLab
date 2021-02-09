# the screen.
# Author: Tony DiCola
# License: Public Domain
import time
import sys

# Import the ADS1x15 module.
import Adafruit_ADS1x15


#print 'Number of arguments:', len(sys.argv), 'arguments.'
#print 'Argument List:', str(sys.argv)

module=str(sys.argv[1])
addressIn=int(sys.argv[2],16)
busNumIn=int(sys.argv[3])
gainIn=str(sys.argv[4])

#print gainIn


if module == "1115":
    # Create an ADS1115 ADC (16-bit) instance.
    adc = Adafruit_ADS1x15.ADS1115(address=addressIn, busnum=busNumIn)
    #print "module 16bit"
else:
    # Or create an ADS1015 ADC (12-bit) instance.
    #adc = Adafruit_ADS1x15.ADS1015()
    adc = Adafruit_ADS1x15.ADS1015(address=addressIn, busnum=busNumIn)
    #print "module 12bit"

# Note you can change the I2C address from its default (0x48), and/or the I2C
# bus by passing in these optional parameters:

# Create an ADS1115 ADC (16-bit) instance.
#adc = Adafruit_ADS1x15.ADS1015(address=addressIn, busnum=busNumIn)

# Choose a gain of 1 for reading voltages from 0 to 4.09V.
# Or pick a different gain to change the range of voltages that are read:
#  - 2/3 = +/-6.144V
#  -   1 = +/-4.096V
#  -   2 = +/-2.048V
#  -   4 = +/-1.024V
#  -   8 = +/-0.512V
#  -  16 = +/-0.256V
# See table 3 in the ADS1015/ADS1115 datasheet for more info on gain.
#GAIN = 1
if gainIn == "1":
    GAIN = 1
if gainIn == "2":
    GAIN = 2
if gainIn == "4":
    GAIN = 4
if gainIn == "8":
    GAIN = 8
if gainIn == "16":
    GAIN = 16
if gainIn == "2/3":
    GAIN = 2/3
#print('Reading ADS1x15 values, press Ctrl-C to quit...')
# Print nice channel column headers.
#print('| {0:>6} | {1:>6} | {2:>6} | {3:>6} |'.format(*range(4)))
#print('-' * 37)
# Main loop.
#while True:
# Read all the ADC channel values in a list.
values = [0]*4
for i in range(4):
    # Read the specified ADC channel using the previously set gain value.
    values[i] = adc.read_adc(i, gain=GAIN)
    # Note you can also pass in an optional data_rate parameter that controls
    # the ADC conversion time (in samples/second). Each chip has a different
    # set of allowed data rate values, see datasheet Table 9 config register
    # DR bit values.
    #values[i] = adc.read_adc(i, gain=GAIN, data_rate=128)
    # Each value will be a 12 or 16 bit signed integer value depending on the
    # ADC (ADS1015 = 12-bit, ADS1115 = 16-bit).
# Print the ADC values.
print('| {0:>6} | {1:>6} | {2:>6} | {3:>6} |'.format(*values))
# Pause for half a second.
#time.sleep(0.5)
