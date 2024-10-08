; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Kismet Oto Stok Takip Sistemi"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "alcanl.com, Inc."
#define MyAppURL "https://www.alcanl.com/"
#define MyAppExeName "Kismet Oto Stok Takip Sistemi.exe"
#define MyAppAssocName MyAppName + " File"
#define MyAppAssocExt ".myp"
#define MyAppAssocKey StringChange(MyAppAssocName, " ", "") + MyAppAssocExt

[Setup]
AppName=K�smet Oto Stok Takip Sistemi
AppVersion=1.0.0
AppPublisher=alcanl.com, Inc.
AppPublisherURL=https://www.alcanl.com/
AppSupportURL=https://www.alcanl.com/
AppUpdatesURL=https://www.alcanl.com/
DefaultDirName={autopf}\Kismet Oto
DisableDirPage=yes
ChangesAssociations=yes
ChangesEnvironment=yes
DisableProgramGroupPage=yes
LicenseFile=C:\Users\alica\Desktop\Kismet Oto\LICENCE.txt
OutputDir=C:\Users\alica\Desktop\Kismet Oto
OutputBaseFilename=Kismet_Auto_Stock_Tracking_System_Setup_1.0.0
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "turkish"; MessagesFile: "compiler:Languages\Turkish.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\alica\Desktop\Kismet Oto\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\alica\Desktop\Kismet Oto\jre\jdk-21_windows-x64_bin.exe"; DestDir: "{app}"; Flags: ignoreversion; AfterInstall: RunOtherInstallerJre
Source: "C:\Users\alica\Desktop\Kismet Oto\pgsql\postgresql-15.6-1-windows-x64.exe"; DestDir: "{app}"; Flags: ignoreversion; AfterInstall: RunOtherInstallerPgSql
Source: "C:\Users\alica\Desktop\Kismet Oto\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Registry]
; set PATH
Root: HKLM; Subkey: "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType:string; ValueName:"JAVA_HOME"; ValueData:"{commonpf64}\Java\jdk-21"; Flags: preservestringtype
Root: HKCU; Subkey: "Environment"; ValueType:string; ValueName:"PATH"; ValueData:"{olddata};%JAVA_HOME%\bin"; Flags: preservestringtype
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocExt}\OpenWithProgids"; ValueType: string; ValueName: "{#MyAppAssocKey}"; ValueData: ""; Flags: uninsdeletevalue
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocName}"; Flags: uninsdeletekey
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName},0"
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" ""%1"""
Root: HKA; Subkey: "Software\Classes\Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".myp"; ValueData: ""

[Code]

procedure SHChangeNotify(wEventId, uFlags: Cardinal; dwItem1, dwItem2: Integer);
  external 'SHChangeNotify@Shell32.dll stdcall';

procedure RunOtherInstallerJre;
var
  ResultCode: Integer;
begin
  if not Exec(ExpandConstant('{app}\jdk-21_windows-x64_bin.exe'), '', '', SW_SHOWNORMAL,
    ewWaitUntilTerminated, ResultCode)
  then
    MsgBox('Other installer failed to run!' + #13#10 +
      SysErrorMessage(ResultCode), mbError, MB_OK);
end;

procedure RunOtherInstallerPgSql;
var
  ResultCode: Integer;
begin
  if not Exec(ExpandConstant('{app}\postgresql-15.6-1-windows-x64.exe'), '', '', SW_SHOWNORMAL,
    ewWaitUntilTerminated, ResultCode)
  then
    MsgBox('Other installer failed to run!' + #13#10 +
      SysErrorMessage(ResultCode), mbError, MB_OK);
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    // Notify the system about the environment variable change
    SHChangeNotify($08000000, $0000, 0, 0);
  end;
end;

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon