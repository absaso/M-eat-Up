import this
import json

class RestoDAO:

    def __init__(self):
        self.price = None
        self.url = None
        self.x = None
        self.y = None
        self.phone = None
        self.review_count = None
        self.name = None
        self.categories = None
        self.rating = None

        self.image_url = None
        self.alias = None
        self.id = None

    def __int__(self ,id , alias, name, image_url, url, review_count, coordinates, phone, price):
        self.id = id
        self.alias = alias
        self.name = name
        self.image_url = image_url
        self.price = price
        self.review_count = review_count
        self.x = coordinates["latitude"]
        self.y = coordinates["longitude"]
        self.phone = phone

    def fromJson(self,json_data):
        json_object = json.loads(json_data)
        self.id = json_object["id"]
        self.alias = json_object["alias"]
        self.image_url = json_object["image_url"]
        self.name = json_object["name"]
        self.url = json_object["url"]
        if "price" in json_object:
            self.price = json_object["price"]
        self.review_count = json_object["review_count"]
        self.phone = json_object["phone"]
        self.x = json_object["coordinates"]["latitude"]
        self.y = json_object["coordinates"]["longitude"]
        malist=[]

        for i in json_object["categories"]:
            malist.append(i["title"])
        self.categories = malist

        self.rating = json_object["rating"]

