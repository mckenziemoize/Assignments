import java.io.*;
import java.util.*;

public class Assignment5 {
    public static void main(String[] args) {
        // The address of the TSV file
        final String DATA_ADDRESS = "IMDBDataset.tsv";
        
        // Determine the vertices of the graph
        String[][] movies = getPeople(DATA_ADDRESS); 
        
        Graph graph = new Graph(movies);
        
//        graph.printAll();
        
        System.out.println("Anne Hathaway has a degree of " + 
                graph.degreeOf("Anne Hathaway"));
        System.out.println(graph.maxDegree());
    }
    
    /**
     * Method to convert the List of Lists from the TSV into a more 
     * manageable array
     * 
     * @param address The location of the TSV File
     * @return A two-dimensional array containing the movie and actors/actresses
     */
    public static String[][] getPeople(String address) {
        List<List<String>> movies = TSV.read(address);
        
        movies.remove(0); // Remove the title
        
        // Remove movie title and number from each movie
        for (List<String> movie : movies) {
            movie.remove(0);
            movie.remove(0);
        }
        
        String[][] people = new String[movies.size()][];
        
        for (int i = 0; i < people.length; i++) {
            people[i] = movies.get(i).get(0).split(",");
            
            // Trim the white space for each person
            for (int j = 0; j < people[i].length; j++) {
                people[i][j] = people[i][j].trim();
            }
        }
        
        return people;
    }
}

/**
 * Class to parse a TSV file
 */
class TSV {
    // Privately parse the file so the Exceptions are not carried
    private static List<List<String>> getData(String address) throws Exception {
        List<List<String>> movies = new ArrayList<>();
        
        StringTokenizer st;
        BufferedReader TSVFile = new BufferedReader(new FileReader(address));
        String dataRow = TSVFile.readLine();

        while (dataRow != null) {
            st = new StringTokenizer(dataRow, "\t");
            List<String> dataArray = new ArrayList<String>();

            while (st.hasMoreElements()) {
                dataArray.add(st.nextElement().toString());
            }
            
            dataRow = TSVFile.readLine();
            
            movies.add(dataArray);
        }

        TSVFile.close(); // Close the file once all data has been read

        return movies;
    }
    
    /**
     * Reads the file at the given address and returns a List of each line, 
     * which contains a List of 0) Number, 1) Title, 2) Actors.
     * 
     * @param address Location of the file being parsed
     * @return List of Lists of Strings containing data from the TSV File
     */
    public static List<List<String>> read(String address) {
        try {
            return getData(address);
        } catch (Exception ex) {
            System.out.println("Something went wrong...");
        }
        
        return null;
    }
}

/**
 * Class to store the graph of IMDB Data
 */
class Graph {
    // Store the vertices as key<String> and the edges as 
    // values<LinkedList<String>>
    Map<String, LinkedList<String>> vertices;
    
    public Graph(String[][] movies) {
        vertices = new TreeMap<String, LinkedList<String>>(); // Use a TreeMap to keep the keys sorted
        
        setGraph(movies);
    }
    
    // Fills in the edges from the given data
    private void setGraph(String[][] movies) {
        for (String[] currentMovie : movies) {
            // Iterate over the people in the movie
            for (int i = 0; i < currentMovie.length; i++) {
                // Store the current actor
                String currentActor = currentMovie[i];

                // Stores all the costars in this movie
                LinkedList<String> costars = new LinkedList<String>();

                // Iterates through the actors in the movie
                for (int j = 0; j < currentMovie.length; j++) {
                    // Ignore the current actor
                    if (i != j) {
                        costars.add(currentMovie[j]); // Add the costar
                    }
                }

                // Check to see if the actor has already been found
                if (!vertices.containsKey(currentActor)) {
                    // If actor is fresh, just add to map with current linked list
                    vertices.put(currentActor, costars); 
                } else {
                    // If actor is old, add non duplicates
                    LinkedList<String> oldStars = vertices.get(currentActor);

                    for (int j = 0; j < costars.size(); j++) {
                        // The currently referenced costar
                        String currentCostar = costars.get(j);

                        if (!oldStars.contains(currentCostar)) {
                            oldStars.add(currentCostar);
                        }
                    }

                    vertices.put(currentActor, oldStars);
                }
            }
        }
    }
    
    /**
     * Perform a Breath-First-Search:
     * Each vertex is accessed then all of its adjacent vertices are added to
     * a queue. Then do the same for each vertex in the queue
     * 
     * @param current The source of the graph
     * @return The order in which the graph was searched
     */
    public String BFS(String current) {
        String output = ""; // Store the output
        
        // Store the visited vertices
        Map<String, Boolean> visited = new HashMap<String, Boolean>();
        for (String vertex : vertices.keySet()) {
            visited.put(vertex, false); // Set them all initially to be false
        }
        
        // Queue for the search
        LinkedList<String> queue = new LinkedList<String>();
        
        visited.put(current, true); // Mark the current vertex as visited 
        queue.add(current); // Enqueue it
        output += "Queue: [";
        
        while (!queue.isEmpty()) {
            current = queue.poll(); // Get the top value from the queue
            output += current + ", ";
            
            // Create an iterator for the queue
            Iterator<String> i = vertices.get(current).listIterator();
            
            while (i.hasNext()) {
                String next = i.next();
                
                if (!visited.get(next)) {
                    visited.put(next, true);
                    queue.add(next);
                }
            }
        }
        
        return output + "\b\b]";
    }
    
    /**
     * Method to display every actor/actress along with their corresponding
     * edges.
     */
    public void printAll() {
        int count = 1; // Numerate each line
        
        // Iterate over each vertex
        for (String person : vertices.keySet()) {
            System.out.println(count++ + ". " + person + ": " + vertices.get(person));
        }
    }
    
    /**
     * Return the degree of the vertex, which is the numbers of connected edges
     * 
     * @param person The vertex being queried
     * @return The degree of the person
     */
    public int degreeOf(String person) {
        return vertices.get(person).size();
    }
    
    /**
     * Determine the person or people with the highest degree
     * 
     * @return The people with the highest degree
     */
    public String maxDegree() {
        int max = 0; 
        
        // Store the max person in a list, in case multiple people have the 
        // same degree
        List<String> maxPerson = new ArrayList<String>();
        
        // Iterate through each person in the graph
        for (String person : vertices.keySet()) {
            int currentMax = degreeOf(person);
            
            if (currentMax > max) {
                max = currentMax; // Update max value
                
                maxPerson.clear(); // Reset the list of max people
                maxPerson.add(person); // Add current person to list
            } else if (currentMax == max) {
                maxPerson.add(person); // Add current person to list
            }
        }
        
        String output = "The max degree is ";
        
        if (maxPerson.size() > 1) {
            output += "a tie between " + maxPerson.toString();
        } else {
            output += maxPerson.get(0);
        }
        
        return output +  " with a degree of " + max;
    }
}