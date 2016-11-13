#include "dataOutput.h"
#include <stdio.h>
 
//  エラーメッセージ
extern int Error;
 
//  学生データの表示
void showStudentData(student* data)
{
    if(data != NULL){
        printf("学生番号：%d 名前：%s\n",data->id,data->name);
    }else{
        printf("データが登録されていません。\n");
    }
}
//  エラーの表示
void showError()
{
    switch(Error){
    case MESSAGE_OK:
        printf("OK!\n");
        break;
    case MESSAGE_ERROR:
        printf("ERROR!\n");
        break;
    }
     
}