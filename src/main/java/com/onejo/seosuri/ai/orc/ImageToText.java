package com.onejo.seosuri.ai.orc;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageToText {

    public String detectText(String filePath) throws BusinessException {
        String output = "";
        List<AnnotateImageRequest> requests = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)){
            ByteString imgBytes = ByteString.readFrom(fileInputStream);

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests. After completing all of your requests, call
            // the "close" method on the client to safely clean up any remaining background resources.
            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();
                output = "";
                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        throw new BusinessException(ErrorCode.IMAGE_TO_TEXT_PROCESSING_ERROR);
                    }

                    // For full list of available annotations, see http://g.co/cloud/vision/docs
                    for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                        String str = annotation.getDescription();
                        output += str;
                        break;
                    }
                }
            } catch(IOException e){
                throw new BusinessException(ErrorCode.OCR_CLIENT_CREATION_ERROR);
            }
        } catch (IOException e){
            throw new BusinessException(ErrorCode.FILE_IO_ERROR);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.OCR_PROCESSING_ERROR);
        }
        return output;
    }

}
