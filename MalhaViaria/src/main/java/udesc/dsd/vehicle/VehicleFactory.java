package udesc.dsd.vehicle;

import udesc.dsd.road.Road;
import udesc.dsd.util.SysColor;

import java.util.Random;

public class VehicleFactory {
    private final Road road;

    public VehicleFactory(Road road) {
        this.road = road;
    }

    public Vehicle createVehicle(){
        Random rand = new Random();
        int speed = rand.nextInt(200, 1000);
        return new Vehicle(road, speed, SysColor.getVehicleColor());
    }
}
