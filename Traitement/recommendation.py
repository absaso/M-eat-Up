import heapq
from collections import Counter
import json
import pandas as pd
from pandas import concat
import requests
from recommendation_data_preparation import DataPreparation
import flask
from flask import request, jsonify
from apscheduler.schedulers.background import BackgroundScheduler


class recommendation():

    def __init__ (self,categories ,data):
        self.categories = categories
        self.data = data
        #self.scheduler = BackgroundScheduler()
        #self.app =flask.Flask(__name__)
        #self.app.config["DEBUG"] = True
        #self.route()

    #def route(self):
    #@self.app.route('/firstRecommendation', methods=['POST'] )
    #def first_recommendation(user):
    def run(self,user):
        clusters = self.clusters_first_recommendation()
        new_restaurant_clustered = pd.DataFrame()
        for target_cluster in clusters:
            restaurants_clustered = self.data[self.data['cluster']==target_cluster]
            new_restaurant_clustered = pd.concat([new_restaurant_clustered, restaurants_clustered], axis=0)
        possible_similars_sorted = new_restaurant_clustered.sort_values(by='rating',ascending=False)
        recommendation = possible_similars_sorted[['name','id','categories' ,'rating']].reset_index(drop=True)
        for i in range(len(recommendation)):
            restau = recommendation.loc[i]
            headers = {'Content-Type': "application/'json'"}
            r = requests.put("http://localhost:8080/suggestions/"+str(user.get("login")) ,json={'id': restau['id']}, headers=headers )
        print("recommendation done")
        return recommendation.to_json(orient='records')


    def clusters_first_recommendation (self): 
        """This function will be call to suggest restaurant to the user when he first sigIn""" 
        liste = []
        clusters = []
        for cat in self.categories :
            restaurant_categories_filtered = self.data.loc[self.data['categories']==cat]
            list_clusters = restaurant_categories_filtered.cluster.unique().tolist()
            print("dataframe avec filtrage de categories",restaurant_categories_filtered.shape)
            counts = restaurant_categories_filtered['cluster'].value_counts()
            maximum = max (counts)
            if len(counts)>=2:
                max_two = heapq.nlargest(2, counts)
                liste_clusters = []
            for i in counts.index:
                if counts[i] in max_two:
                    liste_clusters.append(i) 
            if len(liste_clusters)>=2:
                liste_clusters = liste_clusters[0:2]
            for element in liste_clusters :
                liste.append(element) 
        counter = Counter(liste)
        # Get the 2 most common element
        most_common = counter.most_common(2)
        clusters.append(most_common[0][0])
        clusters.append(most_common[1][0])
        return clusters
    
"""
    def run (self):
        self.scheduler.add_job(self.process_background, 'interval', seconds=20)
        self.scheduler.start()
        self.app.run()
    
    def process_background(self):
        print("update du traitement")
        data_class = DataPreparation()
        self.data = data_class.run()   



if __name__ == "__main__":
    categories = ["Italian","Mediterranean","Bars"]
    recommendation = recommendation(categories,data)
    recommendation.run()
"""
