#include <stdio.h>
#include "studentDatabase.h"
#include "dataOutput.h"
 
void main(){
    int i;
    char names[][LENGTH] = {"山田太郎さん","太田美智子","大山次郎","山口さやか"};
    int ids[] = { 1,2,2,3 };
    initDatabase();
    for(i = 0; i < 4; i++){  //  データの登録
        add(ids[i],names[i]);
        printf("登録：%d %s\n",ids[i],names[i]);
        showError();
    }
    for(i = 0; i < 3; i++){  //  登録したデータの出力
        showStudentData(get(i+1));
    }
    for(i = 0; i < 3; i++){  //  登録したデータの出力おかわり
        showStudentData(get(i+1));
    }
}