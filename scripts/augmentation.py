import os
import cv2
import matplotlib.pyplot as plt
import glob
import PIL
from PIL import Image, ImageEnhance
import torch
import torchvision





directory = "/home/alex/university/projects/IGP/project_files_tests/images/"
new_directory = "/home/alex/university/projects/IGP/project_files_tests/augmented_images/"


# Rename the images from 0
# Loops through the images direcory
for subdir in os.listdir(directory):
    f_subdir = os.path.join(directory, subdir)
    f_newdir = os.path.join(new_directory, subdir)
    i = 0
    os.mkdir(f_newdir)
    # Loops through each sub-directory
    for image in os.listdir(f_subdir):

        image_path = os.path.join(f_subdir, image)
        image = Image.open(image_path)

        newimage_path = os.path.join(f_newdir, "{}.jpg".format(i))

        image.save(newimage_path)
        i += 1

for subdir in os.listdir(directory):
    f_subdir = os.path.join(directory, subdir)
    f_newdir = os.path.join(new_directory, subdir)
    # Loops through each sub-directory
    i = 0
    for image in os.listdir(f_subdir):
        # Open original image
        image_path = os.path.join(f_subdir, image)
        img = Image.open(image_path)

        # Rotate Images
        rotate30 = img.rotate(30)
        rotate30_path = os.path.join(f_newdir, "{}_rotate30.jpg".format(i))
        rotate30.save(rotate30_path)

        rotate90 = img.rotate(90)
        rotate90_path = os.path.join(f_newdir, "{}_rotate90.jpg".format(i))
        rotate90.save(rotate90_path)

        rotate120 = img.rotate(120)
        rotate120_path = os.path.join(f_newdir, "{}_rotate120.jpg".format(i))
        rotate120.save(rotate120_path)

        rotate180 = img.rotate(180)
        rotate180_path = os.path.join(f_newdir, "{}_rotate180.jpg".format(i))
        rotate180.save(rotate180_path)

        random_persp = torchvision.transforms.RandomPerspective(
            distortion_scale=0.5, p=1, interpolation=3, fill=0
        )
        random_perspective_img = random_persp(img)
        random_perspective_path = os.path.join(
            f_newdir, "{}_rand_perspective.jpg".format(i)
        )
        random_perspective_img.save(random_perspective_path)

        # Increase Brighthness
        brightness = ImageEnhance.Brightness(img)
        img_brightness = brightness.enhance(1.5)
        img_brightness_path = os.path.join(f_newdir, "{}_high_brightness.jpg".format(i))
        img_brightness.save(img_brightness_path)

        # Increase Contrast
        img_contr = ImageEnhance.Contrast(img)
        e_img = img_contr.enhance(3)
        img_contrast_path = os.path.join(f_newdir, "{}_high_contrast.jpg".format(i))
        e_img.save(img_contrast_path)

        i += 1
