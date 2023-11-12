class CommentaireDAO:
    def __init__(self, id_restaurent, text):
        self.id_restaurent = id_restaurent
        self.text = text

    def afficher(self):
        print(f"Commentaire pour le restaurant d'ID {self.id_restaurent}: {self.text}")
    def to_json(self):
        return {
            'Restaurant_id': self.id_restaurent,
            'review': self.text
        }