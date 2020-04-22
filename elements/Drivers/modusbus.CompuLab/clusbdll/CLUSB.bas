Attribute VB_Name = "Module1"
Declare Sub INIT Lib "CLUSB" ()
Declare Sub DOUT Lib "CLUSB" (ByVal Wert%)
Declare Function DIN Lib "CLUSB" () As Integer
Declare Function AIN Lib "CLUSB" (ByVal Eingang%) As Integer
