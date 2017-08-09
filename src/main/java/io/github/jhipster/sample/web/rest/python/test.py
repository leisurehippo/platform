# -*- coding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf8')
del sys.path[-1]
print sys.path
import ctypes

      
if __name__ == "__main__":
    dll=ctypes.cdll.LoadLibrary(r"C:\Python27\lib\site-packages\xgboost-0.6-py2.7.egg\xgboost\xgboost.dll")
    print dll
    

