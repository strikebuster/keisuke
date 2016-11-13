#ifndef _STUDENT_DATABASE_H_
#define _STUDENT_DATABASE_H_
 
//  �f�[�^�x�[�X�ɓo�^�ł���w���̍ő吔
#define MAX_STUDENT 10
//  �w���̖��O�̍ő�̒���
#define LENGTH      50
 
//  �G���[���b�Z�[�W
enum ERROR{
    MESSAGE_OK,
    MESSAGE_ERROR
};
 
//  �w���̃f�[�^
typedef struct{
    int id;             //  �w���ԍ�
    char name[LENGTH];  //  ���O
}student;
 
//  �f�[�^�x�[�X�̏�����
void initDatabase();
//  �f�[�^�x�[�X�ւ̃f�[�^�̓o�^�i�w���ԍ��A���O�j
int add(int,char*);
//  �w���̃f�[�^�̎擾
student* get(int);
 
 
#endif //  _STUDENT_DATABASE_H_