import tensorflow as tf
import os

# get path
path = os.getcwd()

# load model
model = tf.keras.models.load_model('models/32-64-128.h5')

# convert model
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# save converted model
with open("models/converted_model.tflite", "wb") as f:
    f.write(tflite_model)
