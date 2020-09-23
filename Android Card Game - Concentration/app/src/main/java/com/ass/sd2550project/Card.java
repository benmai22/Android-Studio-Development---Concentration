package com.ass.sd2550project;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class Card extends Button
{
    // Initializes the variables and sets matched to false to clear
    // the value every time this class is created.
    private int id;
    private String word = "";
    private boolean matched = false;

    // Constructor for a Card object.
    public Card(Context context)
    {
        super(context);
    }


    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setId(int _id)
    {
        id = _id;
    }

    public int getId()
    {
        return id;
    }

    public void setWord(String _word) { word = _word; }

    public String getWord() { return word; }

    public void setMatched(boolean _matched)
    {
        matched = _matched;
    }

    public boolean getMatched()
    {
        return matched;
    }

    public void setText(Integer integer)
    {

    }

}

