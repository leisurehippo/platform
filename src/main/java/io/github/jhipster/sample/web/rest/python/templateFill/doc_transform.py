# -*- coding: utf-8 -*- 
from docx import Document
import os
import subprocess
import docx2txt

def transform(file,outfile):
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
	if file.endswith('.doc'):
	    subprocess.call(['soffice', '--headless', 'docx', file])
	    subprocess.call(['rm', file])
	    file = file[:-4] + '.docx'
	if outfile.endswith('.docx'):
      	    doc = Document(file)
	    doc.save(outfile)
	elif outfile.endswith('.txt'):
	    f = open(outfile, 'w')
	    text = docx2txt.process(file)
	    for line in text:
		f.write(line.encode('UTF-8'))
	    f.close()
    except Exception as e:
        print(e)
        print(file)
        print(outfile)
        return 1
    return 0


def doc_transform(dirname, type='docx'):
    file_path = dirname + "/original"
    file_names = os.listdir(file_path)
    error_count = 0
    for file in file_names:
        if file[-4:]=='.doc':
            if type=='docx':
                outfile=dirname+'/docx/'+file+'x'
            elif type=='txt':
                outfile=dirname+'/newdir/'+file[:-3]+'txt'
        elif file[-5:]=='.docx':
            if type=='docx':
                outfile=dirname+'/docx/'+file
            elif type=='txt':
                outfile=dirname+'/newdir/'+file[:-4]+'txt'
        error_count+=transform(file_path+'/'+file,outfile)
    print('num of errors:{}'.format(error_count))


if __name__=='__main__':
    import sys
    print(sys.argv)
    if len(sys.argv)>2:
        print('参数数目异常!')
    else:
        if len(sys.argv)==2:
            doc_transform(sys.argv[1])
            doc_transform(sys.argv[1], type="txt")
        else:
            doc_transform()
