import java.util.*;

public class Tsp{
  Kattio io;
  public int numberOfPoints;
  public int[][] distanceMatrix;
  public int[] route;
  public Pair[] listOfPoints;

  void readGraph(){
    numberOfPoints = io.getInt();
    listOfPoints = new Pair[numberOfPoints];
    distanceMatrix = new int[numberOfPoints][numberOfPoints];
    double x;
    double y;

    for (int i = 0; i < numberOfPoints; i++){
      x = io.getDouble();
      y = io.getDouble();
      listOfPoints[i] = new Pair(x,y);
    }
  }

  int euclideanDistance(double x, double y, double x1, double y1){
    return (int) Math.sqrt(Math.pow(x-x1,2) + Math.pow(y-y1,2));
  }

  void createDistanceMatrix(){
    int dist = 0;
    for (int i = 0; i < numberOfPoints; i++){
      for (int j = i; j < numberOfPoints; j++){
        dist = euclideanDistance(listOfPoints[i].getX(), listOfPoints[i].getY(), listOfPoints[j].getX(), listOfPoints[j].getY());
        distanceMatrix[i][j] = dist;
        distanceMatrix[j][i] = dist;
      }
    }
  }

  void writeSolution(){
    for (int i = 0; i < numberOfPoints; i++){
      io.println(route[i]);
    }
  }

  int randomStartingEdge(int min, int max){
    return (int) Math.random() * (max-min+1)+min;
  }

  void nearestNeighbour(){
    route = new int[numberOfPoints];
    route[0] = randomStartingEdge(0, numberOfPoints-1);
    boolean[] visited = new boolean[numberOfPoints];
    visited[route[0]] = true;
    int bestOption;
    for (int i = 1; i < numberOfPoints; i++){
      bestOption = -1;
      for (int j = 0; j < numberOfPoints; j++){
        if (!visited[j] && ( bestOption == -1 || distanceMatrix[route[i-1]][j] < distanceMatrix[route[i-1]][bestOption])){
          bestOption = j;
        }
      }
      route[i] = bestOption;
      visited[bestOption] = true;
    }
  }

  void twoOptSwap(int firstPoint, int secondPoint){
    int temp_upper = 0;
    int temp_lower = 0;
    for (int i = 0; i < (int) Math.ceil((double)(secondPoint - firstPoint)/2); i++ ){
      temp_lower = route[firstPoint + i];
      temp_upper = route[secondPoint - i];
      route[firstPoint+i] = temp_upper;
      route[secondPoint-i] = temp_lower;
    }
  }

  void twoOpt(){
    boolean redo = true;
    while(redo){
      int best_change = 0;
      int current_change = 0;
      int switch_i = 0;
      int switch_j = 0;
      int current_distance = 0;
      int new_distance = 0;
      redo = false;
      for (int i = 0; i < numberOfPoints - 2; i++){
        for (int j = i+2; j < numberOfPoints; j++){
          if (i == 0 && j == numberOfPoints-1){
            continue;
          }
          if (j == numberOfPoints-1 ){
            current_distance = distanceMatrix[route[i]][route[i+1]] + distanceMatrix[route[j]][route[0]];
            new_distance = distanceMatrix[route[i]][route[j]] + distanceMatrix[route[i+1]][route[0]];
          } else {
            current_distance = distanceMatrix[route[i]][route[i+1]] + distanceMatrix[route[j]][route[j+1]];
            new_distance = distanceMatrix[route[i]][route[j]] + distanceMatrix[route[i+1]][route[j+1]];
          }
          current_change = new_distance - current_distance;
          if (best_change > current_change){
            best_change = current_change;
            switch_i = i;
            switch_j = j;
          }
        }
      }
      twoOptSwap(switch_i+1,switch_j);
      if (best_change < 0){
        redo = true;
      }
    }
  }

  Tsp(){
    io = new Kattio(System.in, System.out);
    readGraph();
    createDistanceMatrix();
    nearestNeighbour();
    twoOpt();
    writeSolution();
    io.close();
  }
  public static void main(String[] args){
    new Tsp();
  }

}

class Pair{
     double x,y;
     Pair(double setX,double setY){
         x = setX;
         y = setY;
     }
     double getX(){
         return x;
     }
     double getY(){
         return y;
     }
}
