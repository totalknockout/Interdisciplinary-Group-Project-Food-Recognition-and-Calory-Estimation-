import json
from serpapi import GoogleSearch
import pandas as pd
import requests
import os

def scrap_images(dish):
    # Set up the API parameters
    params = { 
    "api_key": "1f2800ec0954257ac987f9c5bbce0a06fe3956a355faaa46c5f9cabb9514a9de",
    "engine": "google",
    "q": dish,
    "kl": "us-en",
    "tbm":"isch",
    "num": "100"
    }   
    
    # Use the API to scrap the links of the images
    search = GoogleSearch(params)
    results = search.get_dict()

    search_results = json.dumps(results['images_results'], indent=2, ensure_ascii=False)
    
    # Create the dataframe
    df = pd.read_json(search_results)
    
    # Create the directories for the images
    directory = dish.replace(" ", "_")
    parent_directory = "./images/"
    path = os.path.join(parent_directory, directory)
    
    os.mkdir(path)
    
    # Download the images
    i = 0 
    for link in list(df['thumbnail']):
        f = open(f'./images/{directory}/{i}.jpg','wb')
        response = requests.get(link)
        f.write(response.content)
        f.close()
        i += 1


food_list = ["Sunday roast with all the trimmings", 
"Fish and chips", 
"Full English breakfast", 
"Sausage", 
"Bacon", 
"Eggs (Scrambled)", 
"Eggs (Fried)", 
"Black pudding", 
"Toast", 
"Baked beans", 
"Mushrooms", 
"Fried tomato", 
"Chicken tikka masala", 
"Roast potatoes", 
"Roast chicken meat", 
"Chips", 
"Pizza", 
"Roast Turkey", 
"Bacon sandwich", 
"Lasagne", 
"Steak and chips", 
"Scones with cream and jam", 
"Roast beef meat", 
"Sausage and mash", 
"Spaghetti Bolognese", 
"Stir fry", 
"Shepherd's pie", 
"Cottage pie", 
"Beef burger and chips", 
"Pigs in blankets (sausages wrapped in bacon)", 
"Scrambled egg", 
"Ham egg and chips", 
"Chilli con carne", 
"Salad", 
"Beans on toast", 
"Sausage rolls", 
"Omelette", 
"Jacket potato with beans and cheese", 
"Ham and cheese toastie", 
"Fajita wraps", 
"Toad in the hole", 
"Pasta bake", 
"Cornish pasties", 
"Beef stew", 
"Steak and kidney pie", 
"Macaroni cheese", 
"Sweet and sour chicken", 
"Chicken and mushroom pie", 
"Fish finger sandwiches", 
"Tomato soup", 
"Chicken burger and chips", 
"Thai curry", 
"Baked salmon and new potatoes", 
"Paella", 
"Meatballs", 
"Boiled egg and soldiers", 
"Fishcakes", 
"Chicken Kebabs", 
"Scotch Egg"]

for dish in food_list:
    scrap_images(dish)
