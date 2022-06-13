package com.example.simpleclassificationapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.common.ops.DequantizeOp;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

public class Classifier {

    private Context context;
    Interpreter tflite;
    final String ASSOCIATED_AXIS_LABELS = "labels.txt";
    List<String> associatedAxisLabels = null;

    public Classifier(Context context)
    {
        this.context = context;

        try {
            associatedAxisLabels = FileUtil.loadLabels(context, ASSOCIATED_AXIS_LABELS);
        } catch (IOException e) {
            Log.e("tfliteSupport", "Error reading label file", e);
        }

        try{
            MappedByteBuffer tfliteModel
                    = FileUtil.loadMappedFile(context,
                    "converted_model.tflite");
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e){
            Log.e("tfliteSupport", "Error reading model", e);
        }
    }

    public String classify(ImageProxy image){
        @SuppressLint("UnsafeExperimentalUsageError")
        Image img = image.getImage();
        Bitmap bitmap = Utils.toBitmap(img);
        int rotation = Utils.getImageRotation(image);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = height > width ? width : height;
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(100, 100, ResizeOp.ResizeMethod.BILINEAR))
                .add(new DequantizeOp(0, 1/255.0f))
                .build();
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);
        TensorBuffer probabilityBuffer =
                TensorBuffer.createFixedSize(new int[]{1, 56}, DataType.FLOAT32);
        if(null != tflite) {
            tflite.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer());
        }
        TensorProcessor probabilityProcessor =
                new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();

        String result = " ";
        if (null != associatedAxisLabels) {
            // Map of labels and their corresponding probability
            TensorLabel labels = new TensorLabel(associatedAxisLabels,
                    probabilityProcessor.process(probabilityBuffer));

            // Create a map to access the result based on label
            Map<String, Float> floatMap = labels.getMapWithFloatValue();
            result = Utils.writeResults(floatMap);
        }
        return result;
    }

}
