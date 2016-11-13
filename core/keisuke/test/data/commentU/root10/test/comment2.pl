#!/usr/bin/perl
use warnings;
# 行コメント
my $name = 'foo';
my $age = 26;

print "僕の名前は$nameです\n"; # 僕の名前はfooです
print "年齢は$age歳です\n"; # 年齢は26歳です

# 配列
=for comment
my @fruit = ('キューイ', 'パパイヤ', 'マンゴ');

=cut
my @fruit = ('アップル', 'バナナ', 'オレンジ');

# 表示
print "ぼくは$fruit[1]が好きです\n"; # ぼくはバナナが好きです

my $word = '=pod';

=pod
ここはリテラルドキュメント
'=pod'から'=cut'までが該当します
=cut
my $job 
   = 'artist';

=begin html
<table>
HTML形式のリテラル
</table>
=end html
=cut

my $s1 = "test";
# ヒアドキュメント
print <<"EOD";
この部分を'ダブルクォート'で囲ったのと
#同様の出力となる。
$s1\n
EOD

print <<'EOD';
この部分を"シングルクォート"で囲ったのと
#同様の出力となる。
$s1\n
EOD

print <<EOD;
クォートで囲わない場合は
'ダブルクォート'で囲ったのと
#同様の出力となる。
$s1\n
EOD

print <<`EOC`
date
hostname
EOC

