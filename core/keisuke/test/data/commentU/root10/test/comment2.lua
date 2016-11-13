-- Lua
--[=x=] 行コメント
print ("I said \"Hello.\" to him.")
print ('that\'s good.')--Hello World!と画面に表示する
print "Hello World!"　--[[この部分が
コメントとなります]]

for i = 1, 9 do
	array = {}
	for j = 1, 9 do
		table.insert(array , i*j)
		io.write( string.format("%3d", array[j]) )
	end
	io.write("\n")
end
io.write

--[===[ コメント
print([[
Hello World!
こんにちは世界
]])
]===]

local ls = [[
最初の改行は文字列の一部とはならない。
つまり、この文字列は2行である。]]

-- 等号を使った記法（ネスト可能）
local lls = [==[
この記法は、Windowsのパスを表現する場合にも便利である。
local path = [=[C:\Windows\Fonts]=]
-- コメントではない行
]==]

