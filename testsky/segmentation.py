import cv2
#import numpy as np
from matplotlib import pyplot as plt


class Size(object):

    def __init__(self, x, y):
        self.width = x
        self.hight = y

SHOWALL = False
DELAY_CAPTION = 1500
DELAY_BLUR = 100
MAX_KERNEL_LENGTH = 31

imgs = dict()

img = cv2.imread('Nature_Fields_Green_field_beneath_a_blue_sky_015103_.jpg',
                 0)
imgs["orginal"] = img
for x in range(8):
    x = x * 2 + 1
    imgs["GBlur"+str(x)] = cv2.GaussianBlur(img, (x, x), 0)
    ## show img 
for (name, image), i in zip(imgs.items(), range(len(imgs))):
    plt.subplot(3, 3, i+1), plt.imshow(image, 'gray')
    plt.title(name)

    cv2.namedWindow(name, cv2.CV_WINDOW_AUTOSIZE)
    cv2.imshow(name, image)
    

plt.show()
if SHOWALL:
    cv2.waitKey(0)
