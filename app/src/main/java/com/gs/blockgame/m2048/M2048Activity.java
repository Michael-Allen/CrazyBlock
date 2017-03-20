package com.gs.blockgame.m2048;

import com.gs.blockgame.R;
import com.gs.blockgame.m2048.Layout2048;
import com.gs.blockgame.m2048.Layout2048.OnGame2048Listener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class M2048Activity extends Activity implements OnGame2048Listener{

	private Layout2048 mLayout2048;

	private TextView mScore;

	private Button restart_button;
	private Button back_button;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mactivity_2048);

		mScore = (TextView) findViewById(R.id.id_score);
		mLayout2048 = (Layout2048) findViewById(R.id.id_game2048);
		mLayout2048.setOnGame2048Listener(this);
		restart_button = (Button) findViewById(R.id.button1);
		restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mLayout2048.restart();
            }
        });
		back_button = (Button) findViewById(R.id.button2);
		back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        		//long a = System.currentTimeMillis();
        		//Log.e("gaoshang","a = " + a);
            	mLayout2048.back();
        		//long b = System.currentTimeMillis();
        		//Log.e("gaoshang","save time  = " + (b-a));
            }
        });
	}

	@Override
	public void onScoreChange(int score)
	{
		mScore.setText("Score: " + score);
		mScore.invalidate();
	}

	@Override
	public void onGameOver()
	{
		new AlertDialog.Builder(this).setTitle("GAME OVER")
				.setMessage("YOU HAVE GOT " + mScore.getText())
				.setPositiveButton("RESTART", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						mLayout2048.restart();
					}
				}).setNegativeButton("EXIT", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				}).setNeutralButton("ClOSE", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						//finish();
					}
				}).show();
	}


}

