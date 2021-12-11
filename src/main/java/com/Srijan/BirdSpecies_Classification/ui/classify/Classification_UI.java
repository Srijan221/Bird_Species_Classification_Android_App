package com.Srijan.BirdSpecies_Classification.ui.classify;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.Srijan.BirdSpecies_Classification.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.app.Activity.RESULT_OK;
import android.text.method.ScrollingMovementMethod;


public class Classification_UI extends Fragment {
    TextView classitext;
    ImageView imageView;
    TextView infotext;
    private int PICK_IMAGE = 100;
    //Button extended;
    List<String>b_name, b_info;


    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private  int imageSizeX;
    private  int imageSizeY;
    private  TensorBuffer outputProbabilityBuffer;
    private  TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    private List<String> labels;
    Uri imageuri;
    private String name = "";

    ExtendedFloatingActionButton cidentify;

    ExtendedFloatingActionButton extended;
    private ArrayList<HashMap<String, String>> formList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_classification, container, false);

/*
        b_name = new ArrayList<>();
        b_info = new ArrayList<>();
        try {
            order(getActivity());
        }catch (Exception e) {
            e.printStackTrace();
        }
*/
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("links");
            formList = new ArrayList<>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String formula_value = jo_inside.getString("links_Name");
                String url_value = jo_inside.getString("links_Paragraph");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("links_Name", formula_value);
                m_li.put("links_Paragraph", url_value);

                formList.add(m_li);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageView = view.findViewById(R.id.imageView2);
        extended = view.findViewById(R.id.extended_fab);
        cidentify= view.findViewById(R.id.identify);
        classitext= view.findViewById(R.id.textView);
        infotext = view.findViewById(R.id.description);
        infotext.setMovementMethod(new ScrollingMovementMethod());
        extended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView hidden = (TextView) getActivity().findViewById(R.id.textViewhidden);
                hidden.setVisibility(View.INVISIBLE);
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, PICK_IMAGE);
            }
        });


        try {
            tflite = new Interpreter(loadmodelfile(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }







        cidentify.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                try {
                    int imageTensorIndex = 0;
                    int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
                    imageSizeY = imageShape[1];
                    imageSizeX = imageShape[2];
                    DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                    int probabilityTensorIndex = 0;
                    int[] probabilityShape =
                            tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
                    DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                    inputImageBuffer = new TensorImage(imageDataType);
                    outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                    probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                    inputImageBuffer = loadImage(bitmap);

                    tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
                    showresult();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("smodel1.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            labels = FileUtil.loadLabels(getActivity(),"labels_final.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                name = entry.getKey();
                classitext.setText(name);
                HashMap<String, String> specie = findSpecie(formList, entry.getKey());

                infotext.setText(specie.get("links_Paragraph"));


/*
                for (int j=0; j<b_name.size(); j++){
                    if (entry.getKey().contentEquals(b_name.get(j))){
                        classitext.setText(entry.getKey());
                        infotext.setText(b_info.get(j));
                    }
                }
                //classitext.setText(entry.getKey());

 */
            }
        }
    }

    private HashMap<String, String> findSpecie(ArrayList<HashMap<String, String>> formList, String key) {
        for (int i =0;i<formList.size();i++){
            if (key.trim().equals(formList.get(i).get("links_Name").trim())){
                return formList.get(i);
            }
        }
        return new HashMap<>();
    }

    /*
       /////////for excel file code from here
       public void order(Activity activity) throws IOException{

           AssetManager an= activity.getAssets();
           InputStream in = an.open("run_results");
           try {
               Workbook wb = Workbook.getWorkbook(in);
               Sheet s = wb.getSheet(0);
               //int row = s.getRows();
               //int col = s.getColumns();
               for (int i=0; i<s.getRows(); i++){
                   Cell[] row = s.getRow(i);
                   //Cell y = s.getCell(1,i);
                   b_name.add(row[0].getContents());
                   b_info.add(row[1].getContents());
               }

           } catch (BiffException e) {
               e.printStackTrace();
           }

       }

   */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("run_results_xls_final.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}



