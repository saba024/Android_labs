package com.example.laba3.DrawableBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.laba3.Board.BoardSize;
import com.example.laba3.Model.Coordinate;
import com.example.laba3.R;

@SuppressLint({"ClickableViewAccessibility", "ViewConstructor"})

public class DrawableBoard extends TableLayout
{
    public DrawableSquare[][] squares;

    public DrawableBoard(Context context, int buttonWidth)
    {
        super(context);
        this.squares = new DrawableSquare[BoardSize.COLUMNS][BoardSize.ROWS];

        for (int i = 0; i < BoardSize.ROWS; i++)
        {
            TableRow row = new TableRow(context);
            addView(row);

            for (int j = 0; j < BoardSize.COLUMNS; j++)
            {
                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidth, buttonWidth);
                final DrawableSquare square = new DrawableSquare(context, new Coordinate(j, i));
                square.setLayoutParams(buttonLayoutParams);

                squares[j][i] = square;
                layout.addView(squares[j][i]);
                row.addView(layout);
            }
        }
    }

    public void colorReset()
    {
        for (int i = 0; i < BoardSize.COLUMNS; i++)
        {
            for (int j = 0; j < BoardSize.ROWS; j++)
            {
                squares[i][j].setColor(R.color.colorBoard);
            }
        }
    }

    public void colorCrosshair(int x, int y)
    {
        colorReset();

        for (int i = 0; i < BoardSize.COLUMNS; i++)
        {
            squares[i][y].setColor(R.color.colorTargetOuter);
        }

        for (int i = 0; i < BoardSize.ROWS; i++)
        {
            squares[x][i].setColor(R.color.colorTargetOuter);
        }

        squares[x][y].setColor(R.color.colorTargetInner);
    }
}

