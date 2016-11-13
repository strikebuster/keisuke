#include "studentDatabase.h"
#include <string.h>
 
#define MESSAGE_LENGTH  256
 
//  �f�[�^�x�[�X�ɓo�^����Ă���w���̐�
static int num = 0;
//  �w���̃f�[�^�x�[�X
static student student_database[MAX_STUDENT];
//  �G���[���b�Z�[�W
int Error = MESSAGE_OK;
 
//  �f�[�^�x�[�X�̏�����
void initDatabase()
{
    int i;
    for(i = 0; i < MAX_STUDENT; i++){
        student_database[i].id = -1;
        strcpy(student_database[i].name,"");
    }
    Error = MESSAGE_OK; //  �G���[���b�Z�[�W�̃N���A
    num = 0;    //  �o�^���ꂽ�w���̐���0�ɏ�����
}
 
//  �f�[�^�x�[�X�ւ̃f�[�^�̓o�^�i�w���ԍ��A���O�j
int add(int id,char* name)
{
    //  ���łɓo�^����Ă���id�ł���΁A�o�^���Ȃ��B
    if(get(id) == NULL && num < MAX_STUDENT){
        student_database[num].id = id;
        strcpy(student_database[num].name,name);
        num++;
        Error = MESSAGE_OK;
        //  �o�^�ł�����A1��Ԃ��B
        return 1;
    }
    Error = MESSAGE_ERROR;
    //  �o�^�ł��Ȃ���΁A0��Ԃ��B
    return 0;
}
//  �w���̃f�[�^�̎擾
student* get(int id)
{
    int i;
    for(i = 0; i < num ; i++){
        if(student_database[i].id == id){   //  �Y������id�̃f�[�^������������A  
            return &student_database[i];    //  �|�C���^��Ԃ�
        }
    }
    return NULL;
}