import cv2
import numpy as np
from matplotlib import pyplot as plt
import collections


class Size(object):

    def __init__(self, x, y):
        self.width = x
        self.hight = y

SHOWALL = True
DELAY_CAPTION = 1500
DELAY_BLUR = 100
MAX_KERNEL_LENGTH = 31

imgs = collections.OrderedDict()

img = cv2.imread('test.png',
                 0)
imgs["orginal"] = img

imgs["GBlur" + str(7)] = cv2.GaussianBlur(img, (7, 7), 0)
imgs["GBlur" + str(9)] = cv2.GaussianBlur(img, (9, 9), 0)

for name, image in imgs.items():
    Z = image.reshape((-1, 3))
    # convert to np.float32
    Z = np.float32(Z)
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    ret, label, center = cv2.kmeans(Z, 3, criteria, 10,
                                    cv2.KMEANS_RANDOM_CENTERS)

# Now convert back into uint8, and make original image
    center = np.uint8(center)
    res = center[label.flatten()]
    res2 = res.reshape((img.shape))
    imgs[name + "k-means"] = res2


    ## show img 
for (name, image), i in zip(imgs.items(), range(len(imgs))):
    plt.subplot(3, 3, i + 1), plt.imshow(image, 'gray')
    plt.title(name)

    cv2.namedWindow(name, cv2.CV_WINDOW_AUTOSIZE)
    cv2.imshow(name, image)
    

plt.show()
if SHOWALL:
    cv2.waitKey(0)
