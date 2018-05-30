
from docx import Document
from win32com import client as wc
import os

def test():
	doc=Document('./1-输电线路（74+46+103）224/111-济南-220kV黄济线046-047故障分析报告-重合不成-外力破坏-异物短路-20160910.docx')
	t=doc.tables[1]
	for r in t.rows:
		for c in r.cells:
			print(c.text)
		print('---->')
	#print len(t.rows),len(t.columns),t.rows[0].cells[-1].text,t.rows[2].cells[-1].text

def transfrom(word,file,outfile,data_type=16):
	'''
	word=wc.Dispatch("Word.Application")
	file:输入doc文件的绝对路径
	outfile:输出docx文件的绝对路径
	0表示失败，1表示成功
	'''
	if file[0]=='.' or (file[-4:]!='.doc' and file[-5:]!='.docx'):
		print(file+'\n文件名异常!')
		return 0
	try:
		doc=word.Documents.Open(file)
		doc.SaveAs(outfile,data_type)
		doc.Close()
	except Exception as e:
		print(e)
		print(file)
		print(outfile)
		return 1
	return 0
		

def doc_transform(type='docx'):
	dirname=r'E:\hubo\电网文本\山东电力发送网研-new\山东电力发送网研\所有案例.rar\所有案例/'
	word=wc.Dispatch("Word.Application")
	data_type=16
	if type=='txt':
		data_type=4
	for _p,dirnames,_f in os.walk(dirname,topdown=False):
		pass
	error_count=0
	for _dir in dirnames:
		print(_dir)
		for _p,_d,filenames in os.walk(dirname+_dir,topdown=False):
			pass
		for file in filenames:
			if file[-4:]=='.doc':
				if type=='docx':
					outfile=dirname+_dir+'/docx/'+file+'x'
				elif type=='txt':
					outfile=dirname+_dir+'/newdir/'+file[:-3]+'txt'
			elif file[-5:]=='.docx':
				if type=='docx':
					outfile=dirname+_dir+'/docx/'+file
				elif type=='txt':
					outfile=dirname+_dir+'/newdir/'+file[:-4]+'txt'
			error_count+=transfrom(word,dirname+_dir+'/'+file,outfile,data_type)
	word.Quit()
	print('doc转换失败数量:'+error_count)


if __name__=='__main__':
	import sys
	if len(sys.argv)>2:
		print('参数数目异常!')
	else:
		if len(sys.argv)==2:
			doc_transform(sys.argv[1])
		else:
			doc_transform()
	
	
