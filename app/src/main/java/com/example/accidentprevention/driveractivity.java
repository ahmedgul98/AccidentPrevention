package com.example.accidentprevention;

public class driveractivity {

    float gyroscopex;
    float gyroscopey;
    float gyroscopez;
    float accelerometerx;
    float accelerometery;
    float accelerometerz;
    int loud;
    int movement;

    public driveractivity(float gyroscopex, float gyroscopey, float gyroscopez, float accelerometerx, float accelerometery, float accelerometerz, int loud, int movement) {
        this.gyroscopex = gyroscopex;
        this.gyroscopey = gyroscopey;
        this.gyroscopez = gyroscopez;
        this.accelerometerx = accelerometerx;
        this.accelerometery = accelerometery;
        this.accelerometerz = accelerometerz;
        this.loud = loud;
        this.movement = movement;
    }
    public driveractivity(){

    }
}
