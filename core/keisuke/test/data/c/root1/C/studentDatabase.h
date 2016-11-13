#ifndef _STUDENT_DATABASE_H_
#define _STUDENT_DATABASE_H_
 
//  データベースに登録できる学生の最大数
#define MAX_STUDENT 10
//  学生の名前の最大の長さ
#define LENGTH      50
 
//  エラーメッセージ
enum ERROR{
    MESSAGE_OK,
    MESSAGE_ERROR
};
 
//  学生のデータ
typedef struct{
    int id;             //  学生番号
    char name[LENGTH];  //  名前
}student;
 
//  データベースの初期化
void initDatabase();
//  データベースへのデータの登録（学生番号、名前）
int add(int,char*);
//  学生のデータの取得
student* get(int);
 
 
#endif //  _STUDENT_DATABASE_H_