rem コメント
Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click
    ' コメント
    Dim Answer As String

    Answer = InputBox("あなたの名前はなんですか？") 'コメント

    MsgBox("こんにちは" & Answer & "さん。")

End Sub
