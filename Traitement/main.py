from sentiment import SentimentAnalyse
from recommendation import recommendation
from recommandation_classifier import classifierRecommandation
from recommandation_classifier_age import classifierRecommandationUsers
from sentiment_data_preparation import DataPreparationSentiment
from apscheduler.schedulers.background import BackgroundScheduler
from recommendation_data_preparation import DataPreparation
import flask
from flask import request, jsonify
from concurrent.futures import ThreadPoolExecutor


class main():

    def __init__ (self):
        self.data = None
        self.scheduler = BackgroundScheduler()
        #self.executor = ThreadPoolExecutor()
        self.app =flask.Flask(__name__)
        #self.app.config["DEBUG"] = True

        ##Pour effectuer les post des sentiments dans DB
        self.dfSentiment = None
        self.RestaurantDTO = None

        ## POur effectuer les post des restaurants recommendés a user sur ses préférences
        self.categories = None
        self.dataRecommendationPref = None
        self.route()



    def route(self):
        @self.app.route('/sentiment', methods=['GET'])
        def sentiment ():
            DataSentimentAnalyse = SentimentAnalyse(self.dfSentiment, self.RestaurantDTO)
            response = DataSentimentAnalyse.run()
            return response

        @self.app.route('/firstRecommendation', methods=['POST'] )
        def firstrecommendation():
            user = request.json
            print(user)
            self.categories = user.get("preferences")
            Firstrecommendation = recommendation(self.categories,self.dataRecommendationPref )
            response = Firstrecommendation.run(user)
            print(response)
            return response

        @self.app.route('/classifierRecommendation', methods=['POST'] )
        def classifierRecommendation():
            user = request.json
            print(user)
            classifierRecommendation = classifierRecommandation(self.dataRecommendationPref)
            response = classifierRecommendation.predict_Restaurants_One_User()
            print(response)
            return response

        @self.app.route('/classifierRecommendationAge', methods=['POST'] )
        def classifierRecommendationAge():
            user = request.json
            print(user)
            classifierRecommendationAge = classifierRecommandationUsers(self.dataRecommendationPref )
            response = classifierRecommendationAge.predict_Restaurants_Multiple_Users()
            print(response)
            return response

    def process_background(self):
        """"
        print("Traitement pour sentiment")
        
        dataSentiment = DataPreparationSentiment()
        self.dfSentiment = dataSentiment.run() 
        self.RestaurantDTO =  dataSentiment.get_RestaurantDTO()
        """

        print("Traitement pour première recommendation")
        DataRecoPref = DataPreparation()
        self.dataRecommendationPref = DataRecoPref.run() 
        



    def run (self):
        #self.scheduler.add_job( func=lambda :self.executor.submit(self.process_background), trigger="interval",seconds=20)
        self.scheduler.add_job( self.process_background, 'interval', seconds=120)
        self.scheduler.start()
        self.app.run()

if __name__ == "__main__":
    main = main()
    main.run()

