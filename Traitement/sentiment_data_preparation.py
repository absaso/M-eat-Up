import string
import pandas as pd
import numpy as np
import requests
import nltk
from sklearn.metrics import jaccard_score
nltk.download('vader_lexicon')
nltk.download('stopwords')
nltk.download('punkt')
from nltk.corpus import stopwords
from nltk.sentiment.vader import SentimentIntensityAnalyzer
from nltk.tokenize import sent_tokenize, RegexpTokenizer


import json

import requests

import time
from RestoModel import RestoDAO




class DataPreparationSentiment() :
  
    def __init__(self):
      self.URL_rest = "http://localhost:8080/Restaurants/offset/"
      self.URL_reviews ="http://localhost:8080/Reviews"

      self.RestaurantDTO = None

    def run (self) :
      #restaurants_df = self.get_dataset_offset (self.URL_rest)
      restaurants_df= self.get_dataset (list_de_restaurents)
      
      #restaurants_df = pd.read_csv("restaurents.csv")
      reviews_data= pd.read_csv("All_reviews.csv")
      
      self.RestaurantDTO = restaurants_df
      reviews_df = reviews_data.drop(reviews_data.columns[0],axis = 1) #on drop la colone "id" géneré par spring qui ne nous sert pas 
      reviews_df.drop_duplicates(inplace= True)
      reviews_df.dropna(inplace= True) 
      reviews_df = self.preprocessing(reviews_df)
      reviews_df = self.calcul_sentiment(reviews_df) 
      reviews_df['sentiment'] = reviews_df['compound'].apply(lambda val: self.polarity_score(val))
      dataFrame_connexion = self.reviews_restName_connexion(reviews_df,restaurants_df)
      df_sentiment = self.df_reduction (dataFrame_connexion)
      print(df_sentiment.head())
      print("preparation ok")
      return df_sentiment

  
    def get_dataset_offset (self,URL) :
      response_df = pd.DataFrame()
      for i in range (0,901,50):
      #for i in range (0,7,2):
        response = requests.get(URL+str(i))
        response_json = response.json()
        print(type(response_json))
        print("next iteration")
        response_df_1 = pd.DataFrame(response_json)
        response_df = response_df.append(response_df_1)
      print("Shape du dataframe restaurant:" ,response_df.shape)
      return response_df

    def get_dataset (self,liste) :
        #response = requests.get(url = URL)
        print(liste)
        response_json = liste.to_dict()
        response_df = pd.DataFrame(response_json)
        print("shape des reviews" ,response_df.shape )

        return response_df

    def preprocessing (self,dataReviews):
      dataReviews["review"]=dataReviews['review'].apply(lambda txt : txt.lower() )
      stop_words_english =stopwords.words('english')
      stop_words_french =stopwords.words('french')
      dataReviews['review'] = dataReviews['review'].apply(lambda txt: ' '.join(
          [word for word in txt.split() if word not in stop_words_english]))
      dataReviews['review'] = dataReviews['review'].apply(lambda txt: ' '.join(
          [word for word in txt.split() if word not in stop_words_french]))
      #dataReviews['review'] = dataReviews['review'].apply(lambda txt: sent_tokenize(txt))
      #dataReviews['review'] = dataReviews['review'].apply(lambda txt: ' '.join(txt))
      return dataReviews
    
    def get_RestaurantDTO(self):
      return self.RestaurantDTO

    
    def calcul_sentiment(self,dataReviews):
      sia = SentimentIntensityAnalyzer()
      dataReviews['score'] = dataReviews['review'].apply(lambda txt: sia.polarity_scores(txt))

      dataReviews['compound'] = dataReviews['score'].apply(lambda txt: txt['compound'])
      return dataReviews
        
    def polarity_score(self,compound):
        if compound > 0.05:
            return "positif"
        elif compound < -0.5:
            return "negative"
        else:
            return "neutral"

    def reviews_restName_connexion(self,dataReviews ,rest):
      dataFrame_connexion  =  pd.DataFrame()
      for i in range (len(rest)):
  
        dataReviews = dataReviews.reset_index(drop=True)
        rest = rest.reset_index(drop=True)
        dataFrame = dataReviews.loc[dataReviews['restaurent_id']==rest['id'][i]] 
        dataFrame["rest_name"]= rest['name'][i]   
        dataFrame_connexion= dataFrame_connexion.append(dataFrame)
      return dataFrame_connexion
    
    def df_reduction (self,data):
      groups = data.groupby('rest_name')
      final = pd.DataFrame(columns = ["name","sentiment"])
      i = 0
      for name, df_name in groups:
        p = df_name['sentiment'].value_counts()
        g= pd.DataFrame()
        g["restaurant_id"] = df_name["restaurent_id"]
        g["name"]= name
        g["sentiment"] = p.idxmax()
        #g= pd.DataFrame(d)
        final = final.append(g)
        i+=1
      df_sentiment = final.drop_duplicates()
      return df_sentiment

if __name__ == "__main__":
    preprocessing = DataPreparationSentiment()
    preprocessing.run()


















