// hoge1　zzz コメント内は差分に出ない確認１
/*
hoge3 */
/*
*/ HOGE5
HOGE6a /* hoge6b zzz コメント内は差分に出ない確認２
hoge7a */ HOGE7b

// hoge9a /* hoge 9b zzz差分に出ない確認３
HOGE10 zzz差分に出る確認1
/* hoge11 */
/* hoge12a // hoge12b */　zzz差分に出る確認2
HOGE13
/* hoge14 */

chome = "// HOGE16 zzz 差分に出る確認3";
chome = "HOGE17a // HOGE17b";
chome = " HOGE18 /* zzz 差分に出る確認4";
chome = "HOGE19";　zzz 差分に出る確認5
chome = " HOGE20a */ \n /* HOGE20b ";
HOGE21a // hoge21b
chome = " HOGE22a \"; /* HOGE22b zzz 差分に出る確認6";
chome = " HOGE23a \'; /* HOGE23b '";
chome = " */"; HOGE24
chome = " HOGE25a \" \\"; /* hoge25b zzz コメント内は差分に出ない確認４
hoge26a */ chome = " \' HOGE26b ; /* HOGE26c \\";
HOGE27
chome = "HOGE28 \t \n \' \" */";
