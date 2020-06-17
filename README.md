# keisuke
プログラムソースの行数計測ツール
- - -
### 概要
StepCounter (http://github.com/takezoe/stepcounter) のコマンドラインI/Fで出力されるファイルに対し、集計する機能があります。  
StepCounterを模倣してソースコードの行数計測をする機能もあります。行数計測において本ツールで実装したのはStepCounterの行数カウントと２つの版の差分行数カウントの機能です。  
コマンドラインI/FおよびAntタスクI/FおよびSwing画面I/FおよびJenkinsプラグインを提供していますが、StepCounterの用意しているEclipseプラグインはありません。  
計測可能な言語についてはdocディレクトリ内のlanguage.xmlを参照してください。
主な言語を例示すると
ASP, BAT, C/C++, C#, Clojure, COBOL, Delphi, Elixir, Erlnag, Flex, Fortran, Go, Groovy,
Haskell, HTML, Io, Java, JavaScript, Jelly, JSP, Kotlin, Lisp, Lua, Perl, PHP, Prolog, Python, R, Ruby,
Scala, Scheme, Shell, Smalltalk, SQL, TypeScript, VisualBasic, VBScript, XML
などがあります。

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
差分計測ファイルは、stepcounter.diffcount.Mainまたはkeisuke.count.DiffCountでTEXT形式で出力されたファイルと同じフォーマットであること、もしくはkeisuke.count.DiffCountでCSV形式で出力されたファイルと同じフォーマットであることが必要です。  
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
stepcounter.diffcount.Mainとの互換のため通常のdiffと比較基準となる引数の位置が逆であることに注意する。  
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
#### ソースコード行数計測/差分行数計測Jenkinsプラグイン
keisuke-jenkins-x.x.x.hpiをJenkinsに導入し利用する。ビルド後処理に追加される。  
Pipelineにも対応しています。  
ジョブの設定にて計測モードを３種類から選択する。  
（１）ディレクトリ配下全ファイルの行数計測  
（２）ディレクトリ配下からパターンに合致するファイルを対象に行数計測  
（３）（１）に加え、（１）のディレクトリの旧版のソースとの差分行数計測  
　　　旧版のソースは直前のビルドのソースではなく、比較基準となる版（例えば直近のリリース版）を格納したディレクトリを指定して比較します。

- - -
### 詳細説明
より詳細な情報については、gitリポジトリのdocディレクトリ内のファイルを参照する。  
ファイルはUTF-8エンコード、UNIX改行で作成しています。

- - -
### 更新履歴
###### Version 2.1.2(2020/6/17)
* 計測対象の追加(Elixir,Erlang,Go,Io,Kotlin,Prolog)  
・language.xmlにElixir,Erlang,Go,Io,Kotlin,Prologの定義を追加  
* 言語定義のバグ修正  
・Java等の言語で文字リテラルで引用符（'"'または'\\"'）があると計測を誤ることがあったので定義を修正  
・Fortranで文字定数に引用符""を使っていた場合に正しく計測できていなかった定義誤りを修正  

###### Version 2.1.1(2020/6/13)
* DiffReport機能のラベル表記の一部変更  
・出力ラベルの"増加"と"減少"を"追加"と"削除"へ変更  
* Report機能のバグ修正  
・V2.0.0でStepCountとDiffCountのCSV出力で未対応ソースの行数を省略する変更をしたが、CountReportとDiffReportがその出力結果に対応漏れであったバグを修正  

###### Version 2.1.0(2020/6/8)
* 計測対象の追加(Jelly)  
・language.xmlにJellyを追加  
* 機能改修  
・keisuke.CountReportの出力データのラベル文字列を一部変更  
　"合計"→"全行数"、"有効"→"コード"  
・keisuke.countの出力データのラベル文字列およびデータのキー文字列を一部変更  
　TEXT("合計"→"全行数", "Exec"→"Code", "Blan"→"Blank", "Cmnt"→"Comnt")  
　EXCEL("合計"→"全行数", "追加"→"追加行数", "削除"→"削除行数", "execut"→"Code", "blanc"→"Blank", "commen"→"Comment")  
　JSON,XML("step"→"code", "none"→"blank")  
・keisuke.countのExcelテンプレートのシート名を一部変更  
* バグ修正  
・message.propertiesのキー名に誤りがあったものを修正  
* 内部的な改変  
・coreモジュールの依存ライブラリのリビジョン更新  
・Jenkinsプラグインの依存する親POMと jenkins-coreのリビジョン更新  
・ビルド時のJavaコード解析をFindBugsからSpotBugsへ変更  
・ビルドスクリプトの変更（Gradle6.x対応、SpotBugs対応、Gradleマルチプロジェクト解除など）  

###### Version 2.0.0(2020/4/29)
* 機能追加  
・Jenkinsプラグインを追加  
・StepCountにファイルパスの表記方法を選択できるオプション(-path)を追加  
・DiffCountにファイルパスの表記方法を選択できるオプション(-path)とファイルパスのソート順を選択できるオプション(-sort)を追加  
・上述のオプション指定をAntタスクI/FおよびSwing画面I/Fに追加  
・DiffReportの入力ファイルにDiffCountのCSV形式の出力結果を選択できるオプション(-format)を追加  
・MatchExtractの入力ファイルのファイルパス表記方法を選択できるオプション(-path)を追加  

* 機能改修  
・StepCountでソースタイプが未対応の場合のJSON,XML形式での表記が"unknown"であったのをCSV,EXCEL,TEXT形式と合わせるようにプロパティに定義した文言（未対応/UNSUPPORTED）に変更    
・DiffCountでソースタイプが未対応の場合の追加/削除行数の表記が”0”であったのをCSV,JSON,XML形式では抑止、HTML形式では"-"に変更（TEXT,EXCEL形式については"0"のまま）  
・DiffCountでソースタイプの表記を含むCSV,EXCEL,JSON,XML形式では未対応時の表記が"UNDEF"であったのをStepCountに合わせるようにプロパティに定義した文言（未対応/UNSUPPORTED）に変更  
・上述の変更によりStepCounterの出力との互換性が損なわれたが本ツールの機能拡張での整合性を優先  

* バグ修正  
・Windowsでのパス比較時に大文字小文字を無視するように修正  

* 内部的な改変  
・Gradle5.xに対応するようビルドスクリプトの修正  
・coreモジュールにMavenリポジトリ登録用のpom.xmlを追加  
・Jenkins PluginからStepCountとDiffCountの機能を呼び出すための外部I/Fを追加  
・Jenkinsのスレーブからcoreモジュールを呼び出した結果をマスターに渡すために一部のクラスにSerializableを実装  
・JenkinsなどWebApplicationからcoreモジュールを呼び出した時にExcel出力ができない問題を回避するための修正  
・Jenkins test-harnessとcoreモジュールが推移依存するdom4jの競合問題を回避するため、coreモジュールの依存ライブラリにorg.jenkins-ci.dom4j:dom4j:1.6.1-jenkins-4を追加  
・Swing画面テストコードの関数の一部をcoreモジュールのテストコードに移動  
・StepCountやDiffCountのテスト期待値ファイルの名称を変更  

###### Version 1.4.1(2018/10/6)
* バグ修正  
・CountReportとDiffReportとMatchExtractの出力において改行コードが環境依存になっていなかったバグを修正  
* 内部的な改変  
・Windowsでビルドする際にテストエラーにならないようテストコードや期待値データを修正  

###### Version 1.4.0(2018/7/9)
* 機能追加  
・DiffCountの出力形式にCSV,JSON,XMLを追加  
* 機能改修  
・テキストデータの出力において改行コードが'\n'固定だったのを環境依存の改行コードに変更  
・StepCountのCSV形式の出力においてカテゴリ名の値が'"'または','を含む場合にその値を'"'で囲むように変更  
・DiffCountのHTML形式の出力においてMETAタグのcharset属性を指定するように変更  
・Swing画面でファイル選択画面を開くときに同一ボタンで開いた直近のディレクトリをカレントディレクトリとするように変更。ただし画面起動時にデフォルトディレクトリにリセットされる  
* バグ修正  
・XML形式の出力ファイルのエンコードが環境依存であるのに、XMLタグのエンコード属性がUTF-8固定であったバグを修正  
・JSON形式の出力ファイルのエンコードが環境依存であったバグを修正（標準規格に合わせUTF-8固定）  
・Swing画面でルール定義XMLファイルを指定して計測した後、ルール定義XMLファイル指定を削除してから再計測してもデフォルトのルールに戻らないバグを修正  
* 内部的な改変  
・Gradle4.xに合わせてビルドスクリプトの修正  
・ビルドスクリプトにcoreモジュールのJarファイルをMavenローカルリポジトリに登録するタスク追加  
・テストコードにおいてリソースファイル読み込み時に環境依存のエンコードをしていたのを、UTF-8固定に変更  
・StepCountとDiffCountの機能を呼び出す外部I/Fを変更し、AntタスクI/FとSwing画面からの呼び出しを合わせて修正（このためVer.1.3.0のSwing画面から呼び出すとエラーになる）  
・keisuke.count以下, keisuke.swing以下のクラス構成を一部変更  

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
