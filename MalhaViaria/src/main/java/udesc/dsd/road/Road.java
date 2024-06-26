package udesc.dsd.road;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.CellType;
import udesc.dsd.road.cell.Position;
import udesc.dsd.vehicle.Vehicle;
import udesc.dsd.vehicle.VehicleFactory;
import udesc.dsd.factory.CellFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Road {

    private Cell[][] grid;
    private int maxVehicles;
    private final List<Vehicle> vehicles = new CopyOnWriteArrayList<>();
    private boolean vehicleEntrance;
    private final List<Cell> entrances = new ArrayList<>();

    public Road(String file, CellFactory factory, int maxVehicles) {

        this.maxVehicles = maxVehicles;
        String path = "MalhaViaria/src/resources/" + file;

        try (BufferedReader br = new BufferedReader(new FileReader(path))){

            int cols;
            int rows;

            rows = Integer.parseInt(br.readLine().trim());
            cols = Integer.parseInt(br.readLine().trim());
            grid = new Cell[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().split("\t");
                for (int j = 0; j < cols; j++) {

                    Position pos = new Position(j, i);
                    CellType type = CellType.valueOf(Integer.parseInt(line[j]));

                    boolean isCross = isCross(type);
                    Cell cell = factory.createCell(pos, type, isCross);
                    grid[i][j] = cell;

                    if(isEntrance(type, pos))
                        entrances.add(cell);
                }
            }
        } catch (IOException e){
            grid = new Cell[0][0];
        }
    }

    public void start(){
        runVehicleEntrance();
    }

    public void runVehicleEntrance(){

        VehicleFactory vehicleFactory = new VehicleFactory(this);

        vehicleEntrance = true;
        new Thread(() -> {
            while (vehicleEntrance) {
                if (vehicles.size() < maxVehicles) {
                    try {

                        Vehicle v = vehicleFactory.createVehicle();
                        addVehicle(v);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized Cell getEmptyEntrance(){
        Random random = new Random();
        Cell entrance;

        while (true) {
            entrance = entrances.get(random.nextInt(entrances.size()));
            if (entrance.isEmpty()) {
                return entrance;
            }
        }
    }

    public synchronized void addVehicle(Vehicle v) throws InterruptedException {
        v.start();
        vehicles.add(v);
    }

    public synchronized void removeVehicle(Vehicle v){
        vehicles.remove(v);
    }

    private boolean isCross(CellType type){
        return type.getCode() > 4;
    }

    private boolean isEntrance(CellType type, Position pos){
        int rows = grid.length;
        int cols = grid[0].length;

        return ((type == CellType.ROAD_DOWN && pos.y == 0) ||
                (type == CellType.ROAD_UP && pos.y == rows - 1) ||
                (type == CellType.ROAD_RIGHT && pos.x == 0) ||
                (type == CellType.ROAD_LEFT && pos.x == cols - 1));

    }

    public List<Vehicle> getVehicles(){
        return this.vehicles;
    }

    public Cell[][] getGrid(){
        return grid;
    }

    public Cell cellAtUp(Position pos){
        try{
            return grid[pos.y - 1][pos.x];
        } catch (ArrayIndexOutOfBoundsException ex){
            return null;
        }
    }

    public Cell cellAtDown(Position pos) {
        try {
            return grid[pos.y + 1][pos.x];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Cell cellAtRight(Position pos) {
        try {
            return grid[pos.y][pos.x + 1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Cell cellAtLeft(Position pos) {
        try {
            return grid[pos.y][pos.x - 1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean isActive(){
        return vehicleEntrance;
    }

    public void end(){
        vehicleEntrance = false;
    }
}
