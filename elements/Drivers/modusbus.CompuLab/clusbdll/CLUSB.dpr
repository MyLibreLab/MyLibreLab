library CLUSB;
{CLUSB.DLL  B.Kainka 2003}

uses Windows,
  SysUtils,
  Classes;

type _lIn  = record
    bFunction : Byte;
    bValue1 : Byte;
    bValue2 : Byte;
    bValue3 : Byte;
end;

type _lOut  = record
    bAck : Byte;
    bValue1 : Byte;
    bValue2 : Byte;
    bValue3 : Byte;
    bValue4 : Byte;
    bValue5 : Byte;
    bValue6 : Byte;
    bValue7 : Byte;
end;


var  SaveExit: Pointer;
     ADCA, ADCB: Integer;
     ADCC: Integer;
     DigOut, DigIn: Integer;

procedure InOut;
var DeviceHandle: THandle;
    nBytes: DWord;
    bResult: Boolean;
    lIn: _lIn;
    lOut: _lOut;
begin
  lIn.bFunction := 6;
  lIn.bValue1 := DigOut;
  DeviceHandle := CreateFile ('\\.\CompuLABusb_0',Generic_write,File_Share_write,nil,open_existing,0,0);
  bResult := false;
  if (DeviceHandle <> INVALID_HANDLE_VALUE) then begin
     bResult := DeviceIoControl(DeviceHandle,$08,@lIn,sizeof(lIn),@lOut,sizeof (lOut),nBytes,nil);
     CloseHandle (DeviceHandle);
  end;
  if bResult then begin
    DigIn := lOut.bValue1;
    ADCA := (4*lOut.bValue2+ (lOut.bValue4 and 15));
    ADCB := (4*lOut.bValue3+ (lOut.bValue4 div 15));

    ADCC := (4*lOut.bValue5);
  end;
end;

procedure DOUT (Value: Integer); stdcall;
begin
  DigOut := Value;
  InOut;
end;

function DIN: Integer; stdcall;
begin
  InOut;
  result := DigIn;
end;

function AIN (Kanal: Integer): Integer; stdcall;
begin
  InOut;
  case Kanal of
    1 : result := ADCA;
    2 : result := ADCB;
    //3 : result := ADCC;
  else
    result := 0;
  end

  //if Kanal = 1 then result := ADCA else result := ADCB;
end;

procedure WrRAM(Adresse, Wert: Byte);
var lIn: _lIn;
    lOut: _lOut;
    DeviceHandle: THandle;
    nBytes: DWord;
begin
  lIn.bFunction := 23;
  lIn.bValue1 := Adresse;
  lIn.bValue2 := Wert;
  DeviceHandle := CreateFile ('\\.\CompuLABusb_0',Generic_write,File_Share_write,nil,open_existing,0,0);
  if (DeviceHandle <> INVALID_HANDLE_VALUE) then begin
    DeviceIoControl(DeviceHandle,$04,@lIn,sizeof(lIn),@lOut,sizeof (lOut),nBytes,nil);
    CloseHandle (DeviceHandle);
  end;
end;

procedure INIT;
begin
  WrRAM ($34,$80); //AD-Startkanal festlegen;
end;


procedure LibExit;
begin
  ExitProc := SaveExit;
end;

procedure LibraryProc(Reason: Integer);
begin
 if (Reason = DLL_PROCESS_DETACH) then
end;

exports
  DIN index 1,
  DOUT index 2,
  AIN index 3,
  INIT index 4;

begin
  SaveExit := ExitProc;
  ExitProc := @LibExit;
  DLLProc := @LibraryProc;
  DigOut := 0;
  DigIn := 0;
  ADCA := 0;
  ADCB := 0;
  INIT;
end.


