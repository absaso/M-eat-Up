import json

import requests

import CommentaireDAO
import RestoModel

def get_restos():
    url = "http://localhost:8080/Restaurants"
    response = requests.get(url)
    print(response.text)

def get_reviews():
    url = "http://localhost:8080/Reviews"
    response = requests.get(url)
    print(response.text)

def send_commentary(commentary):
    url = "http://localhost:8080/Reviews/add"
    headers = {'Content-Type': 'application/json'}
    data = commentary.to_json()
    response = requests.post(url, json=data, headers=headers)
    return response


def send_resto(restaurantDAO):

    url = "http://localhost:8080/Restaurants/add"
    headers = {'Content-Type': 'application/json'}
    resto_json = json.dumps(restaurantDAO.__dict__)

    response = requests.post(url, json=resto_json, headers=headers)
    return response
resto={
            "id": "-0iLH7iQNYtoURciDpJf6w65",
            "alias": "le-comptoir-de-la-gastronomie-paris",
            "name": "Le Comptoir de la Gastronomie",
            "image_url": "https://s3-media3.fl.yelpcdn.com/bphoto/xT4YkCm_cZWbKbz9AVEnaA/o.jpg",

            "url": "https://www.yelp.com/biz/le-comptoir-de-la-gastronomie-paris?adjust_creative=4sPdTM4nhxt3QAyGNt3_Dg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=4sPdTM4nhxt3QAyGNt3_Dg",
            "review_count": 1207,

    "coordinates": {
        "latitude": 48.8645157999652,
        "longitude": 2.34540185646608
    },

            "price": "€€",

            "phone": "+33142333132"

        }
restoDao = RestoModel.RestoDAO()
restoDao.fromJson(json.dumps(resto))

commentaire = CommentaireDAO.CommentaireDAO(resto.get("id"), "c'est SUPER !!!")
#send_resto(restoDao)
data={
    "id_restaurent":"0iLH7iQNYtoURciDpJf6w65",
    "text":"c'est SUPER !"
}

send_commentary(commentaire)
#get_restos()
get_reviews()