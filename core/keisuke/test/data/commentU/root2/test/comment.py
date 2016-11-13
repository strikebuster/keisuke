#!/usr/bin/python
# coding: utf-8

# zzz コメント追加差分なし１
# define class
class Score:
    def __init__(self, name, sc1, sc2, sc3):
        self.name = name
        self.math = sc1
        self.english = sc2
        self.japanese = sc3

    def get_average(self):
        return (self.math + self.english + self.japanese)/3

# procedure1　zzzコメント内修正差分なし
taro = Score('taro',60,70,80) # taro
jiro = Score("jiro",30,40,50) # jiro
saburo = Score("saburo",0,100,20) # saburo
shiro = Score("shiro",90,100,90) # shiro 差分あり１
zzz有効行追加差分あり２
if( taro.math > 50 )
    ave = taro.get_average()
print(taro.name+'\'s Ave#'+ave+'\n')
zzz 有効行差分あり３　''' コメントアウト
ave = jiro.get_average()
print(jiro.name+'\'s Ave#'+ave+'\n')
'''
ave = saburo.get_average(zzz 4)
print(saburo.name+"'s Ave#"+ave+"\n")
""" コメントアウト
ave = shiro.get_average()
'''
print(shiro.name+"'s Ave#"+ave+"\n")
'''
zzz コメント内追加差分なし
"""　　zzz 有効行差分あり5

# end
print("===\n")
