package com.example.laba3.Ship;

public enum ShipType {
    CARRIER(1, "Aircraft Carrier", 5), BATTLESHIP(2, "Battleship", 4), CRUISER(3, "Cruiser", 3), SUBMARINE(4, "Submarine", 3), DESTROYER(5, "Destroyer", 2);

    private int id;
    private String name;
    private int length;

    ShipType(int id, String name, int length)
    {
        this.id = id;
        this.name = name;
        this.length = length;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getLength()
    {
        return length;
    }
}
