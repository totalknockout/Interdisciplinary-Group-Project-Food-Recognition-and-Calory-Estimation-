from PIL import Image
import os
import PIL
import glob



directory = r'C:\Users\jabba\Documents\Data Science\IGP\Team-15\igp-team15\images'
# Loops through the images direcory
for subdir in os.listdir(directory):
    f_subdir = os.path.join(directory, subdir)
    
    # Loops through each sub-directory
    for image in os.listdir(f_subdir):
        image_path = os.path.join(f_subdir, image)
        
        # Resize the image
        original_image = Image.open(image_path)
        resized_image = original_image.resize((30,30))
        # Saves the resized image in-place (this will replace
        # the orignal one)
        resized_image.save(image_path)





















