import os
import json
import time, sys
import pandas as pd
import numpy as np
from pandas import concat
import requests
from sklearn.cluster import KMeans



class DataPreparation() :
    def __init__(self):
        #self.URL = "http://localhost:8080/Restaurants"
        self.URL = "restaurents.csv"
        self.RestaurantDAO = None

    def run (self):
        #data = self.get_dataset(self.URL)
        data = pd.read_csv(self.URL)
        self.RestaurantDAO = data
        data = self.data_cleaning(data)
        data =self.split_categories(data)
        df_categories = self.count_categories (data)
        data = self.reduce_by_categories(data,df_categories)
        binary_features = self.get_binary_feature(data)
        data = self.merge_data_with_new_features(data,binary_features)
        data = self.data_merge_with_clusters(data)
        DAO_for_merge = self.RestaurantDAO.drop(["name","categories","rating","rating"] , axis=1)
        data = pd.merge(data,DAO_for_merge,on=['id'],how='left')
        print('=================================')
        print("Done with preprocessing")
        return data

    def get_dataset (self,URL) :
        response = requests.get(url = URL)
        response_json = response.json()
        response_df = pd.read_json(response_json)
        return response_df

    def data_cleaning(self,restaurant_df) :
        restaurant = restaurant_df.drop(['alias', 'image_url', 'phone', 'price' ,'url','x','y','review_count'], axis=1)
        df_clean = restaurant.apply(lambda s:s.astype(str).str.replace('{', ""))
        df_clean = df_clean.apply(lambda s:s.astype(str).str.replace('}', ""))
        df_clean = df_clean.apply(lambda s:s.astype(str).str.replace('"', ""))
        return df_clean

    def split_categories_by_row(self,row):
        """Split the categories column on a comma and return a list of categories"""
        if ',' in row['categories']:
            return row['categories'].split(',')
        else:
            return [row['categories']]
    
    def split_categories (self,data_clean):
        # Apply the split_categories function to each row of the DataFrame
        data_clean['categories'] = data_clean.apply(self.split_categories_by_row, axis=1)
        # Use the explode method to split the categories column into separate rows
        data_clean = data_clean.explode('categories')
        # Drop any rows with a null value in the categories column
        data  = data_clean.dropna(subset=['categories'])
        print(data["categories"])
        return data 

    def count_categories  (self,data):
        """Compter le nombre de fois qu'une catégories apparait dans le dataFrame"""
        series = data['categories'].value_counts()
        count = 0
        for element in series:
            if (element < 10):
                count+=1
        df_categories = pd.DataFrame(series)
        df_categories['cat']=df_categories.index
        df_categories.rename(columns = {'categories':'number'}, inplace = True)
        df_categories.rename(columns = {'cat':'categories'}, inplace = True)
        return df_categories

    def reduce_by_categories (self,data,df_categories):
        """reducing the dataframe by deleting restaurants from not useful categories"""
        df_categories = df_categories.merge(data, on=['categories'],how='left')
        df_categories_filtered = df_categories[df_categories['number']>10]
        return df_categories_filtered 

    def get_binary_feature(self,df_categories_filtered):
        df_index = df_categories_filtered.set_index(df_categories_filtered['name'])
        df_joined_dum = pd.get_dummies(df_index['categories'])
        grouped = df_joined_dum.groupby(by=df_joined_dum.index)
        # Use the transform method to sum the values in each group
        binary_features = grouped.transform(lambda x: x.sum())
        binary_features.replace(2, 1, inplace=True)
        return binary_features

    def merge_data_with_new_features(self,data,binary_features):
        data = binary_features.merge( data, on=['name'],how='left')
        data = data.drop_duplicates()
        return data

    def data_merge_with_clusters(self,data):
        X = data.drop(['name','id','rating','number','categories'],axis=1)
        kmeans = KMeans(n_clusters=9, random_state=0) # 9 obtenue grace à elbow methode
        kmeans.fit(X)
        # Get the cluster labels for each restaurant
        labels = kmeans.labels_
        # Add the labes back to the original DataFrame
        data['cluster'] = labels
        return data 



if __name__ == "__main__":
    preprocessing = DataPreparation()
    preprocessing.run()
















    

 















    

        