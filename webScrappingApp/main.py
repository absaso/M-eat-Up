import json

import requests

import time



from CommentaireDAO import CommentaireDAO
from RestoModel import RestoDAO

#browser = webdriver.Chrome()



def get_resto():
    global offset
    url = "https://api.yelp.com/v3/businesses/search?&limit=50&term=restaurant&location=Lyon&offset=" + str(offset)

    headers = {
        "accept": "application/json",
        "Authorization": 'Bearer {}'.format(token)
    }

    response = requests.get(url, headers=headers)
    return response



def send_resto(restaurantDAO):
    print('{}/{}'.format(offset,numbr_resto))
    url = "http://localhost:8080/Restaurants/modify"
    headers = {'Content-Type': 'application/json'}
    resto_json = json.dumps(restaurantDAO.__dict__)

    response = requests.post(url, json=resto_json, headers=headers)
    return response


# Variables
token = 'SDc4z_zGv39Vh8TPT-N3zqUeTEnYkGqlpxyqEU1zmDZd9k8SbRXhJrALc7IG-aYcnmWdgCf2LqH3KYcEhKc70CEZe6Nvp4Q7chKlu2a3' \
        '-NAy5J62px2LnoJqsP6zY3Yx '
list_de_restaurents = []
commentaires = []
nbr_commentaires = 0
offset = 0
response = get_resto()
numbr_resto = response.json()["total"]
print(str(numbr_resto) + ' trouvé')
print('Nombre de requettes :' + str(numbr_resto // 50))

# starting the script
#start_browser()

while offset < 1000:  # (numbr_resto//50)
    response = get_resto()

    for resto in response.json()['businesses']:
        restoDao = RestoDAO()
        restoDao.fromJson(json.dumps(resto))
        list_de_restaurents.append(restoDao)
        send_resto(restoDao)     #decommenter pour envoyer les resto a la bdd
        nbr_commentaires += restoDao.review_count
        offset += 1

print("nbr_commentaires" +str(nbr_commentaires))
count = 0
count_rest = 0
"""for resto in list_de_restaurents[753+158:]:
    count_rest+=1
    commentarys = get_commentary(resto)
    print(len(commentarys))
    result = []
    for commentary in commentarys:
        if commentary.text not in {c.text for c in result}:
            result.append(commentary)
    print(str(len(commentarys)-len(result))+" doublons supprimé")
    for commentary in result:
        count += 1
        send_commentary(commentary)
    print(str(len(result)) + " commentaire envoyés au serveur")
    print(str(count_rest)+"/"+str(1000)+" restaurents visité")"""
