package com.llama1b;

public class NativeLlama {
    static {
        System.loadLibrary("llama_jni"); // Load your native library
    }

    // Native method for loading the model
    public static native long loadModel(String modelPath);

    // Native method for running inference
    public static native String runInference(long modelHandle, String prompt);
}
