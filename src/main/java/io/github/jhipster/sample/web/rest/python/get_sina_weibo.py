#-*- coding:utf-8 -*-
import time
import MySQLdb
import urllib
import HTMLParser
from splinter import Browser
from pyquery import PyQuery as pq
import time
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

# db = MySQLdb.connect("10.109.247.63","root","hadoop","weibo_new")
# cursor = db.cursor()
# db.set_character_set('utf8')
# cursor.execute('SET NAMES utf8;')
# cursor.execute('SET CHARACTER SET utf8;')
# cursor.execute('SET character_set_connection=utf8;')

html_parser=HTMLParser.HTMLParser()

class microblog:
    def __init__(self):
        self.since_id = ''
        self.keyword = ''
        self.content = ''
        self.author = ''
        self.weibo_time = ''

def save_mb(m):
    # d = {
    #     'since_id': m.since_id,
    #     'keyword': m.keyword,
    #     'weibo_content': m.content,
    #     'weibo_author': m.author,
    #     'weibo_time': m.weibo_time
    # }
    # sql = 'INSERT INTO cuitest5(since_id,weibo_content,weibo_author,weibo_time,keyword) VALUES('
    # sql += "'" + d['since_id'] + "','" + d['weibo_content'] + "','" + d['weibo_author'] + "','" + d['weibo_time'] +"','"+d['keyword']+"')"
    # try:
    #     cursor.execute(sql)
    # except:
    #     print "insert"+d['since_id']+"fail."

    print m.content.encode('gbk',errors='ignore')


def insurance_get(save_path,keywords):
    key = keywords.replace('+',' ').decode('gbk').encode('utf-8')

    file=open(save_path,'w')
    usr = 'jadfi_040@sina.cn'
    pwd = 'bupt123456'
    browser = Browser('chrome')
    browser.visit('http://s.weibo.com')
    time.sleep(1)
    btns = browser.find_by_css('.gn_login')
    btn_login = btns[0].find_by_tag('a')[1]
    btn_login.click()
    time.sleep(1)
    browser.fill('username', usr)
    browser.fill('password', pwd)
    time.sleep(0.5)
    btn_login = browser.find_by_css('.item_btn')
    btn_login[0].find_by_tag('a')[0].click()
    time.sleep(1)
    #keyword =['综述 银行'] #['早盘综述','收盘综述','大盘综述','综述 黄金','综述 白银','综述 石油','综述 原油','综述 人民币','综述 欧元','综述 银行']
    q1 = urllib.quote(key)
    q2 = urllib.quote(q1)
    url_root = 'http://s.weibo.com/weibo/'+q2
# browser.find_by_css('.searchInp_form')[0].fill(u'尾盘综述')
# url_root = 'http://s.weibo.com/weibo/%25E9%2593%25B6%25E8%25A1%258C%25E6%259D%25BF%25E5%259D%2597'
# browser.find_by_css('.searchBtn')[0].click()
    browser.visit(url_root)
    page=browser.html
    dom = pq(html_parser.unescape(page))
    pages= list(dom(".W_scroll li").items())
    pagenum=1
    if pages:
        pagenum=pages[-1] .text()[1:-1]
    print 'pagenum:',pagenum
    for i in range(1,int(pagenum)):
        url = url_root + '&page=%d'%(i)
        browser.visit(url)
        expends = browser.find_by_css('.WB_text_opt')
        for exp in expends:
            try:
                exp.click()
                time.sleep(0.5)
            except:
                pass
        page = browser.html
        dom = pq(html_parser.unescape(page))
        blogs = dom(".WB_cardwrap")
        for each in blogs:
            mb = microblog()
            mb.keyword = key
            d = pq(each)
            if not d.has_class('clearfix'):
                continue
            d('.search_title_as').remove()
            for divs in d('div'):
                mb.since_id = pq(divs).attr('mid')
                if mb.since_id != None:
                    break
            if mb.since_id == None:
                continue
            mb.author = d('.comment_txt').attr('nick-name')
            content_list=list(d('.comment_txt').items())
            if content_list:
                mb.content = content_list[-1].text().replace(' ','').replace('\n','').replace('\t',' ')

            # print mb.content
            mb.weibo_time = d('.feed_from a').attr('title') + ':00'
            # print mb.weibo_time
            #save_mb(mb)
            file.write(mb.since_id.strip()+'\t'+mb.content.strip()+'\t'+mb.weibo_time.strip()+'\t'+mb.keyword.strip()+'\t'+mb.author.strip()+'\n')
    file.close()

if __name__ == '__main__':
    save_path=sys.argv[1].strip()
    keywords = sys.argv[2].strip()
    start=time.time()
    insurance_get(save_path,keywords)
    print (time.time()-start)