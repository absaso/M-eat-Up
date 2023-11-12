import os
import json
import time, sys
import pandas as pd
import numpy as np
from pandas import concat
import requests
from recommendation_data_preparation import DataPreparation
import flask
from flask import request, jsonify
from apscheduler.schedulers.background import BackgroundScheduler
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier


class classifierRecommandation() :
    def __init__(self,data):
        #self.URL = "http://localhost:8080/Restaurants"
        self.historiques = None
        self.URL = "restaurents.csv"
        self.restaurant_categories_joined = data
        #self.route()

    #def route(self):
        #@self.app.route('/classifierRecommendation', methods=['POST'])
    def predict_Restaurants_One_User(self):
        user = request.json
        self.historiques = user.get("historique_id")
        historique_data_user = self.get_historique_data(user)
        y_pred, X_test = self.classification_Random_Forest(user)
        X_test['Like']= y_pred
        test_data = self.get_test_data(user)
        merged_df = pd.merge(test_data,X_test, left_index=True, right_index=True, how='inner')
        merged_df = merged_df.drop_duplicates()
        df_to_recommend = merged_df[merged_df['Like']==1]
        df_to_recommend = df_to_recommend.sort_values(by='rating', ascending=False)
        df_to_recommend = df_to_recommend.reset_index()
        df_to_recommend = df_to_recommend.rename(columns={'index': 'id'})
        restaurants_to_recommend = df_to_recommend[['id','name','rating','alias','image_url','phone','price','review_count','x','y','url']].drop_duplicates()
        """
        for i in range(len(restaurants_to_recommend)):
            restau = restaurants_to_recommend.loc[i]
            print(restau)
            headers = {'Content-Type': "application/'text/plain'"}
            r = requests.put("http://localhost:8080/classification" + str(user.get("id")) ,restau['id'],headers=headers )
        """
        print("Classifier recommendation done")
        print(restaurants_to_recommend)
        response = restaurants_to_recommend.to_json(orient='records')
        return response
            
    


    # remplacer la génération aléatoire de l'historique de l'utilisateur par les données de la BDD
    def get_historique_data(self,user):
            data = user
            user_id = [data["id"]]
            user_name = [data["name"]]
            user_historique_name = data["historique_name"]
            user_historique_liked = data["liked_restaurants"]
            user_historique_like = []
            for element in user_historique_name:
                if element in user_historique_liked:
                    user_historique_like.append(1)
                else :
                    user_historique_like.append(0)
            for user_id in user_id:
                user_df = pd.DataFrame({'user_id': [user_id], 'user_name': user_name})
                # Repeat the user ID and age values 10 times
                user_df = pd.concat([user_df]*len(user_historique_name), ignore_index=True)
                # Create a new dataframe with the list of restaurant names
                restaurants_df = pd.DataFrame({'name': user_historique_name, 'Like': user_historique_like})
                user_df = pd.concat([user_df, restaurants_df], axis=1)
            historique_data_user = user_df.merge(self.restaurant_categories_joined, on=['name'],how='inner')           
            return historique_data_user

    def get_test_data(self,user):
        historique_data_user = self.get_historique_data(user)
        restaurant_categories_filtered = historique_data_user[historique_data_user['Like']==1]
        liste_clusters = restaurant_categories_filtered.cluster.unique().tolist()
        test_data = self.restaurant_categories_joined[self.restaurant_categories_joined['cluster'].isin(liste_clusters)]
        test_data = test_data.iloc[100:]
        test_data = test_data[:40]
        test_data = test_data.drop(['number'],axis=1)
        test_data = test_data.drop_duplicates()
        test_data = test_data.set_index('id')
        return test_data

    def classification_Random_Forest(self,user):
        historique_data_user = self.get_historique_data(user)
        print(historique_data_user.columns)
        train_data = historique_data_user.drop(['number','rating','categories','name','alias','image_url','phone','price','review_count','x','y','url','user_name','user_id'],axis=1)
        train_data = train_data.drop_duplicates()
        train_data = train_data.set_index('id')
        X_train = train_data.drop(['Like'],axis=1)
        y_train = train_data["Like"]
        test_data = self.get_test_data(user)
        X_test =  test_data.drop(['categories','alias','image_url','phone','price','review_count','x','y','url'],axis=1)
        X_test = X_test.drop_duplicates()
        X_test =  X_test.drop(['rating','name'],axis=1)
        clf = RandomForestClassifier()
        # Train the classifier on the training data
        clf.fit(X_train, y_train)
        # Make predictions on the test data
        y_pred = clf.predict(X_test)
        return y_pred,X_test

"""
    def run (self):
        self.scheduler.add_job(self.process_background, 'interval', seconds=20)
        self.scheduler.start()
        self.app.run()
      
    def process_background(self):
        print("update du traitement")
        data_class = DataPreparation()
        self.restaurant_categories_joined = data_class.run() 

if __name__ == "__main__":
    recommendation =  classifierRecommandation()
    recommendation.run()
"""
