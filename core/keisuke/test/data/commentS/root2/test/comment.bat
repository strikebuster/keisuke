@echo off
cd -d %~dp00zzz1

rem hoge1	zzz コメント行末に追記しても差分に出ない確認1
	rem	hoge2
	rem
	remove HOGE4　zzz 修正行の確認2（追加１，削除１）
HOGE5a rem HOGE5b　　zzz 修正行の確認3（追加１，削除１）
 rem rem hoge6
REM zzz コメント行を追加しても差分に出ない確認2　＆　下に空行追加しても出ない確認3
