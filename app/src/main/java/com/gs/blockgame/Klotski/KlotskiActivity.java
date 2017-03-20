package com.gs.blockgame.Klotski;

import com.gs.blockgame.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KlotskiActivity extends Activity {

	private TextView mScore;
    private KlotskiLayout mKlotskiLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
        int level = intent.getIntExtra("level", 0);
		setContentView(R.layout.mactivity_klotski);
        mKlotskiLayout = (KlotskiLayout) findViewById(R.id.id_klotski);
        mKlotskiLayout.setLevel(level);
	}
}

