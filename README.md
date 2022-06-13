# Interdisciplinary-Group-Project-Food-Recognition-and-Calory-Estimation-
My masters project Interdisciplinary Group Project, to build team-working ability. 


# IGP-TEAM15

This repository contains the source code for Team 15 IGP.

# Project introduction

Our projects aims to offer people an easier way to measure their calories. 

We've collected the 50 most popular dishes across the UK and applied ML techniques to them.

Imagine this: you're at a restaurant, the server has just brought your food and you've wondering how many calories this dish has. All you have to do is go into our app, take a picture of your dish and our Machine Learning algorithm will do the rest. You'll be provided with a calories estimate for your dish.

# How have we done this?

First, we needed a dataset from which the algorithms can learn. After collecting and curating this dataset, we realised that we need to classify the images first, then we can predict how many calories each dish has. Following our supervisor's advice and to be within the scope of our project, we've decided to create a classifier that will determine what kind of dish it is and after that to estimate the calories based on a look-up table that we've gathered and curated.

## ML approaches

We've tried a few different algorithms: Support Vector Machines, Decision Trees, Random Forest and lastly, Convolutional Neural Networks.

Initially, we decided to have 100 images / dish accruing to a dataset of 5.000 images. We applied these images to vanilla Machine Learning techniques (SVM, DT, RF), but the results were lackluster: 7%, 9% and 13% accuracy on the test set, respectively. We then augmented our dataset and the final dataset contained 40.000 images. This improved the performance of our algorithms to around 15% (SVM), 17%(DT) and 20% (RF) accuracy on the test set. We decided to tune the hyperparameters of RF and managed to get a 30% accuracy on the testset with hyper-parameter tuning.

We felt that we could get even better performance, so we decided to use Neural Networks, more specifically Convolutional Neural Networks. With a simple 3 layer CNN, we had now a 40% test set accuracy. The final improvement was changing the size of the images. Initially, the images were scaled down to 30x30 pixels, but we decided to try out a larger image size (100x100 pixels).

In the end, our dataset had around 40.000 images of size 100x100x3 and the final performance of our CNN came to 50% accuracy on the test set.

Due to time constraints and the scope of our project, we decided that this was the final step in our project's journey.

# App design and implementation

For our app's design, we'ved used Figma to create a Wireframe. We've also implemented everything in Java, Android Studio. The app is simple: you take a picture of the food and after some crunching, the app spits out the number of calories your dish has or the number of calories a 100g dish would have.
