@echo off
cd -d %~dp00

rem hoge1	zzz コメント行末に追記しても差分に出ない確認１
	rem	hoge2
	rem
	remove "'HOGE4'""　'zzz' 修正行の確認2（追加１，削除１）"
HOGE5 rem HOGE5　　zzz 修正行の確認3（追加１，削除１）
 rem rem hoge6
REM zzz コメント行を追加しても差分に出ない確認２　＆　下に空行追加しても出ない確認３

echo 'that''s very good.'
echo "I said ""Hello."" to him."
