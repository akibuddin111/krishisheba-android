package com.krishisheba.ml;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUtils {

    public static MultipartBody.Part prepareImage(File file) {

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        return MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                requestFile
        );
    }
}
