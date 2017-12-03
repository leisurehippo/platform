import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from tqdm import tqdm
import xgboost as xgb
from sklearn.linear_model import LinearRegression
from sklearn import metrics

I = pd.read_csv('data/I.txt',header = None)
I['type'] = I[0].apply(lambda x:x.split('&')[0])
I['DeviceId'] = I[0].apply(lambda x:x.split('&')[1])
I['time'] = I[0].apply(lambda x:x.split('&')[2])
I.drop([0],axis=1,inplace=True)
# 分离 年月日时分秒
I['year'] = I['time'].apply(lambda x:x[:4]).astype(int)
I['month'] = I['time'].apply(lambda x:x[4:6]).astype(int)
I['day'] = I['time'].apply(lambda x:x[6:8]).astype(int)
I['hour'] = I['time'].apply(lambda x:x[8:10]).astype(int)
I['minute'] = I['time'].apply(lambda x:x[10:12]).astype(int)
I['second'] = I['time'].apply(lambda x:x[12:14]).astype(int)
# 去掉0值
index0 = I[I[1]==0.0].index
I.drop(index0,inplace=True)
# 统一测量时间，统一到“分钟”
I['second'] = I['second'].apply(lambda x:0 if(x<=30) else 1)
I['minute'] = I['minute'] + I['second']
I = I.sort_values('time')
Id1 = I[I['DeviceId'] == '1164520']
Id1 = Id1.reset_index()
Id1.drop(['index'],axis=1,inplace=True)

# 对15号数据的处理
Id1.loc[Id1.shape[0]] = {1:329.297,'type':'pmsln','DeviceId':'1164520','time':'20170415025954515',
                           'year':2017,'month':4,'day':15,'hour':2,'minute':60,'second':1}
Id1.loc[Id1.shape[0]] = {1:309.663,'type':'pmsln','DeviceId':'1164520','time':'20170415123200515',
                           'year':2017,'month':4,'day':15,'hour':12,'minute':32,'second':0}
Id1.loc[Id1.shape[0]] = {1:329.297,'type':'pmsln','DeviceId':'1164520','time':'20170415235954515',
                           'year':2017,'month':4,'day':15,'hour':23,'minute':60,'second':1}
Id1.loc[Id1.shape[0]] = {1:324.422,'type':'pmsln','DeviceId':'1164520','time':'20170415061900515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':19,'second':0}
Id1.loc[Id1.shape[0]] = {1:337.230,'type':'pmsln','DeviceId':'1164520','time':'20170415062000515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':20,'second':0}
Id1.loc[Id1.shape[0]] = {1:329.102,'type':'pmsln','DeviceId':'1164520','time':'20170415062100515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':21,'second':0}
Id1.loc[Id1.shape[0]] = {1:320.044,'type':'pmsln','DeviceId':'1164520','time':'20170415062200515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':22,'second':0}
Id1.loc[Id1.shape[0]] = {1:332.557,'type':'pmsln','DeviceId':'1164520','time':'20170415062300515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':23,'second':0}
Id1.loc[Id1.shape[0]] = {1:329.226,'type':'pmsln','DeviceId':'1164520','time':'20170415062400515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':24,'second':0}
Id1.loc[Id1.shape[0]] = {1:330.625,'type':'pmsln','DeviceId':'1164520','time':'20170415062500515',
                           'year':2017,'month':4,'day':15,'hour':6,'minute':25,'second':0}
Id1 = Id1.sort_values('time')
Id1 = Id1.reset_index()
Id1.drop(['index'],axis=1,inplace=True)
# 对15号数据的处理
curminute = 1
curhour = 0
curday = 15
flag = 0
for i in range(1440):
    Id1.loc[i,'day'] = curday
    Id1.loc[i,'hour'] = curhour
    Id1.loc[i,'minute'] = curminute
    curminute = curminute + 1
    if curminute == 61:
        curminute = 1
    if curminute==1:
        curhour = curhour + 1

for day in range(16,27,1):
    drop = []
    for hour in range(24):
        tmp = Id1[(Id1['day']==day)&(Id1['hour']==hour)]
        # 把minute调成1-60
        if(len(tmp)==60):
            for r in range(len(tmp)):
                Id1.loc[tmp.iloc[r].name,'minute'] = r+1
        elif(len(tmp)>60):
            for r in range(60):
                Id1.loc[tmp.iloc[r].name,'minute'] = r+1
            for r in range(60,len(tmp),1):
                drop.append(tmp.iloc[r].name)
        else:
            curminute = 1
            print('tmp.shape : ',tmp.shape)
            for i in range(len(tmp)):
                if (tmp.iloc[i]['minute'] < curminute):
                    Id1.loc[tmp.iloc[i].name,'minute'] = curminute
                elif tmp.iloc[i]['minute'] > curminute:
                    for j in range(curminute,tmp.iloc[i]['minute'],1):
                        print(j)
                        data = Id1.loc[(Id1['day']==(day-1))&(Id1['hour']==hour)&(Id1['minute']==j),1].values[0]
                        print(data)
                        Id1.loc[Id1.shape[0]] = {1:data,'type':'pmsln','DeviceId':'1164520',
                                             'time':'201704'+str(day)+('%02d'%hour)+('%02d'%j)+'00515',
                                             'year':2017,'month':4,'day':day,'hour':hour,'minute':j,'second':0}
                    curminute += tmp.iloc[i]['minute'] - curminute
                curminute += 1
            if curminute<61:
                print('curminute: ',curminute)
                for k in range(curminute,61,1):
                    print('k:',k)
                    data = Id1.loc[(Id1['day']==(day-1))&(Id1['hour']==hour)&(Id1['minute']==k),1].values[0]
                    Id1.loc[Id1.shape[0]] = {1:data,'type':'pmsln','DeviceId':'1164520',
                                         'time':'201704'+str(day)+('%02d'%hour)+('%02d'%k)+'00515',
                                         'year':2017,'month':4,'day':day,'hour':hour,'minute':k,'second':0}
    if len(drop)>0:
        print('drop')
        Id1.drop(drop,inplace=True)
        Id1 = Id1.sort_values('time')
        Id1 = Id1.reset_index()
        Id1.drop(['index'],axis=1,inplace=True)

traino = Id1[(Id1['day']<27)&(Id1['day']>17)]

#todo根据相关性筛选特征
dall = []
for i in tqdm(range(len(traino))):
    target = traino.iloc[i][1]
    arr = []
    for j in range(1,1440*3+1,1):
        minute1 = Id1.loc[traino.iloc[i].name-j,1]
        arr.append(minute1)
    arr.append(target)
    dall.append(arr)
dfall = pd.DataFrame(dall)

#相关系数计算
dictc = {}
arr_coef = []
for col in range(0,4320,1):
    tmp = dfall.loc[:,[col,4320]]
    arr = np.array(tmp.T)
    corr = np.corrcoef(arr)
    dictc[col] = corr[0][1]
    arr_coef.append([col,corr[0][1]])
df_coef = pd.DataFrame(arr_coef)
df_coef = df_coef.sort_values(0)
df_coef.to_csv('df_coef.csv',index=False)

target = dfall.pop(4320)
var = np.var(dfall)
df_var = pd.DataFrame(var)
df_var.to_csv('df_var.csv')

plt.figure()
plt.plot(df_coef[0],df_coef[1],'k')
plt.xlabel("Time diff")
plt.ylabel('Correlation coefficient')
plt.show()

plt.figure()
plt.hist(df_coef['1'])
plt.xlabel('Correlation coefficient')
plt.ylabel('Frequency')
plt.plot([0.56,0.56],[0,1000],'b')

plt.show()

plt.figure()
plt.plot(var.index,var,'k')
plt.xlabel("Time diff")
plt.ylabel('Variance')
plt.show()

plt.figure()
plt.hist(var)
plt.xlabel('Variance')
plt.ylabel('Frequency')
plt.plot([3710,3710],[0,1400],'b')

plt.show()

dfall.to_csv('dfall.csv',index=False,float_format='%.3f')