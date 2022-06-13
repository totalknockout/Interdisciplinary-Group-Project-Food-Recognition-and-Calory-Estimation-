import json
from serpapi import GoogleSearch
import pandas as pd
import requests
import os

def scrap_images(dish,num):
    # Set up the API parameters
    params = { 
    "api_key": "c98ee496dd6027c165df3f823bc3528ceb75aeb43b6b73f48739bffaf13749fc",
    "engine": "google",
    "start" : 100,
    "q": dish,
    "kl": "us-en",
    "tbm":"isch",
    "num": num
    }   
    
    # Use the API to scrap the links of the images
    search = GoogleSearch(params)
    results = search.get_dict()

    search_results = json.dumps(results['images_results'], indent=2, ensure_ascii=False)
    
    # Create the dataframe
    df = pd.read_json(search_results)
    
    # Create the directories for the images
    directory = dish.replace(" ", "_")
    parent_directory = r"C:\Users\Jasper\Documents\Team15\igp-team15\images2"
    path = os.path.join(parent_directory, directory)

    try:
        os.mkdir(path)
    except:
        print("Already made")

    
    # Download the images
    i = 0 
    for link in list(df['thumbnail']):
        f = open(fr'C:\Users\Jasper\Documents\Team15\igp-team15\images2\{directory}\{i+100}.jpg','wb')
        response = requests.get(link)
        f.write(response.content)
        f.close()
        i += 1


food_list = [["chicken_and_mushroom_pie", 4],
["chicken_burger_and_chips", 17],
["chilli_con_carne",2],
["UK chips",100],
["cornish_pasties",3],
["cottage_pie",1],
["Eggs Fried",9],
["Eggs Scrambled",2],
["Fajitas" ,9],
["Cod & chips",6],
["Fish_finger_sandwiches",9],
["fried_tomato",49],
["full_english",5],
["ham_and_cheese_toastie",13],
["ham_egg_and_chips",8],
["jacket_potato_with_beans_and_cheese",9],
["pigs_and_blankets",14],
["pizza",18],
["toast",45],
["roast_turkey_carved",100],
["roast_beef",14],
["Roast chicken", 30],
["Roast potatoes", 1],
["Salad", 6],
["Cooked Sausages", 30],
["Bangers and mash", 15],
["Sausage Roll", 13],
["lasagne", 26],
["macaroni_cheese",30],
["Fried mushrooms", 100],
["Omelette", 4],
["pasta bake plate", 6]]

for dish in food_list:
    x,y=dish
    scrap_images(x,y)
