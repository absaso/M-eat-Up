# -*- coding: utf-8 -*-
"""ProjetMeat'up.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/10JMuCk7OnEj9XAdSQDvp5yLhgnfd12VR

##Restaurant recommendation
"""

from apscheduler.schedulers.background import BackgroundScheduler
import string
import pandas as pd
import numpy as np
import flask
from flask import request, jsonify
import requests
from sentiment_data_preparation import DataPreparationSentiment
import time
import atexit
import json

class SentimentAnalyse():

  def __init__ (self , restautDF ,restauDTO ):
    self.data = restautDF
    
    #self.scheduler = BackgroundScheduler()
    #self.app =flask.Flask(__name__)
    #self.app.config["DEBUG"] = True
    #self.route()
    self.RestaurantDTO = restauDTO
#venv-name\Scripts\activate

  #def route(self):
    #@self.app.route('/sentiment', methods=['GET'])
    #def getRestaurantSentiment( ):
  def run (self):
    a = 0
    print("debut du process")
    data = self.data.rename(columns={"restaurant_id":"id"})
    data=data.drop(["name"],axis=1)
    #restaudto= self.RestaurantDTO.drop(["sentiment"] , axis = 1)  //quand on recup data from dB
    restaudto = self.RestaurantDTO
    df_restaurants_sentiments = pd.merge(restaudto,data,  on="id", how="left")
    df_restaurants_sentiments.fillna(0,inplace=True)
    print(df_restaurants_sentiments.columns)
    print(df_restaurants_sentiments)
    for i in range (len(df_restaurants_sentiments)) :
      if df_restaurants_sentiments.loc[i,"sentiment" ]== 0 :
        sentiment = self.default_sentiment(df_restaurants_sentiments.loc[i,"rating"]) 
        df_restaurants_sentiments.loc[i,"sentiment"] = sentiment
      
      restau = "["+json.dumps(df_restaurants_sentiments.loc[i].to_dict()) +"]"
      #restau = df_restaurants_sentiments.loc[i].to_json(orient='records') 
      #print(restau) 
      headers = {'Content-Type': "application/json"}
      r = requests.put("http://localhost:8080/update" ,restau,headers=headers )

    return df_restaurants_sentiments.to_json(orient='records')

  def default_sentiment(self,rating):
    sentiment = ""
    if rating >3 :
      sentiment = "positif"
    elif rating <3 :
      sentiment == "negatif"
    else :
      sentiment == "neutre"
    return sentiment 
 
"""
  def run (self):
    self.scheduler.add_job(self.process_background, 'interval', minutes=1)
    self.scheduler.start()
    self.app.run()
  
  
  def process_background(self):
    print("update du traitement")
    data_class = DataPreparationSentiment()
    self.data = data_class.run() 
    self.RestaurantDTO =  data_class.get_RestaurantDTO()
"""

  


#if __name__ == "__main__":
#    SentimentAnalyse = SentimentAnalyse()
#    SentimentAnalyse.run()












