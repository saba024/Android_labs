package com.example.laba3.DrawableBoard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.laba3.Board.Board;
import com.example.laba3.Board.BoardSize;
import com.example.laba3.Ship.Ship;
import com.example.laba3.Ship.ShipDirection;
import com.example.laba3.Model.Coordinate;
import com.example.laba3.R;


import java.util.List;

@SuppressLint({"ClickableViewAccessibility", "ViewConstructor"})

public class DrawableBoardPlacing extends DrawableBoard
{
    private Board board;
    private List<Ship> ships;
    private Ship activeShip;

    private boolean shipFirstTouch;
    private boolean shipDragged;

    public DrawableBoardPlacing(final Context context, Board board, int buttonSize)
    {
        super(context, buttonSize);
        this.board = board;
        this.ships = board.getShips();

        for (int i = 0; i < BoardSize.ROWS; i++)
        {
            for (int j = 0; j < BoardSize.COLUMNS; j++)
            {
                final DrawableSquare square = squares[i][j];
                square.setOnTouchListener(new OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                    {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            int x = square.getCoordinate().getX();
                            int y = square.getCoordinate().getY();
                            boolean shipClicked = false;

                            for (Ship ship : ships)
                            {
                                for (Coordinate coordinate : ship.getListCoordinates())
                                {
                                    int shipX = coordinate.getX();
                                    int shipY = coordinate.getY();

                                    if (x == shipX && y == shipY)
                                    {
                                        if (activeShip == ship)
                                        {
                                            shipFirstTouch = false;
                                        }
                                        else
                                        {
                                            shipFirstTouch = true;
                                            activeShip = ship;
                                        }

                                        shipClicked = true;
                                        break;
                                    }
                                }
                            }

                            if (shipClicked)
                            {
                                colorShips();

                                ClipData data = ClipData.newPlainText("", "");
                                View.DragShadowBuilder shadowBuilder = new DragShadowBuilder();
                                view.startDrag(data, shadowBuilder, view, 0);

                                return true;
                            }
                            else
                            {
                                activeShip = null;
                                colorShips();
                            }
                        }

                        return false;
                    }
                });

                square.setOnDragListener(new OnDragListener()
                {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent)
                    {
                        if (activeShip != null)
                        {
                            switch(dragEvent.getAction())
                            {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    shipDragged = false;
                                    break;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    DrawableSquare square = (DrawableSquare) view;
                                    Coordinate squareCoordinate = square.getCoordinate();

                                    if (shipDragged)
                                    {
                                        if (activeShip.getDirection() == ShipDirection.HORIZONTAL)
                                        {
                                            activeShip.setCoordinate(new Coordinate(squareCoordinate.getX() - ((activeShip.getLength() - 1) / 2), squareCoordinate.getY()));
                                        }
                                        else if (activeShip.getDirection() == ShipDirection.VERTICAL)
                                        {
                                            activeShip.setCoordinate(new Coordinate(squareCoordinate.getX(), squareCoordinate.getY() - ((activeShip.getLength() - 1) / 2)));
                                        }

                                        colorShips();
                                    }

                                    break;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    shipDragged = true;
                                    break;
                                case DragEvent.ACTION_DROP:
                                    if (!shipDragged && !shipFirstTouch)
                                    {
                                        activeShip.rotate();
                                        colorShips();
                                    }
                                    break;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    break;
                                default:
                                    break;
                            }
                        }

                        return true;
                    }
                });
            }
        }

        colorShips();
    }

    public Ship getActiveShip()
    {
        return activeShip;
    }

    public void setNoActiveShip()
    {
        activeShip = null;
    }

    public DrawableSquare getSquareFromCoordinate(Coordinate coordinate)
    {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (x >= 0 && x < BoardSize.COLUMNS && y >= 0 && y < BoardSize.ROWS)
        {
            return squares[x][y];
        }
        else
        {
            return squares[0][0];
        }
    }

    public void rotateIconReset()
    {
        for (int i = 0; i < BoardSize.COLUMNS; i++)
        {
            for (int j = 0; j < BoardSize.ROWS; j++)
            {
                squares[i][j].setImage(0);
            }
        }
    }

    public void colorShips()
    {
        colorReset();
        rotateIconReset();

        for (Ship ship : ships)
        {
            for (Coordinate coordinate : ship.getListCoordinates())
            {
                if (board.isCollidingWithAny(ship))
                {
                    getSquareFromCoordinate(coordinate).setColor(R.color.colorCollision);
                }
                else if (ship == activeShip)
                {
                    if (coordinate.equals(ship.getCenter()))
                    {
                        getSquareFromCoordinate(coordinate).setImage(R.drawable.rotate);
                    }

                    getSquareFromCoordinate(coordinate).setColor(R.color.colorAccent);
                }
                else
                {
                    getSquareFromCoordinate(coordinate).setColor(R.color.colorShip);
                }
            }
        }
    }
}
