package com.example.hawkergo.activities.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.hawkergo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DynamicEditTextManager {

    private LinearLayout favouriteFoodsContainerController;
    private HashMap<Integer,EditText> favouriteFoodsEditTextControllers;
    private int favouriteFoodsTextFieldCounter;
    private Context context;

    public void init(Context context, LinearLayout favFoodContainer){
        this.context = context;
        this.favouriteFoodsContainerController = favFoodContainer;
        this.favouriteFoodsEditTextControllers = new HashMap<>();
        this.favouriteFoodsTextFieldCounter = 0;
    }

    public void addEditTextField() {
        favouriteFoodsTextFieldCounter+=1;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.single_dynamic_edit_text, null);

        Button btn = ll.findViewById(R.id.add_more_edit_text_button);
        btn.setId(View.generateViewId());

        EditText editText = ll.findViewById(R.id.add_more_food);
        editText.setId(View.generateViewId());

        favouriteFoodsEditTextControllers.put(editText.getId(), editText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEditTextField(ll, editText);
            }
        });

        favouriteFoodsContainerController.addView(ll);
    }

    public void removeEditTextField(View viewToRemove, View editTextControllerToRemove) {
        if(favouriteFoodsTextFieldCounter > 1){
            favouriteFoodsContainerController.removeView(viewToRemove);
            int idToRemove = editTextControllerToRemove.getId();
            favouriteFoodsEditTextControllers.remove(idToRemove);
            favouriteFoodsTextFieldCounter-=1;
        }

    }

    public List<String> getAllFavFoodItems(){
        ArrayList<String> list = new ArrayList<>();
        for(EditText controller : favouriteFoodsEditTextControllers.values()){
            list.add(controller.getText().toString());
        }
        return list;
    }
}
