import numpy as np
import argparse
import matplotlib.pyplot as plt
import cv2
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, Flatten
from tensorflow.keras.layers import Conv2D
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.layers import MaxPooling2D
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import os
import operator
import collections
from keras.models import load_model
import time

import datetime
import mysql.connector



os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

model = Sequential()
model.add(Conv2D(32, kernel_size=(3, 3), activation='relu', input_shape=(96,96,1)))
model.add(Conv2D(64, kernel_size=(3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))
    
model.add(Conv2D(128, kernel_size=(3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Conv2D(128, kernel_size=(3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))
    
model.add(Flatten())
model.add(Dense(1024, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(6, activation='softmax'))
model.load_weights('gesture trained model.h5')
    
    
        # prevents openCL usage and unnecessary logging messages
cv2.ocl.setUseOpenCL(False)
    
        # dictionary which assigns each label an emotion (alphabetical order)
gesture_dict = {0:"BACK",1:"BLANK", 2: "BUY", 3: "MORE INFO",4: "NEXT",5: "PREVIOUS"}
    
        # start the webcam feed
cap = cv2.VideoCapture(0)
gesturelist=[]
x=150
y=100
h=300
w=300

mydb = mysql.connector.connect( host="localhost", user="root",  passwd="root",  database="gesturedb")

mycursor = mydb.cursor()

# gid="1"

# sql = "INSERT INTO gestureinfo (gid,gesturename) VALUES (%s,%s)"
# val = (gid,"BLANK")


# mycursor.execute(sql, val)
# mydb.commit()
while cap.isOpened():
    _,capimg=cap.read()
    img = capimg[y:y+h, x:x+w]
      
    cv2.rectangle(capimg,(x,y),(x+w,y+h),(0,0,255),3)
    newfilepath="TEMP.jpg"
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
    cropped_img = np.expand_dims(np.expand_dims(cv2.resize(YCrCb_result, (96, 96)), -1), 0)
 
    prediction = model.predict(cropped_img)
    maxindex = int(np.argmax(prediction))
    Gesturename=gesture_dict[maxindex]
    print("IG : ",Gesturename)
    cv2.putText(capimg, Gesturename, (x+20, y-60), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)
    gesturelist.append(Gesturename)
    frequency = collections.Counter(gesturelist)
    geturefreq=dict(frequency)
    sorted_d = sorted(geturefreq.items(), key=operator.itemgetter(1))
    print('Dictionary in ascending order by value : ',sorted_d)
    index=len(sorted_d)-1
    mxvaluegesture=sorted_d[index]
   # print("Matched ",mxvaluegesture)
    finalname=mxvaluegesture[0]
    gesturecouunt=mxvaluegesture[1]
  #  print("finalname ",finalname)
    print("gesturecouunt ",gesturecouunt)
    count=int(gesturecouunt)
    if(count>=50):
        print("Detected Gesture Name is  ",finalname)
        gesturelist.clear()
        if(finalname !="BLANK"):
            
            gid1="1"
            sql="UPDATE gestureinfo SET gesturename='"+finalname+"' where gid='"+gid1+"' ";
            mycursor.execute(sql)
           # sql = "INSERT INTO gestureinfo (gid,gesturename) VALUES (%s,%s)"
           # val = (gid,finalname)

            #mycursor.execute(sql, val)
            mydb.commit()

            print(mycursor.rowcount, "record inserted.")
              
            
        
        
  
          
    cv2.imshow('Capture Image( Press q to quit)',capimg)
    
    if cv2.waitKey(1)==ord('q'):
        break
    
cap.release()
cv2.destroyAllWindows()