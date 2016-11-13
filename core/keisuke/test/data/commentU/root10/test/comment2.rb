# Ruby

print "hello world!\n"
true
(1+2)*3
foo()
if test then ok else ng end

# 文字列リテラル
"this is a string #expression\n"
'this is a string #expression\n'
"this is multi # line
string"
# %記法
%q!I said, #"You said, 'She said it.'"! # !で区切り
%#I said, "You said, 'She said it.'"## #で区切り
%Q hogehoge   # ' 'で区切り
%Q
　#文字列内(改行で区切り）
%
　#文字列内(改行で区切り）
%Q(This is it. (foo) << So good.\n)# ()で区切り
%q{It's so bad. {but not worst} (x) <x> * [x] *} # ｛｝で区切り

#文字リテラル
var = ?a
var = foo(?#, arg2)

# 式展開
"my name is #{$ruby}" #=> "my name is RUBY"
'my name is #{$ruby}' #=> "my name is #{$ruby}"
"my name is #$ruby" #=> "my name is RUBY"
"my name is \#{$ruby}" #=> "my name is #{$ruby}"
"my name is #ruby" #=> "my name is #ruby"
class Foo
  @foo = 'RUBY'
  def bar
    puts　"my name is #@foo"
    #=> "my name is RUBY"
  end
end

# 正規表現リテラル
/my name is #{myname}/o

=begin
the everything between a line beginning with `=begin' and
that with `=end' will be skipped by the interpreter.
=end

# ヒアドキュメント
print <<EOS      # 識別子 EOS までがリテラルになる
  the string
  next # line
EOS

# 式の中に開始ラベルを書く
# method の第二引数には "    ヒアドキュメント\n" が渡される
method(arg1, <<LABEL, arg2)
    ヒアドキュメント
    # リテラル
LABEL

# ヒアドキュメントをレシーバにメソッドを呼ぶ
p  <<LABEL.upcase
the lower case string # foo
LABEL

# => "THE LOWER CASE STRING # FOO"

    expected_result = <<~SQUIGGLY_HEREDOC
      This would contain specially formatted text.

      That might span many # lines
    SQUIGGLY_HEREDOC

# 複数のヒアドキュメント
print <<FIRST, <<SECOND
   これは # 一つめのヒアドキュメントです。
   まだ一つめです。
FIRST
   この行からは # 二つめのヒアドキュメントです。
   この行で終わります。
SECOND

# バックスラッシュ記法、式展開が有効
print << "EOS"
The price is #{$price}.
EOS

# 上のものと同じ結果
print << EOS
The price is #{$price}.
EOS

# 式展開はできない
print <<'EOS'
The price is #{$price}.
EOS

# コマンドを実行
print <<`EOC`
date
diff test.c.org test.c
EOC

# これはヒアドキュメントでない
obj = Object.new # obj = nil でも可
class << obj
  def test
     hoge
  end
  # moge
end

