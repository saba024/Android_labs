package com.example.laba3.DrawableBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.widget.AppCompatImageButton;

import com.example.laba3.Model.Coordinate;
import com.example.laba3.R;

@SuppressLint("ViewConstructor")
public class DrawableSquare extends AppCompatImageButton
{
    private final Coordinate coordinate;
    boolean clicked = false;

    DrawableSquare(Context context, Coordinate coordinate)
    {
        super(context);
        this.coordinate = coordinate;
        setColor(R.color.colorBoard);
        setPadding(0, 0, 0, 0);
        setScaleType(ScaleType.FIT_CENTER);
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public void setClicked(boolean clicked)
    {
        this.clicked = clicked;
    }

    public void setColor(int color)
    {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(4, getResources().getColor(R.color.colorStroke));
        drawable.setColor(getResources().getColor(color));
        setBackgroundDrawable(drawable);
    }

    public void setImage(int image)
    {
        setImageResource(image);
    }
}
