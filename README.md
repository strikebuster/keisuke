# keisuke
プログラムソースの行数計測ツール
- - -
### 概要
StepCounter (http://github.com/takezoe/stepcounter) のコマンドラインI/Fで出力されるファイルに対し、集計する機能があります。  
StepCounterを模倣してソースコードの行数計測をする機能もあります。行数計測において本ツールで実装したのはStepCounterの行数カウントと２つの版の差分行数カウントの機能です。コマンドラインI/FおよびAntタスクI/FおよびSwing画面I/Fを提供していますが、StepCounterの用意しているEclipseプラグインのI/Fはありません。
- - -
### 使い方
#### 行数カウント集計コマンド
```
> java -cp keisuke-x.x.x-jar-with-dependencies.jar keisuke.CountReport [行数計測ファイル]
```
行数計測ファイルは入力であり、省略時は標準入力となります。  
行数計測ファイルは、stepcounter.Mainまたはkeisuke.count.StepCountでCSV形式で出力されたファイルと同じフォーマットであることが必要です。  
入力を分類ごとに集計した結果は標準出力に出力されます。

オプション引数"-?", "--help"を指定すると他のオプション引数が表示されます。

#### 差分行数カウント集計コマンド
```
> java -cp keisuke-x.x.x-jar-with-dependencies.jar keisuke.DiffReport [差分計測ファイル]
```
差分計測ファイルは入力であり、省略時は標準入力となります。  
差分計測ファイルは、stepcounter.diffcount.Mainまたはkeisuke.count.DiffCountでTEXT形式で出力されたファイルと同じフォーマットであることが必要です。  
入力を分類ごとに集計した結果は標準出力に出力されます。

オプション引数"-?", "--help"を指定すると他のオプション引数が表示されます。

#### 差分ファイル行数抽出コマンド
```
> java -cp keisuke-x.x.x-jar-with-dependencies.jar keisuke.MatchExtract [行数計測ファイル] [差分リストファイル]
```
行数計測ファイルは入力であり、stepcounter.Mainまたはkeisuke.count.StepCountでCSV形式で出力されたファイルと同じフォーマットであることが必要です。  
差分リストファイルは入力であり、行数計測ファイルに含まれるファイルのうち集計したいものだけを列挙したファイル名リストです。これは差分行数カウント集計コマンドkeisuke.DiffReportの引数オプション"--aout"や"--mout"を使って追加ファイルだけ、修正ファイルだけのリストを出力したものを使うことを想定しています。  
差分リストファイルに記述されたファイルだけの集計結果が標準出力に出力されます。

オプション引数"-?", "--help"を指定すると他のオプション引数が表示されます。

- - -
#### ソースコード行数計測コマンド
```
> java -cp keisuke-x.x.x-jar-with-dependencies.jar keisuke.count.StepCount [ファイル|ディレクトリ] ...
```
行数計測の対象となるファイルまたはディレクトリを引数で１つ以上指定する。  
ディレクトリを指定した場合は、その下位にあるファイルおよびサブディレクトリが計測対象になる。  
計測した結果は標準出力に出力されます。

オプション引数"-?", "--help"を指定すると他のオプション引数が表示されます。

#### ソースコード差分行数計測コマンド
```
> java -cp keisuke-x.x.x-jar-with-dependencies.jar keisuke.count.DiffCount [新版ディレクトリ] [旧版ディレクトリ]
```
差分計測の対象となるディレクトリを引数で２つ指定する。１つ目に新バージョンのソースがあるディレクトリ、２つ目に旧バージョンのソースがあるディレクトリを指定する。  
ディレクトリの下位にあるファイルおよびサブディレクトリが計測対象になる。ディレクトリツリーの階層構造が新旧で対応するように同じ階層レベルのルートディレクトリを選ぶ。  
計測した結果は標準出力に出力されます。

オプション引数"-?", "--help"を指定すると他のオプション引数が表示されます。

- - -
#### ソースコード行数計測画面
```
> java -cp keisuke-swing-x.x.x-jar-with-local-dependencies.jar:keisuke-x.x.x-jar-with-dependencies.jar keisuke.swing.StepCount
```
引数およびオプションはありません。  
keisuke.count.StepCountの引数およびオプションで指定する値は、画面の中で指定できます。

#### ソースコード差分行数計測画面
```
> java -cp keisuke-swing-x.x.x-jar-with-local-dependencies.jar:keisuke-x.x.x-jar-with-dependencies.jar keisuke.swing.DiffCount
```
引数およびオプションはありません。  
keisuke.count.DiffCountの引数およびオプションで指定する値は、画面の中で指定できます。

- - -
### 詳細説明
より詳細な情報については、gitリポジトリのdocディレクトリ内のファイルを参照する。  
ファイルはUTF-8エンコード、UNIX改行で作成しています。

- - -
### 更新履歴
###### Version 1.3.0(2018/5/20)
* 機能追加  
・StepCountとDiffCountのGUI画面を追加  
* 内部的な改変  
・計測機能に新たなI/Fを追加しやすくするため、StepCountとDiffCountに対しオプション設定や機能呼び出しのメソッドを追加  
・keisuke.countの下のパッケージ構成一部見直し  
・Gradleマルチプロジェクトとしてビルド設定変更  

###### Version 1.2.3(2018/1/20)
* バグ修正  
・DiffCountで差分がない場合にルートディレクトリのステータスが「変更」となるバグを修正  
・DiffCountでHtml形式の出力の際に差分のないディレクトリのステータスが「変更」となるバグを修正  
* 出力内容の変更  
・DiffCountの出力結果のラベルの英語定義を変更  

###### Version 1.2.2(2017/12/30)
* バグ修正  
・V.1.2.0の修正においてWindows環境でStepCountのshowDirectoryオプション指定して実行した場合にファイルパスの出力が絶対パスになってしまうバグを修正  

###### Version 1.2.1(2017/12/27)
* バグ修正  
・parseLongが+符号に対応していないJava1.6以前の環境でDiffReportが正しく処理できない問題を対策  

###### Version 1.2.0(2017/12/2)
* 機能改修  
・各コマンドに対するAntタスククラスを追加  
・集計/抽出コマンドに出力ファイルを指定する--outputオプションを追加  
・行数計測機能で以前は結果全体を文字列ソート順に出力していたが、並び順の指定できるオプションを追加  
・行数計測機能において対象外として無視するバージョン管理ツールの管理ディレクトリに"SCCS","vssver.scc",".bzr"を追加  
* 内部的な改変  
・各コマンドの主処理クラスのメソッドをコマンドライン引数とAntタスクプロパティのいずれからも設定できるように見直し  
・コマンドの引数不正時にException発行しない場合があったが発行するように変更  

###### Version 1.1.0(2017/10/28)
* 機能改修  
・行数計測機能のJSON出力において、'/'をエスケープ処理するように修正（これによりstepcounterとの互換性が損なわれたが規格準拠を優先)  
・行数計測機能において計測対象ディレクトリに含まれるバージョン管理ツールの管理ディレクトリ("CVS",".svn",".git",".hg")を無視するように修正  
* 内部的な改変  
・keisuke.countパッケージ（計測機能）の構成を見直し、分割  
・合わせてクラス構成も見直し  
・stepcounter.jarへの依存を廃止し必要なクラスを作成  

###### Version 1.0.4(2017/9/2)
* 機能変更はなく、内部的な改変  
・keisukeパッケージ（集計機能）の構成を見直し、keisuke.report以下にパッケージ分割  
・合わせてクラス構成も見直し  

###### Version 1.0.3(2017/8/19)
* 機能変更はなく、内部的な改変  
・ビルド方法をMavenからGradleに変更  
・checkstyleエラー解消のためのソース修正  
・その他改善のためのソース修正  
・テストデータはZipしたものを管理に変更  

###### Version 1.0.2(2016/12/31)
* 計測対象の追加(JRXML,SCSS,他)  
・language.xmlに以下の定義追加  
　　JSPの拡張子に.jspfを追加  
　　LESS,SCSS,Sass,Stylus,PCSS,Tassを追加  
　　JRXMLを追加  

###### Version 1.0.1(2016/11/19)
* 設定xmlの更新  
・language.xmlにTypeScript定義追加  
・framework.xmlから不要な種類を削除  

###### Version 1.0.0(2016/11/13)
* 初期バージョン  

- - -
### ライセンス
Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

- - -
### 免責事項
* 本ソフトウェア自身およびその実行結果に対し、一切の保証をしません
* 本ソフトウェアを利用したことで生じる結果に対し、如何なる責任も負いません
