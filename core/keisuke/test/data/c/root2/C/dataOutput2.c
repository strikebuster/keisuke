#include "dataOutput.h"
#include <stdio.h>
 
//  �G���[���b�Z�[�W
extern int Error;
 
//  �w���f�[�^�̕\��
void showStudentData(student* data)
{
    if(data != NULL){
        printf("�w���ԍ��F%d ���O�F%s\n",data->id,data->name);
    }else{
        printf("�f�[�^���o�^����Ă��܂���B\n");
    }
}
//  �G���[�̕\��
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