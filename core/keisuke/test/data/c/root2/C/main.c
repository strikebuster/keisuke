#include <stdio.h>
#include "studentDatabase.h"
#include "dataOutput.h"
 
void main(){
    int i;
    char names[][LENGTH] = {"�R�c���Y����","���c���q�q","��R���Y","�R�����₩"};
    int ids[] = { 1,2,2,3 };
    initDatabase();
    for(i = 0; i < 4; i++){  //  �f�[�^�̓o�^
        add(ids[i],names[i]);
        printf("�o�^�F%d %s\n",ids[i],names[i]);
        showError();
    }
    for(i = 0; i < 3; i++){  //  �o�^�����f�[�^�̏o��
        showStudentData(get(i+1));
    }
    for(i = 0; i < 3; i++){  //  �o�^�����f�[�^�̏o�͂������
        showStudentData(get(i+1));
    }
}