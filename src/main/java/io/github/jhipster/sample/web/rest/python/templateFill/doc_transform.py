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


def doc_transform(dirname, type='docx'):
    word=wc.Dispatch("Word.Application")
    data_type=16
    if type=='txt':
        data_type=4
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
        error_count+=transfrom(word,file_path+'/'+file,outfile,data_type)
    word.Quit()
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
