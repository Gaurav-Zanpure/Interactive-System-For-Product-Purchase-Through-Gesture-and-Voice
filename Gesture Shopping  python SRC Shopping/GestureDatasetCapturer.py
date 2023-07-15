import numpy as np
from keras.models import load_model
from keras.preprocessing import image
import cv2
import datetime
import os

typename="test"
gesturename="BUY"

datasetpath="GESTURE DATASET111"
if not os.path.exists(datasetpath):
    os.makedirs(datasetpath)         
    

typedataset="GESTURE DATASET111//"+typename
if not os.path.exists(typedataset):
    os.makedirs(typedataset)          
        

gesturepath="GESTURE DATASET111//"+typename+"//"+gesturename
if not os.path.exists(gesturepath):
    os.makedirs(gesturepath)      
    
      
cap=cv2.VideoCapture(0)
k=342
x=150
y=100
h=300
w=300
while cap.isOpened():
    _,capimg=cap.read()
    img = capimg[y:y+h, x:x+w]
      
    cv2.rectangle(capimg,(x,y),(x+w,y+h),(0,0,255),3)
    filename=str(k)   
    newfilepath=gesturepath+"//"+filename+".jpg"
    dim = (96, 96)
    img = cv2.resize(img, dim, interpolation = cv2.INTER_AREA)
        
        
    #converting from gbr to hsv color space
    img_HSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    #skin color range for hsv color space 
    HSV_mask = cv2.inRange(img_HSV, (0, 15, 0), (17,170,255)) 
    HSV_mask = cv2.morphologyEx(HSV_mask, cv2.MORPH_OPEN, np.ones((3,3), np.uint8))
    
    #converting from gbr to YCbCr color space
    img_YCrCb = cv2.cvtColor(img, cv2.COLOR_BGR2YCrCb)
    #skin color range for hsv color space 
    YCrCb_mask = cv2.inRange(img_YCrCb, (0, 135, 85), (255,180,135)) 
    YCrCb_mask = cv2.morphologyEx(YCrCb_mask, cv2.MORPH_OPEN, np.ones((3,3), np.uint8))
    
    #merge skin detection (YCbCr and hsv)
    global_mask=cv2.bitwise_and(YCrCb_mask,HSV_mask)
    global_mask=cv2.medianBlur(global_mask,3)
    global_mask = cv2.morphologyEx(global_mask, cv2.MORPH_OPEN, np.ones((4,4), np.uint8))
    
    
    HSV_result = cv2.bitwise_not(HSV_mask)
    YCrCb_result = cv2.bitwise_not(YCrCb_mask)
    global_result=cv2.bitwise_not(global_mask)
    cv2.imwrite(newfilepath,YCrCb_result)
    k=k+1
   # image=cv2.imread(newfilepath(),1)
    #res_image=cv2.resize(image,(150,150))
    #cv2.imwrite(newfilepath,res_image)
            
          
    cv2.imshow('Capture Image( Press q to quit)',capimg)
    
    if cv2.waitKey(1)==ord('q'):
        break
    
cap.release()
cv2.destroyAllWindows()