package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich aSandwich) {
        TextView alsoKnown = findViewById(R.id.also_known_tv);
        alsoKnown.setText(listToString(aSandwich.getAlsoKnownAs()));
        TextView ingredients = findViewById(R.id.ingredients_tv);
        ingredients.setText(listToString(aSandwich.getIngredients()));
        TextView origin = findViewById(R.id.origin_tv);
        origin.setText(aSandwich.getPlaceOfOrigin());
        TextView description = findViewById(R.id.description_tv);
        description.setText(aSandwich.getDescription());
    }
    private String listToString(List<String> aStrings) {
        if (aStrings != null) {
            String toReturn="";
            for (int i = 0;i<aStrings.size();i++) {
                //Is it that important to avoid +=??
                toReturn+=aStrings.get(i) + ",";
            }
            return toReturn.substring(0,toReturn.length()-1);
        }
        return null;
    }
}
