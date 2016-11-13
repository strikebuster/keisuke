VERSION 5.00
Begin VB.Form frmMain
    Caption = "Form1"
    ClientHeight = 4035
    ClientLeft = 60
    ClientTop = 345
    ClientWidth = 5505
    LinkTopic = "Form1"
    ScaleHeight = 4035
    ScaleWidth = 5505
    StartUpPosition = 3 'Windows ‚ÌŠù’è’l
    Begin VB.CommandButton cmdMessage
        Caption = "•\Ž¦"
        Height = 495
        Left = 1800
        TabIndex = 0
        Top = 1800
        Width = 1575
    End
End
Attribute VB_Name = "frmMain"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Private Sub cmdMessage_Click()

MsgBox "Hello!"

End Sub
