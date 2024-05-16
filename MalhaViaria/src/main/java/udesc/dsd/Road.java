package udesc.dsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Road {

    private Cell[][] grid;
    private int maxVehicles;
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Vehicle> entranceQueue = Collections.synchronizedList(new ArrayList<>());

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

                    boolean isEntrance = isEntrance(type, pos);
                    boolean isCross = isCross(type);
                    boolean isExit = isExit(type, pos);

                    Cell cell = factory.createCell(pos, type, isEntrance, isCross, isExit);
                    grid[i][j] = cell;

                    if(isEntrance)
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

    public void addToEntranceQueue(Vehicle vehicle){
        entranceQueue.add(vehicle);
    }

    public void runVehicleEntrance(){

        vehicleEntrance = true;
        new Thread(() -> {
            while (vehicleEntrance) {
                if (vehicles.size() < maxVehicles) {
                    try {
                        addVehicle(entranceQueue.get(0));
                        entranceQueue.remove(0);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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

    public void addVehicle(Vehicle v) throws InterruptedException {
        v.setCell(getEmptyEntrance());
        vehicles.add(v);
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

    private boolean isExit(CellType type, Position pos){
        int rows = grid.length;
        int cols = grid[0].length;

        return ((type == CellType.ROAD_DOWN && pos.y == rows - 1) ||
                (type == CellType.ROAD_UP  && pos.y == 0) ||
                (type == CellType.ROAD_RIGHT && pos.x == cols - 1) ||
                (type == CellType.ROAD_LEFT && pos.x == 0));

    }

    public List<Vehicle> getVehicles(){
        return this.vehicles;
    }

    public Cell[][] getGrid(){
        return grid;
    }

    public Cell cellAtUp(Position pos){
        try{
            return grid[pos.y + 1][pos.y];
        } catch (ArrayIndexOutOfBoundsException ex){
            return null;
        }
    }

    public Cell cellAtDown(Position pos) {
        try {
            return grid[pos.y - 1][pos.x];
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
