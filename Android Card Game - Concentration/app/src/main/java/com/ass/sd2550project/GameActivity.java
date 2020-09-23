package com.ass.sd2550project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements PlayerControls{
    private SharedPreferences prefs;
    int mCardCount = 0;
    int mHighScore = 0;
    int mScore = 0;

    List<Card> mCardList;
    Card mCard1;
    Card mCard2;

    TextView mTxtScore;
    TextView mTxtHighScore;
    Button mBtnTryAgain;
    Button mBtnNewGame;
    Button mBtnEndGame;
    Button mBtnPlaySound;
    Button mBtnStopSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        mCardCount = extras.getInt("CardCount");

        mCardList = new ArrayList<>();

        mTxtScore = findViewById(R.id.txt_score);
        mTxtScore.setText(getResources().getString(R.string.score) + mScore);

        mTxtHighScore = findViewById(R.id.txt_high_score);

        mBtnTryAgain = findViewById(R.id.btn_try_again);
        mBtnTryAgain.setEnabled(false);
        mBtnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTryAgain();
            }
        });

        mBtnNewGame = findViewById(R.id.btn_new_game);
        mBtnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewGame();
            }
        });

        mBtnEndGame = findViewById(R.id.btn_end_game);
        mBtnEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEndGame();
            }
        });

        mBtnPlaySound = findViewById(R.id.btn_play_sound);
        mBtnStopSound = findViewById(R.id.btn_stop_sound);

        mBtnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("bg_sound");
                if(fragment instanceof SoundPlayerFragment) {
                    ((SoundPlayerFragment) fragment).playSound();
                    mBtnPlaySound.setEnabled(false);
                    mBtnStopSound.setEnabled(true);
                }
            }
        });

        mBtnStopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("bg_sound");
                if(fragment instanceof SoundPlayerFragment) {
                    ((SoundPlayerFragment) fragment).stopSound();
                    mBtnPlaySound.setEnabled(true);
                    mBtnStopSound.setEnabled(false);
                }

            }
        });

        int mRows = mCardCount / 4;
        ViewGroup row[] = new ViewGroup[5];
        row[0] = findViewById(R.id.row1);
        row[1] = findViewById(R.id.row2);
        row[2] = findViewById(R.id.row3);
        row[3] = findViewById(R.id.row4);
        row[4] = findViewById(R.id.row5);

//        ViewGroup panel = findViewById(R.id.layout_game);
//        Rect rt = new Rect();
//        panel.getLocalVisibleRect(rt);
//        int cardWidth = rt.width() / 4;
//        int cardHeight = rt.height() / 5;
        int cardWidth = 150;
        int cardHeight = 250;

        for (int i = 0; i < mCardCount; i++) {
            final Card card = new Card(this);
            card.setId(i);
            card.setWord("");
            card.setText("AAA");
            card.setLayoutParams(new ViewGroup.LayoutParams(cardWidth, cardHeight));
            row[i/4].addView(card);
            card.setText("");
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectCard(card);
                }
            });
            mCardList.add(card);
        }
        setWord();

        prefs = getSharedPreferences("card_match", 0);
        loadHighScore();
    }

    private void onTryAgain() {
        mBtnTryAgain.setEnabled(false);
        mCard1.setEnabled(true);
        mCard2.setEnabled(true);
        mCard1.setText("");
        mCard2.setText("");
        mCard1 = null;
        mCard2 = null;
    }

    private void onNewGame() {
        for (int i=0; i<mCardList.size(); i++) {
            mCardList.get(i).setEnabled(true);
            mCardList.get(i).setWord("");
            mCardList.get(i).setText("");
            mCardList.get(i).setMatched(false);
            mCardList.get(i).setTextColor(Color.BLACK);
        }
        setWord();
        mBtnEndGame.setEnabled(true);
        mScore = 0;
        mTxtScore.setText(getResources().getString(R.string.score) + mScore);

        mCard1 = null;
        mCard2 = null;
    }

    private void onEndGame() {
        for (int i=0; i<mCardList.size(); i++) {
            mCardList.get(i).setText(mCardList.get(i).getWord());
            mCardList.get(i).setEnabled(false);
            mCardList.get(i).setTextColor(Color.BLUE);
        }
        mBtnTryAgain.setEnabled(false);
    }

    private void setWord() {
        String words[] = getResources().getStringArray(R.array.words);
        int iWord = 0;
        int setWords = 0;
        while (true) {
            double rnd = Math.random();
            int iRnd = (int)(rnd * 100) % mCardCount;
            if (!mCardList.get(iRnd).getWord().isEmpty())
                continue;
            mCardList.get(iRnd).setWord(words[iWord]);
            setWords++;
            if (setWords % 2 == 0) {
                iWord++;
            }
            if (setWords == mCardCount)
                break;
        }
    }

    private void selectCard(Card card) {
        if (mCard1 != null && mCard2 != null)
            return;
        card.setEnabled(false);
        if (mCard1 == null && mCard2 == null) {
            mCard1 = card;
            mCard1.setText(mCard1.getWord());
        }

        if (mCard1 != null && mCard1 != card && mCard2 == null) {
            mCard2 = card;
            mCard2.setText(mCard2.getWord());
            checkCard();
        }
    }

    private void checkCard() {
        if (mCard1.getWord().equals(mCard2.getWord())) {
            mCard1.setMatched(true);
            mCard2.setMatched(true);
            mCard1.setTextColor(Color.BLUE);
            mCard2.setTextColor(Color.BLUE);
            mScore += 2;
            mCard1 = null;
            mCard2 = null;

            int i;
            for (i=0; i<mCardList.size(); i++) {
                if (mCardList.get(i).getMatched() == false)
                    break;
            }

            if (i == mCardList.size()) {    // All Match
                if (mScore > 0) {
                    if (mScore > mHighScore) {
                        mHighScore = mScore;
                        saveHighScore();
                        loadHighScore();
                    }
                    new AlertDialog.Builder(this)
                            .setMessage(getResources().getString(R.string.win))
                            .setPositiveButton("OK", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    showInputUsernameDialog();
                                }
                            })
                            .show();

                } else {
                    new AlertDialog.Builder(this)
                            .setMessage(getResources().getString(R.string.lose))
                            .setPositiveButton("OK", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    showInputUsernameDialog();
                                }
                            })
                            .show();
                }



                mBtnEndGame.setEnabled(false);
            }
        } else {
            if(mScore > 0)
                mScore--;
            mBtnTryAgain.setEnabled(true);
        }

        if(mScore > mHighScore) {
            mHighScore = mScore;
            saveHighScore();
            loadHighScore();
        }

        String strScore = getResources().getString(R.string.score) + mScore;
        mTxtScore.setText(strScore);
    }

    private void loadHighScore() {
        mHighScore = prefs.getInt("high_score_" + mCardCount, 0);
        String username = prefs.getString("high_score_username_"+ mCardCount,"");
        if(username.trim().length() > 1)
            mTxtHighScore.setText(getResources().getString(R.string.high_score) + mHighScore+" ("+username+")");
        else
            mTxtHighScore.setText(getResources().getString(R.string.high_score) + mHighScore);

    }

    private void saveHighScore() {
        if(mScore >= mHighScore) {
            SharedPreferences.Editor ed = prefs.edit();
            ed.putInt("high_score_" + mCardCount, mHighScore);

            if (TextUtils.isEmpty(username)) {
                username = "";
            }
            ed.putString("high_score_username_" + mCardCount, username);
            ed.apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            showExitAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showExitAlert();
    }

    private void showExitAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getResources().getString(R.string.do_you_want_to_leave))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        GameActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showInputUsernameDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.username_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final EditText userInput = view.findViewById(R.id.etName);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String userName = userInput.getText().toString();
                                if(!TextUtils.isEmpty(userName.trim())) {
                                    username = userName.trim();
                                    saveHighScore();
                                    loadHighScore();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CardCount", mCardCount);
        outState.putInt("CurrentScore",mScore);
        outState.putInt("HighestScore", mHighScore);
        outState.putString("HighestScoreText",mTxtHighScore.getText().toString());
        for(int i = 0; i < mCardList.size(); i++) {
            outState.putInt("card_"+i+"_id", mCardList.get(i).getId());
            outState.putString("card_"+i+"_word", mCardList.get(i).getWord());
            outState.putBoolean("card_"+i+"_matched", mCardList.get(i).getMatched());
            outState.putBoolean("card_"+i+"_enabled", mCardList.get(i).isEnabled());
        }

        outState.putBoolean("try_again_enabled", mBtnTryAgain.isEnabled());
        outState.putBoolean("new_game_enabled", mBtnNewGame.isEnabled());
        outState.putBoolean("end_game_enabled", mBtnEndGame.isEnabled());

        if(mCard1 != null) {
            outState.putInt("card1_index",mCardList.indexOf(mCard1));
        }
        if(mCard2 != null) {
            outState.putInt("card2_index", mCardList.indexOf(mCard2));
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCardCount = savedInstanceState.getInt("CardCount");
        mScore = savedInstanceState.getInt("CurrentScore");
        mTxtScore.setText(getResources().getString(R.string.score) + mScore);

        mHighScore = savedInstanceState.getInt("HighestScore");
        mTxtHighScore.setText(savedInstanceState.getString("HighestScoreText"));

        mBtnTryAgain.setEnabled(savedInstanceState.getBoolean("try_again_enabled"));
        mBtnNewGame.setEnabled(savedInstanceState.getBoolean("new_game_enabled"));
        mBtnEndGame.setEnabled(savedInstanceState.getBoolean("end_game_enabled"));

        for(int i = 0; i < mCardList.size(); i++) {
            mCardList.get(i).setId(savedInstanceState.getInt("card_"+i+"_id"));
            mCardList.get(i).setWord(savedInstanceState.getString("card_"+i+"_word"));
            mCardList.get(i).setMatched(savedInstanceState.getBoolean("card_"+i+"_matched"));
            mCardList.get(i).setEnabled(savedInstanceState.getBoolean("card_"+i+"_enabled"));

            if(mCardList.get(i).getMatched()) {
                mCardList.get(i).setTextColor(Color.BLUE);
                mCardList.get(i).setText(mCardList.get(i).getWord());
            }
        }

        if(savedInstanceState.containsKey("card1_index")) {
            mCard1 = mCardList.get(savedInstanceState.getInt("card1_index"));
        }

        if(savedInstanceState.containsKey("card2_index")) {
            mCard2 = mCardList.get(savedInstanceState.getInt("card2_index"));
        }
    }

    private String username;

    @Override
    protected void onStart() {
        super.onStart();
        Fragment soundPlayerFragment = getSupportFragmentManager().findFragmentByTag("bg_sound");
        if(soundPlayerFragment == null) {
            soundPlayerFragment = new SoundPlayerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(soundPlayerFragment,"bg_sound").commit();
        }

        else {
            setSoundButtons(soundPlayerFragment);
        }
    }

    private void setSoundButtons(Fragment fragment) {
        if(fragment instanceof SoundPlayerFragment) {
            boolean isPlaying = ((SoundPlayerFragment) fragment).isPlaying();
            mBtnPlaySound.setEnabled(!isPlaying);
            mBtnStopSound.setEnabled(isPlaying);
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        setSoundButtons(fragment);
    }

    @Override
    public void toggleControls(boolean isPlaying) {
        if(mBtnPlaySound != null) {
            mBtnPlaySound.setEnabled(!isPlaying);
        }
        if(mBtnStopSound != null) {
            mBtnStopSound.setEnabled(isPlaying);
        }
    }

    @Override
    protected void onDestroy() {
        mBtnStopSound = null;
        mBtnPlaySound = null;
        mBtnEndGame = null;
        mBtnNewGame = null;
        mBtnTryAgain = null;
        mCard1 = null;
        mCard2 = null;
        mCardList.clear();
        super.onDestroy();
    }
}
