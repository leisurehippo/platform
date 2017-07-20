##coding:utf-8

from keras.applications import *
from keras.preprocessing.image import *
import h5py
import math
import gc
from keras.models import *
from keras.layers import *
import numpy as np
import pandas as pd
from sklearn.utils import shuffle

batch_size=16
train_samples=8209*5
test_samples=10593*5


def stringsplit(string):
    return string.split('.jpg')[0]
def write_gap(MODEL, image_size, lambda_func=None):
    width = image_size[0]
    height = image_size[1]
    input_tensor = Input((height, width, 3))
    x = input_tensor
    if lambda_func:
        x = Lambda(lambda_func)(x)

    base_model = MODEL(input_tensor=x, weights='imagenet', include_top=False)
    model = Model(base_model.input, GlobalAveragePooling2D()(base_model.output)) #卷积层输出的每个激活图直接求平均值，不然输出的文件会非常大，且容易过拟合
    gen = ImageDataGenerator()
    train_generator = gen.flow_from_directory(
                    "handled1",
                    image_size,
                    shuffle=False,
                    batch_size=batch_size,
                    )
    test_generator = gen.flow_from_directory(
                    "test_handled",
                    image_size,
                    shuffle=False,
                    batch_size=batch_size,
                    class_mode=None)
    # subdf = pd.DataFrame(columns=['class', 'photoid'])
    # subdf.photoid = np.array(pd.Series(test_generator.filenames))
    # subdf.photoid = subdf.photoid.apply(lambda x: stringsplit(x))
    # subdf.to_csv('D:/PycharmProjects/bddog/submission/ids1.csv', index=False)
    tmp=pd.Series(train_generator.filenames)
    # print(tmp)
    y_train = tmp.str.split('_').apply(lambda x: x[1]).astype('int64').values#训练集所属100类别从文件名中获取
    # print('y_train:',y_train)
    # print("is equal:",np.array_equal(np.array(y_train),np.array(train_generator.classes)))

    train = model.predict_generator(train_generator, math.ceil(train_samples/batch_size))
    print(math.ceil(test_samples/batch_size))
    test = model.predict_generator(test_generator, math.ceil(test_samples/batch_size))
    print(MODEL.__name__)
    print('train shape',train.shape,'label shape:',len(y_train),'test shape:',test.shape)
    with h5py.File("gap_%shandled.h5" % MODEL.__name__) as h:
        h.create_dataset("train", data=train)
        h.create_dataset("test", data=test)
        h.create_dataset("label", data=y_train)

# Xception 和 Inception V3 都需要将数据限定在 (-1, 1) 的范围内
# write_gap(VGG16, (224, 224))
# write_gap(ResNet50, (224, 224))
# write_gap(InceptionV3, (299, 299), inception_v3.preprocess_input)
write_gap(Xception, (299, 299), xception.preprocess_input)
gc.collect()
