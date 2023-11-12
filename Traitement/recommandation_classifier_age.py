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
import random


class classifierRecommandationUsers() :
    def __init__(self,data):
        #self.URL = "http://localhost:8080/Restaurants"
        self.historiques = None
        self.URL = "restaurents.csv"
        self.restaurant_categories_joined = data
        #self.route()
        #self.route()

    #def route(self):
        #@self.app.route('/classifierRecommendationAge', methods=['POST'])
    def predict_Restaurants_Multiple_Users(self):
        user = request.json
        X_test, y_pred = self.classifier_Random_Forest(user)
        X_test['Like_user']= y_pred
        restaurant_categories_joined = self.restaurant_categories_joined.set_index('id')
        #test_data_n = test_data.drop(['Like'],axis=1)
        #possible_similars_sorted = pd.merge(X_test,test_data, on='Like', how='inner')
        merged_df = pd.merge(restaurant_categories_joined,X_test, left_index=True, right_index=True, how='inner')
        merged_df = merged_df.drop_duplicates()
        df_to_recommend = merged_df[merged_df['Like_user']==1]
        df_to_recommend = df_to_recommend.sort_values(by='rating', ascending=False)
        df_to_recommend = df_to_recommend.drop_duplicates()
        df_to_recommend = df_to_recommend.reset_index()
        df_to_recommend = df_to_recommend.rename(columns={'index': 'id'})
        df_to_recommend = df_to_recommend[['id','name','rating','alias','image_url','phone','price','review_count','x','y','url']].drop_duplicates()
        response = df_to_recommend.to_json(orient='records')
        return response
    

 
# remplacer la création des utilisateurs et la génération aléatoire de l'historique  par les données de la BDD

    def createUsers(self):
        user_dfs = []
        user_ids = ['user_1','user_2','user_3','user_4','user_5','user_6','user_7','user_8','user_9','user_10','user_11']
        for user_id in user_ids:
            user_df = pd.DataFrame({'user_id': [user_id], 'age': [random.randint(18,60)]})
            # Repeat the user ID and age values 10 times
            user_df = pd.concat([user_df]*10, ignore_index=True)
            # Create a new dataframe with the list of restaurant names
            sample_restaurant = self.restaurant_categories_joined.sample(10)['name'].tolist()
            restaurants_df = pd.DataFrame({'name': sample_restaurant})
            user_df = pd.concat([user_df, restaurants_df], axis=1)
            user_dfs.append(user_df)

        all_users_df = pd.concat(user_dfs)
        df_users_restaurants = all_users_df.merge(self.restaurant_categories_joined, on=['name'],how='inner')
        df_users_restaurants['Like'] = np.random.choice([0, 1], size=len(df_users_restaurants))
        return df_users_restaurants

    def createtestUser(self,user):
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
        user_age = data["age"]
        for user_id in user_id:
            user_df = pd.DataFrame({'user_id': [user_id], 'user_name': user_name,'age': user_age})
            # Repeat the user ID and age values 10 times
            user_df = pd.concat([user_df]*len(user_historique_name), ignore_index=True)
            # Create a new dataframe with the list of restaurant names
            restaurants_df = pd.DataFrame({'name': user_historique_name, 'Like': user_historique_like})
            user_df = pd.concat([user_df, restaurants_df], axis=1)
        historique_data_user = user_df.merge(self.restaurant_categories_joined, on=['name'],how='inner')           
        return historique_data_user

    def add_Age_groups(self,user):
        df_users_restaurants = self.createUsers()
        test_users_restaurants = self.createtestUser(user)
        bins = [  19, 30, 40,50, 60]
        df_users_restaurants['age_group'] = pd.cut(df_users_restaurants['age'], bins, labels=[0.25, 0.5, 0.75, 1])
        df_users_restaurants['age_group'] = pd.cut(df_users_restaurants['age'], bins, labels=[0.25, 0.5, 0.75, 1])
        test_users_restaurants['age_group'] = pd.cut(test_users_restaurants['age'], bins, labels=[0.25, 0.5, 0.75, 1])
        test_users_restaurants['age_group'] = pd.cut(test_users_restaurants['age'], bins, labels=[0.25, 0.5, 0.75, 1])
        # entre 0.25 => entre 20 et 30 
        # entre 0.5 => entre 30 et 40 
        # entre 0.75 => entre 40 et 0 
        # entre 0.1 => entre 50 et 60 
        return df_users_restaurants, test_users_restaurants


    def classifier_Random_Forest(self,user):
        df_users_restaurants, test_users_restaurants = self.add_Age_groups(user)
        df_users_restaurants = df_users_restaurants.dropna()
        df_users_restaurants = df_users_restaurants.set_index('id')
        test_users_restaurants = test_users_restaurants.set_index('id')
        df_users_restaurants = df_users_restaurants.drop(['age','user_id','number'], axis =1)
        test_users_restaurants = test_users_restaurants.drop(['age','user_id','number'], axis =1)

        print(df_users_restaurants)
        print(df_users_restaurants)

        # Split the data into features and labels
        X_train = df_users_restaurants.drop(['Like','name','categories','rating','alias','image_url','phone','price','review_count','x','y','url'],axis=1)
        y_train = df_users_restaurants["Like"]
        X_test =  test_users_restaurants.drop(['Like','name','categories','rating','alias','image_url','phone','price','review_count','x','y','url','user_name'],axis=1)

        clf = RandomForestClassifier()
        # Train the classifier on the training data
        clf.fit(X_train, y_train)
        # Make predictions on the test data
        y_pred = clf.predict(X_test)
        return X_test, y_pred

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
    recommendation =  classifierRecommandationUsers()
    recommendation.run()
"""