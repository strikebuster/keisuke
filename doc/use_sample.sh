#!/bin/bash
# SAMPLEカウント実行シェル

# ＃＃＃対象ソースに合わせて設定＃＃＃
# 新規/改修後ソースのデイレクトリ
SRCROOT="testdat/src"

# 改修前ソースのデイレクトリ
OLDSRCROOT="testdat/oldsrc"
#OLDSRCROOT="" # 新規で改修前がない場合

# ソースのエンコーディング
SRCENCODE="UTF-8"
#SRCENCODE="EUC-JP"
#SRCENCODE="Shift_JIS"
#SRCENCODE="Windows-31J"

# ＝＝＝計助内部設定＝＝＝
# KeisukeのJarパッケージ
JARFILE="keisuke-1.0.0-jar-with-dependencies.jar"
# Keisuke集計時の分類指定
#CLASSIFY="-classify extension"
CLASSIFY="-classify language"
#CLASSIFY="-classify group"
#CLASSIFY="-classify fw:xxx"

# ＝＝＝(1)新規/改修後のソースのカウント＝＝＝
# 計測結果I/Fファイル
IFFILE01="_count.csv"
# 計測結果レポートファイル
OUTFILE01="01_新規改修後全PGM規模.csv"

# StepCount実行
java -cp ${JARFILE} keisuke.count.StepCount -format csv -output ${IFFILE01} -encoding ${SRCENCODE} -showDirectory ${SRCROOT}

# 計測結果集計
java -cp ${JARFILE} keisuke.CountReport ${CLASSIFY} ${IFFILE01} > ${OUTFILE01}

# ＝＝＝新規開発であればここで終了＝＝＝
if [ "_${OLDSRCROOT}_" = "__" ]
then
	exit 0
fi

# ＝＝＝(2)改修前後の差分カウント＝＝＝
# 差分計測結果I/Fファイル
IFFILE02="_diff.txt"
# 差分計測結果から新規ファイルパス抽出結果ファイル
ADDFILE02="_diff_add.txt"
# 差分計測結果から修正ファイルパス抽出結果ファイル
MODFILE02="_diff_modify.txt"
# 計測結果レポートファイル
OUTFILE02="02_改修差分PGM規模.csv"

# DiffCount実行
java -cp ${JARFILE} keisuke.count.DiffCount -format text -output ${IFFILE02} -encoding ${SRCENCODE} ${SRCROOT} ${OLDSRCROOT}

# 差分計測結果集計
java -cp ${JARFILE} keisuke.DiffReport ${CLASSIFY} ${IFFILE02} -aout ${ADDFILE02} -mout ${MODFILE02} > ${OUTFILE02}

# ＝＝＝(3)改修追加PGMのソースのカウント＝＝＝
# StepCount計測結果ファイル
MAFILE03="_count.csv"
# ソート後のStepCount計測結果ファイル
MA2FILE03="_count_sorted.csv"
# DiffCount差分計測から新規ファイルパス抽出ファイル
TRFILE03="_diff_add.txt"
# ソート後の新規ファイルパス抽出ファイル
TR2FILE03="_diff_add_sorted.txt"
# StepCount計測結果から新規ファイルのみ抽出したI/Fファイル
IFFILE03="_count_add.csv"
# 計測結果レポートファイル
OUTFILE03="03_改修追加PGM規模.csv"

# 計測結果ファイルをソート
sort ${MAFILE03} -o ${MA2FILE03}

# 新規ファイルパス抽出ファイルをソート
sort ${TRFILE03} -o ${TR2FILE03}

# 新規ファイルのみ計測結果抽出
java -cp ${JARFILE} keisuke.MatchExtract ${MA2FILE03} ${TR2FILE03} ${IFFILE03}

# 新規ファイルのみの計測結果集計
java -cp ${JARFILE} keisuke.CountReport ${CLASSIFY} ${IFFILE03} > ${OUTFILE03}

# ＝＝＝(4)改修前リグレッション規模のカウント＝＝＝
# 計測結果I/Fファイル
IFFILE04="_count_old.csv"
# 計測結果レポートファイル
OUTFILE04="04_リグレッション規模.csv"

#  StepCount実行
java -cp ${JARFILE} keisuke.count.StepCount -format csv -output ${IFFILE04} -encoding ${SRCENCODE} -showDirectory ${OLDSRCROOT}

# 計測結果集計
java -cp ${JARFILE} keisuke.CountReport ${CLASSIFY} ${IFFILE04} > ${OUTFILE04}

# ＝＝＝(5)改修母体PGMのカウント＝＝＝
# StepCount計測結果ファイル
MAFILE05="_count_old.csv"
# ソート後のStepCount計測結果ファイル
MA2FILE05="_count_old_sorted.csv"
# DiffCount差分計測から修正ファイルパス抽出ファイル
TRFILE05="_diff_modify.txt"
# ソート後の修正ファイルパス抽出ファイル
TR2FILE05="_diff_modify_sorted.txt"
# StepCount計測結果から修正ファイルのみ抽出したI/Fファイル
IFFILE05="_count_modify_old.csv"
# 計測結果レポートファイル
OUTFILE05="05_改修母体規模.csv"

# 計測結果ファイルをソート
sort ${MAFILE05} -o ${MA2FILE05}

# 修正ファイルパス抽出ファイルをソート
sort ${TRFILE05} -o ${TR2FILE05}

# 修正ファイルのみ計測結果抽出
java -cp ${JARFILE} keisuke.MatchExtract ${MA2FILE05} ${TR2FILE05} ${IFFILE05}

# 修正ファイルのみの計測結果集計
java -cp ${JARFILE} keisuke.CountReport ${CLASSIFY} ${IFFILE05} > ${OUTFILE05}

