# -*- coding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf8')
print sys.path
import cPickle as pickle
import pandas as pd
import numpy as np
import xgboost as xgb
import jieba
import re
import hashlib
import ast
import os
from sklearn.metrics import precision_score,recall_score,f1_score
from sklearn.externals import joblib
from sklearn import cross_validation
from sklearn.grid_search import GridSearchCV
from xgboost.sklearn import XGBClassifier
import matplotlib.pylab as plt
#from matplotlib.pylab import rcParams
#rcParams['figure.figsize'] = 12, 4
#rcParams['font.sans-serif'] = ['SimHei']


def strdecode(sentence):
    if not isinstance(sentence, unicode):
        try:
            sentence = sentence.decode('utf-8')
        except UnicodeDecodeError:
            sentence = sentence.decode('gbk', 'ignore')
    return sentence

def get_object(filename):
	"""
	从目标文件中获取数据
	:param filename: 数据类型的目标文件路径,string
	:return: 数据
	"""
	file=open(filename,'rb')
	result=pickle.load(file)
	file.close()
	return result

def save_object(object,filename):
	"""
	将数据存入目标文件
	:param object: 数据,all
	:param filename: 目标文件路径，string
	:return: none
	"""
	file=open(filename,'wb')
	pickle.dump(object,file,1)
	file.close()
    
def plot_n_feat_imp(model, n_features = -1):
    '''
    画 特征重要性 的图
    '''
    if n_features == -1:
        n_features = len(model.get_fscore())
        
    d = model.get_fscore()
    ss = sorted(d, key=d.get, reverse=True)
    top_names = ss[0:n_features]

    plt.figure(figsize=(15,10))
    plt.title("Feature importances")
    plt.bar(range(n_features), [d[i] for i in top_names], color="r", align="center")
    plt.xlim(-1, n_features)
    plt.xticks(range(n_features), top_names, rotation='vertical')    

def save_fea_imp(model, fea_imp_uri):
    '''
    保存模型的特征重要性
    '''
    tmp=model.get_fscore()
    tmp = sorted(tmp.items(), key=lambda d:d[1], reverse = True)
    tmp = dict(tmp)
    df_tmp = pd.DataFrame()
    df_tmp[u'fea_name'] = tmp.keys()
    df_tmp[u'fea_imp'] = tmp.values()
    df_tmp.sort_values(u'fea_imp',ascending=False,inplace=True)
    df_tmp.to_csv(fea_imp_uri, index=False, encoding = 'utf-8')

def eval_metrics(labels, preds):
    '''
    返回精确度、召回率、F1
    '''
    labels = labels.split('+')[1:]
    preds = preds.split('+')[1:]
    if len(labels) == 0:
        labels.append('other')
    if len(preds) == 0:
        preds.append('other')
    rr = (np.intersect1d(labels, preds))
    precision = np.float(len(rr)) / len(preds)
    recall = np.float(len(rr)) / len(labels)
    try:
        f1 = 2 * precision * recall / (precision + recall)
    except ZeroDivisionError:
        return (precision, recall, 0.0)
    return (precision, recall, f1) 


def generate_map(data,useful_words=[]):
	"""
		生成词特征映射词典
		:return: None
	"""
	#delete empty
#	data['tag'].fillna('other',inplace=True)
#	data.dropna(inplace=True)

	#construct word map
	word_dict = {}
	index = 0
	if len(useful_words) <= 0:
		for text in data['weibo_content']:
			try:
				words =jieba.cut(text)
				for w in words:
					if(len(w)>1 and len(w)<10 and not re.match(r'^[a-zA-Z_0-9.#]*$',w)  and w not in word_dict):
						word_dict[w] = index
						index+=1
			except:
				print 'error row!!'
	else:
		for text in data['weibo_content']:
			try:
				words =jieba.cut(text)
				for w in words:
					if(len(w)>1 and len(w)<10 and not re.match(r'^[a-zA-Z_0-9.#]*$',w)  and w not in word_dict and w in useful_words):
						word_dict[w] = index
						index+=1
			except:
				print 'error row!!'					
#	print pd.Series(word_dict)
	print('len(word_dict)',len(word_dict))
    	return word_dict
#	save_object(word_dict, word_map_uri)


def data_trans(data,index,word_dict,missing='normal',onehot=False,word2vec=False,):
	"""
	:param data: 需要转换的数据，DataFrame
	:param index: 需要转换的数据column名，string
	:param missing: 空值处理方式,normal不处理，avg取平均，del删除，onehot哑编码，string
	:param onehot: 对于数字类型是否进行哑编码，bool
	:param word2vec: 对于文本类型是否进行向量转换，bool
	:return:none
	"""

	# map word to feature matrix
	if(word2vec):
		wmap = word_dict
		col = [''] * len(wmap)
		for i in wmap:
			col[wmap[i]] = index + '-' + i
		result = []
		for text in data[index]:
			vec = np.zeros(len(wmap))
			words = jieba.cut(text)
			for w in words:
				if (w in wmap):
					vec[wmap[w]] += 1
			result.append(vec)
		result = pd.DataFrame(result,index=data.index,columns=col)
		del data[index]
		return pd.concat([data,result],axis=1)

	# handle empty data
	if(missing=='avg'):
		avg = data[index][data[index]!=0].mean()
		data.loc[data[index]==0,index]=avg

	# one hot code
	if(onehot):
		tmap = word_dict
		col = ['']*len(tmap)
		col.append(index+u'其他')
		for i in tmap:
			col[tmap[i]] = index+i
		result = []
		for i in data[index]:
			vec = np.zeros(len(tmap)+1)
			if(i in tmap):
				vec[tmap[i]] = 1
			else:
				vec[-1] = 1
			result.append(vec)
		result = pd.DataFrame(result,columns=col,index=data.index)
		del data[index]
		return pd.concat([data,result],axis=1)
    


def updata_matrix(data, word_dict):
	"""
	更新训练和测试集特征矩阵
	:return:None,实例化矩阵为train_matrix文件
	"""
	# word to feature matrix
#	data['tag'].fillna('other', inplace=True)
#	data.dropna(inplace=True,how='all')

	data = data_trans(data, 'weibo_content', word_dict, word2vec=True, )
	print 'content transform sucess!!'
	print 'generate train matrix sucess!!'
#	print data
    	return data
#	save_object(data, 'train_matrix')
    
    
    
def data_precessing(raw_data, useful_words = []):
    print('len(useful_words)',len(useful_words))
    word_dict = generate_map(raw_data,useful_words)
    return updata_matrix(raw_data, word_dict)
    


def modelfit(alg, trainX, trainY, tree_num=True, cv_folds=3, early_stopping_rounds=10):
	"""
	网格搜索调参生成最优模型
	:param alg: 原模型
	:param trainX: 训练特征
	:param trainY: 训练标签
	:param tree_num: 是否对迭代层数调优
	:param cv_folds: 交叉验证叠数
	:param early_stopping_rounds:当tree_num为True时生效，设置每隔多少轮判断推出条件
	:return: 调优后的模型
	"""
	if tree_num:
		xgb_param = alg.get_xgb_params()
		xgtrain = xgb.DMatrix(trainX, label=trainY)
		cvresult = xgb.cv(xgb_param, xgtrain, num_boost_round=alg.get_params()['n_estimators'], nfold=cv_folds,
						  metrics='auc', early_stopping_rounds=early_stopping_rounds, verbose_eval=50)
		alg.set_params(n_estimators=cvresult.shape[0])
		print 'the best n_estimators：',cvresult.shape[0]

	print 'begin grid search...'
	param_grid = {
		'max_depth': range(3, 10, 2),
		'min_child_weight': range(1, 6, 2)
	}
	gsearch1 = GridSearchCV(estimator=alg,param_grid=param_grid, scoring='accuracy', n_jobs=4, iid=False, cv=5)
	gsearch1.fit(trainX, trainY)
	print 'best_params:',gsearch1.best_params_
	print 'Auccuracy:',gsearch1.best_score_
  
	return gsearch1.best_estimator_


def gen_model(raw_data, fea_th = 0.01, fea_imp_uri = ''):
    '''
    第一次生成模型
    raw_data :   [since_id, time, weibo_content, tag(0,1)]
    fea_th      : 特征占比低于该 阈值 则移除
    fea_imp_uri : 模型的特征重要性保存的位置，长度为0则不保存
    return      : 返回训练好的模型
    '''
   
    
    print 'data processing...'
    #先筛选有用的 分词
    trainX = data_precessing(raw_data.loc[raw_data.tag == 1,:].drop(['since_id', 'time', 'tag'],axis = 1))
    fea_count = trainX.sum()/trainX.shape[0]
    fea_count = fea_count[fea_count.values >= fea_th]
    print 'useful features count =',fea_count.shape[0]
    
    trainX = data_precessing(raw_data.drop(['since_id', 'time'],axis = 1)
                            , map(lambda x:x.replace('weibo_content-',''),fea_count.index.tolist()))
    del raw_data
    trainY = trainX['tag']
    trainX.drop('tag',axis=1,inplace = True)

    
    print 'training data...'
    unbalance_ratio = (trainY.shape[0] - sum(trainY))*1.0/sum(trainY) # 处理数据不平衡
    
    xgb1 = XGBClassifier(
		 n_estimators=1000,
		 max_depth=5,
		 min_child_weight=1,
		 objective= 'binary:logistic',
		 scale_pos_weight=unbalance_ratio if unbalance_ratio>=5 else 1,
		 random_state=27)
    print 'trainX shape =',trainX.shape
    
#    print unbalance_ratio, xgb1.get_xgb_params()
    
    model = modelfit(xgb1, trainX, trainY)
    
    if len(fea_imp_uri)>0 :
        save_fea_imp(model.get_booster(), fea_imp_uri)

    model_attr = {'params':str(model.get_xgb_params())}
    model = model.get_booster() # booster 才有 增量训练 的接口
    model.set_attr(**model_attr)
    return model

    
def model_pred(raw_data, model_uri, proba_th = 0.5):   
    '''
    raw_data:[since_id,time,weibo_content]
    model_uri: 模型存储的路径
    proba_th: 预测的可能性大于该值 判别为 1
    return :  返回预测结果['since_id','time','weibo_content','tag','proba']
    '''

    if not os.path.exists(model_uri):
        raw_data['tag'] = 1
        raw_data['proba'] = 1
        print "Error: can't find model!!!"
        return raw_data[['since_id','time','weibo_content','tag','proba']]
    model = joblib.load(model_uri)
    
    fea_names = model.feature_names
    trainX = data_precessing(raw_data.copy(), map(lambda x:x.replace('weibo_content-',''),fea_names))
    print 'trainX shape =',trainX.shape
    

    # trainX = trainX.loc[:,set(list(trainX.columns)) & set(fea_names)]
    for col_name in list(set(fea_names) - set(list(trainX.columns))):
        trainX.loc[:,col_name] = 0
        
    print 'after fix :trainX shape =',trainX.shape

    print 'start predicting...'
    trainX = trainX.ix[:,fea_names]
    preds = model.predict(xgb.DMatrix(trainX))
    
    raw_data['proba'] = preds
    raw_data.loc[:,'tag'] = 0
    raw_data.loc[raw_data['proba']>proba_th,'tag'] = 1
    return raw_data[['since_id','time','weibo_content','tag','proba']]
    
def incremental_train(raw_data, model_save_uri, fea_imp_uri = ''):
    '''
    增量训练（单标签）
    raw_data : 训练数据['since_id','time','weibo_content','tag'(0,1)]
    model_save_uri : 模型存储路径
    fea_imp_uri : 模型的特征重要性保存的位置，长度为0则不保存
    return      : 返回 增量训练 后的xgb模型
    '''
    
    model = joblib.load(model_save_uri)
    fea_names = model.feature_names
    
    print 'data processing...'
    trainX = data_precessing(raw_data, map(lambda x:x.replace('weibo_content-',''),fea_names))
    del raw_data
    trainY = trainX['tag']
    trainX.drop(['since_id','time','tag'],axis=1,inplace = True)
    print 'trainX shape =',trainX.shape
    

    for col_name in list(set(fea_names) - set(list(trainX.columns))):
        trainX.loc[:,col_name] = 0
    print 'after fix :trainX shape =',trainX.shape
    
    print 'start training...'
    trainX = trainX.ix[:,fea_names]
    d_train = xgb.DMatrix(trainX, trainY)    
    saved_params = ast.literal_eval(model.attr('params'))
    unbalance_ratio = (trainY.shape[0] - sum(trainY))*1.0/sum(trainY) # 处理数据不平衡    
    saved_params['scale_pos_weight'] = unbalance_ratio if unbalance_ratio >=5 else 1
#    print unbalance_ratio, saved_params
    model = xgb.train(saved_params, d_train, xgb_model = model,
                    num_boost_round = saved_params['n_estimators'])
    
    if len(fea_imp_uri)>0 :
        save_fea_imp(model.get_booster(), fea_imp_uri)    
    return model

#==============================================================================
# 调用下面三个接口：增量训练、生成模型、模型预测
#==============================================================================

def multi_tag_incremental_train(raw_data_uri, model_save_uri,train_label, fea_imp_uri = '', filter_words = []):
    '''
    增量训练
    raw_data : 训练数据['since_id','time','weibo_content','tag'(多个标签)]
    model_save_uri : 模型存储路径，精确到 文件夹
    train_label:string 表示要训练的label名称
    fea_imp_uri : 模型的特征重要性保存的位置，长度为0则不保存
    filter_words : 过滤掉weibo_content中的词
    return      : 返回 增量训练 后的xgb模型
    '''   
#    #just test
#    model_save_uri= u'./xgb_model/'
#    raw_data_uri = u'../data/api_test_data.txt'
    
    
    print 'data processing...'
    raw_data = pd.read_table(raw_data_uri,sep = '\t',header = None, names = ['since_id','time','weibo_content','tag'])
    raw_data = raw_data.loc[~raw_data[u'weibo_content'].isnull(),:]
    raw_data.reset_index(drop = True,inplace = True)
    if raw_data['tag'].dtype != 'O':
        # '+123456' pandas 读进来时变成了 123456，这里转为 '+123456'        
        raw_data['tag'] = raw_data['tag'].apply(lambda x: '+' + str(x).strip())
    else:
        raw_data['tag'] = raw_data['tag'].apply(lambda x: x.strip())
    
    for words in filter_words:
        fix_index = raw_data.loc[raw_data.weibo_content.str.find(words) != -1, u'weibo_content'].index
        raw_data.loc[fix_index, u'weibo_content'] = raw_data.loc[fix_index,u'weibo_content'].apply(lambda x: x.replace(words,''))
    
    print 'raw_data shape =', raw_data.shape
    train_tag=[]
    train_label=strdecode(train_label)
    all_tag=raw_data.tag.unique().tolist()
    if train_label=='+all':
        train_tag=all_tag
    elif train_label in all_tag:
        train_tag.append(train_label)
    for crt_tag in train_tag:
        crt_tag = crt_tag[1:]
        crt_data = raw_data.copy()
        crt_data.loc[:,'crt_tag'] = 0
        crt_data.loc[raw_data.tag.str[1:] == crt_tag,'crt_tag'] = 1
        postive_ids = crt_data.loc[crt_data.crt_tag == 1,'since_id'].tolist()
        #去重
        crt_data = crt_data.loc[~((crt_data['crt_tag'] == 0) & (raw_data['since_id'].isin(postive_ids))),:]
        del crt_data['tag']
        crt_data.rename_axis({'crt_tag':'tag'},axis=1,inplace=True)
        print crt_tag,'shape =',crt_data.shape
      
        crt_model = incremental_train(crt_data, model_save_uri+u'xgb_'+ hashlib.md5(crt_tag.decode('utf-8')).hexdigest())
        print 'update model:',crt_tag,':',hashlib.md5(crt_tag).hexdigest()
        save_object(crt_model, model_save_uri+u'xgb_'+ hashlib.md5(crt_tag).hexdigest())
        
        if len(fea_imp_uri) >0:
            save_fea_imp(crt_model, fea_imp_uri+u'fea_imp_'+hashlib.md5(crt_tag).hexdigest()+'.csv')





    
def gen_multi_model(raw_data_uri, model_save_uri,train_label, fea_th=0.01, fea_imp_uri = '', filter_words = []):
    '''
    第一次生成模型
    raw_data :   [since_id, time, weibo_content, tag(多个标签)]
    model_save_uri:模型保存的路径，精确到 文件夹，如 /home/tmp/
    train_label:string 表示要训练的label名称
    fea_th      : 特征出现频率低于该 阈值 则移除
    fea_imp_uri : 模型的特征重要性保存的位置，长度为0则不保存,精确到文件夹，如 /home/tmp/
    '''
    
    #just test
#    model_save_uri= u'./xgb_model/'
#    raw_data_uri = u'../data/api_test_data.txt'
#    fea_imp_uri = u'../data/tmp.csv'
    
    raw_data = pd.read_table(raw_data_uri,sep = '\t',header = None, names = ['since_id','time','weibo_content','tag'])
    raw_data = raw_data.loc[~raw_data[u'weibo_content'].isnull(),:]
    raw_data.reset_index(drop = True,inplace = True)
    if raw_data['tag'].dtype != 'O':
        # '+123456' pandas 读进来时变成了 123456，这里转为 '+123456'        
        raw_data['tag'] = raw_data['tag'].apply(lambda x: '+' + str(x).strip())
    else:
        raw_data['tag'] = raw_data['tag'].apply(lambda x: x.strip())
        
    
    for words in filter_words:
        fix_index = raw_data.loc[raw_data.weibo_content.str.find(words) != -1, u'weibo_content'].index
        raw_data.loc[fix_index, u'weibo_content'] = raw_data.loc[fix_index,u'weibo_content'].apply(lambda x: x.replace(words,''))
    
    print 'raw_data shape =', raw_data.shape
    train_tag=[]
    train_label=strdecode(train_label)
    all_tag=raw_data.tag.unique().tolist()
    if train_label=='+all':
        train_tag=all_tag
    elif train_label in all_tag:
        train_tag.append(train_label)
    print train_tag
    for crt_tag in train_tag:
        crt_tag = crt_tag[1:]
        crt_data = raw_data.copy()
        crt_data.loc[:,'crt_tag'] = 0
        crt_data.loc[raw_data.tag.str[1:] == crt_tag,'crt_tag'] = 1
        postive_ids = crt_data.loc[crt_data.crt_tag == 1,'since_id'].tolist()
        #去重
        crt_data = crt_data.loc[~((crt_data['crt_tag'] == 0) & (raw_data['since_id'].isin(postive_ids))),:]
        del crt_data['tag']
        crt_data.rename_axis({'crt_tag':'tag'},axis=1,inplace=True)
        print crt_tag,'shape =',crt_data.shape
        
        if len(fea_imp_uri) >0:
            crt_model = gen_model(crt_data, fea_th, fea_imp_uri+u'fea_imp_'+hashlib.md5(crt_tag).hexdigest()+'.csv')
        else:
            crt_model = gen_model(crt_data, fea_th)
            
        print 'saving model',crt_tag,':',hashlib.md5(crt_tag).hexdigest()
        save_object(crt_model, model_save_uri+u'xgb_'+ hashlib.md5(crt_tag).hexdigest())

class TagException(Exception):
    pass
      
def data_pred(raw_data_uri, model_uri, rst_save_uri, proba_th=0.5, filter_words = []):
    '''
    模型预测，只预测值为1的数据，格式为['since_id','time','weibo_content']，txt文件
    raw_data :   [since_id, time, weibo_content, tag(一个标签)]
    model_uri:   模型保存的路径，精确到 文件夹
    rst_save_uri  : 所要保存结果的路径
    proba_th      : 特征占比低于该 阈值 为0,否则为1
    filter_words  : 过滤掉weibo_content中的词
    '''
    
#    #just test
#    raw_data_uri = u'../data/temp_hkEVi6zRYKUvW-rlpVNMFaqeYTmw1_ZyTYMZ_JaW.txt'
#    model_uri= u'./xgb_model/'
#    rst_save_uri = u'../data/tmp/test.txt'
    
    raw_data = pd.read_table(raw_data_uri, sep = '\t',header = None, names = ['since_id','time','weibo_content','tag'])
    raw_data = raw_data.loc[~raw_data[u'weibo_content'].isnull(),:]
    raw_data.reset_index(drop = True,inplace = True)

    if raw_data['tag'].dtype != 'O':
        # '+123456' pandas 读进来时变成了 123456，这里转为 '+123456'
        raw_data['tag'] = raw_data['tag'].apply(lambda x: '+' + str(x).strip())
    else:
        raw_data['tag'] = raw_data['tag'].apply(lambda x: x.strip())

    for words in filter_words:
        fix_index = raw_data.loc[raw_data.weibo_content.str.find(words) != -1, u'weibo_content'].index
        raw_data.loc[fix_index, u'weibo_content'] = raw_data.loc[fix_index,u'weibo_content'].apply(lambda x: x.replace(words,''))
    
    print 'raw_data shape =', raw_data.shape
    if len(raw_data.tag.unique().tolist()) > 1:
        raise TagException('tag number more than one')
    crt_tag=""
    if raw_data.tag.unique().tolist():    
        crt_tag = raw_data.tag.unique().tolist()[0][1:].decode("utf8")
    raw_data.drop('tag', axis=1, inplace = True)

    print crt_tag,'shape =',raw_data.shape
    url= model_uri+u'xgb_'+ hashlib.md5(crt_tag).hexdigest()    
    rst = model_pred(raw_data, url , proba_th)
    rst.loc[rst['tag'] == 1,['since_id','time','weibo_content']].to_csv(rst_save_uri, header=None, index=None, sep='\t', mode='w', encoding='utf-8')
            
      
if __name__ == "__main__":
#     pass
     import os
     print os.getcwd()
     _type = sys.argv[1].strip()
     if _type == 'predict':
         data_pred(sys.argv[2].strip(),sys.argv[3].strip(),sys.argv[4].strip())
     elif _type=='update':
         multi_tag_incremental_train(sys.argv[2].strip(),sys.argv[3].strip(),sys.argv[4].strip())
     elif _type=="retrain":
         gen_multi_model(sys.argv[2].strip(),sys.argv[3].strip(),sys.argv[4].strip())

