 Public Class Form1


    Private Sub btnDiv_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnDiv.Click

        On Error Resume Next

        Dim X As Decimal
        Dim Y As Decimal

        X = txtValue1.Text
        Y = txtValue2.Text

        lblAnswer.Text = X / Y

    End Sub

    Private Sub btnMulti_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnMulti.Click

        On Error Resume Next

        lblAnswer.Text = txtValue1.Text * txtValue2.Text

    End Sub

    Private Sub btnMinus_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnMinus.Click

        On Error Resume Next

        Dim X As Decimal = txtValue1.Text
        Dim Y As Decimal = txtValue2.Text

        lblAnswer.Text = X - Y

    End Sub

    Private Sub btnPlus_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnPlus.Click

        On Error Resume Next

        Dim X As Decimal
        Dim Y As Decimal

        X = txtValue1.Text
        Y = txtValue2.Text

        lblAnswer.Text = X + Y

    End Sub

End Class
