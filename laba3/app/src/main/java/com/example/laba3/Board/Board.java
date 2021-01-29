package com.example.laba3.Board;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.laba3.Model.Coordinate;
import com.example.laba3.Ship.Ship;
import com.example.laba3.Ship.ShipDirection;
import com.example.laba3.Ship.ShipType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board implements Parcelable
{
    private BoardStatus[][] statuses;
    private List<Ship> ships;
    private Random random = new Random();
    private boolean shipsPlaced;

    public Board()
    {
        this.statuses = new BoardStatus[BoardSize.COLUMNS][BoardSize.ROWS];

        for (BoardStatus[] row : statuses)
        {
            Arrays.fill(row, BoardStatus.HIDDEN_EMPTY);
        }

        ships = new ArrayList<>();

        ships.add(new Ship(new Coordinate(0, 0), ShipDirection.VERTICAL, ShipType.CARRIER));
        ships.add(new Ship(new Coordinate(2, 0), ShipDirection.VERTICAL, ShipType.BATTLESHIP));
        ships.add(new Ship(new Coordinate(4, 0), ShipDirection.VERTICAL, ShipType.CRUISER));
        ships.add(new Ship(new Coordinate(6, 0), ShipDirection.VERTICAL, ShipType.SUBMARINE));
        ships.add(new Ship(new Coordinate(8, 0), ShipDirection.VERTICAL, ShipType.DESTROYER));

        shipsPlaced = false;
    }

    public BoardStatus getStatus(int x, int y)
    {
        return statuses[x][y];
    }

    public void setStatus(int x, int y, BoardStatus status)
    {
        statuses[x][y] = status;
    }

    public boolean isStatusHidden(int x, int y)
    {
        if (statuses[x][y] == BoardStatus.HIDDEN_EMPTY || statuses[x][y] == BoardStatus.HIDDEN_SHIP)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<Ship> getShips()
    {
        return ships;
    }

    public int getSmallestRemainingShip()
    {
        int smallest = 5;

        for (Ship ship : ships)
        {
            if (ship.isAlive() && ship.getLength() < smallest)
            {
                smallest = ship.getLength();
            }
        }

        return smallest;
    }

    public int getLongestRemainingShip()
    {
        int longest = 2;

        for (Ship ship : ships)
        {
            if (ship.isAlive() && ship.getLength() > longest)
            {
                longest = ship.getLength();
            }
        }

        return longest;
    }

    public boolean areShipsPlaced()
    {
        return shipsPlaced;
    }

    public void setShipsPlaced(boolean shipsPlaced)
    {
        this.shipsPlaced = shipsPlaced;
    }

    public void placeShipsRandom()
    {
        for (Ship ship : ships)
        {
            boolean valid = false;

            while(!valid)
            {
                int direction = random.nextInt(2);

                if (direction == 0)
                {
                    int x = random.nextInt(BoardSize.COLUMNS - ship.getType().getLength());
                    int y = random.nextInt(BoardSize.ROWS);
                    ship.setDirection(ShipDirection.HORIZONTAL);
                    ship.setCoordinate(new Coordinate(x, y));
                }
                else if (direction == 1)
                {
                    int x = random.nextInt(BoardSize.COLUMNS);
                    int y = random.nextInt(BoardSize.ROWS - ship.getType().getLength());
                    ship.setDirection(ShipDirection.VERTICAL);
                    ship.setCoordinate(new Coordinate(x, y));
                }

                if (!isCollidingWithAny(ship))
                {
                    valid = true;
                }
            }
        }
    }

    private boolean isColliding(Ship ship1, Ship ship2)
    {
        if (ship1 != ship2)
        {
            for (Coordinate coordinate : ship1.getListCoordinates())
            {
                for (Coordinate coordinate2 : ship2.getListCoordinates())
                {
                    if (coordinate.equals(coordinate2))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isCollidingWithAny(Ship ship)
    {
        for (Ship ship2 : ships)
        {
            if (isColliding(ship, ship2))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isValidBoard()
    {
        for (Ship ship : ships)
        {
            if (isCollidingWithAny(ship))
            {
                return false;
            }
        }

        return true;
    }

    public void confirmShipLocations()
    {
        if (isValidBoard())
        {
            for (Ship ship : ships)
            {
                for (Coordinate coordinate : ship.getListCoordinates())
                {
                    int x = coordinate.getX();
                    int y = coordinate.getY();

                    setStatus(x, y, BoardStatus.HIDDEN_SHIP);
                }
            }
        }
    }

    public Ship shipToSink()
    {
        for (Ship ship : ships)
        {
            boolean alive = false;

            for (Coordinate coordinate : ship.getListCoordinates())
            {
                int x = coordinate.getX();
                int y = coordinate.getY();

                if (statuses[x][y] != BoardStatus.HIT)
                {
                    alive = true;
                    break;
                }
            }

            if (!alive)
            {
                return ship;
            }
        }

        return null;
    }

    public void sinkShips()
    {
        if (shipToSink() != null)
        {
            Ship ship = shipToSink();

            for (Coordinate coordinate : ship.getListCoordinates())
            {
                int x = coordinate.getX();
                int y = coordinate.getY();

                statuses[x][y] = BoardStatus.SUNK;
                ship.setAlive(false);
            }
        }
    }

    public boolean allShipsSunk()
    {
        for (Ship ship : ships)
        {
            if (ship.isAlive())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(this.statuses);
        dest.writeTypedList(this.ships);
    }

    protected Board(Parcel in)
    {
        this.statuses = (BoardStatus[][]) in.readSerializable();
        this.ships = in.createTypedArrayList(Ship.CREATOR);
    }

    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>()
    {
        @Override
        public Board createFromParcel(Parcel source)
        {
            return new Board(source);
        }

        @Override
        public Board[] newArray(int size)
        {
            return new Board[size];
        }
    };
}
