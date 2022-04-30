package org.example.multithread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.BusinessObjects.BrandStockComparator;
import org.example.DAOs.MySqlPerfumeDao;
import org.example.DAOs.PerfumeDaoInterface;
import org.example.DTOs.Perfume;
import org.example.DTOs.SummaryData;
import org.example.DTOs.WholeSaler;
import org.example.Exceptions.DaoException;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server
{
    List<Perfume> perfumes;
    Map<Integer, WholeSaler> mapOfOrigin;
    Map<Integer, Perfume> StockAmountMap;
    PriorityQueue<Perfume> queue;
    PriorityQueue<Perfume> queueDBFiltered;
    List<Perfume> listDBFiltered;
    PriorityQueue<Perfume> twoFieldQueue;
    PerfumeDaoInterface IPerfumeDao = new MySqlPerfumeDao();


    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start()
    {
        System.out.println("Projects part 1 - CA5");
        this.perfumes = new ArrayList<>();
        this.mapOfOrigin = new HashMap<Integer, WholeSaler>();
        this.StockAmountMap = new TreeMap<>();
        this.queue = new PriorityQueue<>();
        this.queueDBFiltered = new PriorityQueue<>();
        this.twoFieldQueue = new PriorityQueue<>(new BrandStockComparator());
        initialize();

        try
        {
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect

            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection,
                // and open a new socket to communicate with the client
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber, IPerfumeDao)); // create a new ClientHandler for the client,
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        } catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {


        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber, PerfumeDaoInterface IPerfumeDao)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {

            String message;
            try
            {
                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);

                    if (message.equals("DisplayAllPerfume"))
                    {
                        socketWriter.println(findAllPerfumeJSON());  // sends current time to client
                    }
                    else if (message.startsWith("DisplayPerfumeById"))
                    {

                        String[] tokens = message.split(" ");
                        String param1 = tokens[0];
                        String param2 = tokens[1];
                        System.out.println("param1 == " + param1 + "\tparam2 == " + param2);

                        try {
                            String res = findPerfumeByIDJSON(param2);

                            Integer.parseInt(param2);

                            System.out.println("res == " + res);

                            socketWriter.println(findPerfumeByIDJSON(param2));

                            message = message.substring(5);
                            socketWriter.println(message);
                        }
                        catch (Exception e)
                        {
                            socketWriter.println("No Perfume found");
                        }


                    }
                    else if (message.startsWith("AddPerfumeToDb"))
                    {

                        System.out.println("Arriving from server: " + message);

                        String msg = socketReader.readLine();

                        Gson gsonParser = new Gson();
                        Perfume perfume1 = gsonParser.fromJson(msg, Perfume.class);
                        try {
                            IPerfumeDao.addPerfume(perfume1.getBrand(), perfume1.getName(), perfume1.getSize(), perfume1.getPrice(), perfume1.getGender(), perfume1.getStockLvl());
                            String g = perfume1.getName() + " " + perfume1.getBrand() + " " + perfume1.getSize();
                            String result = IPerfumeDao.findPerfumeByNameBrandSizeJSON(g);
                            socketWriter.println(result);
                        }
                        catch( DaoException e )
                        {
                            e.printStackTrace();
                            socketWriter.println(" Add Unsuccessful :( Please try again");
                        }
                    }
                    else if (message.startsWith("DelPerfumeByID"))
                    {
                        String[] tokens = message.split(" ");
                        String param1 = tokens[0];
                        String param2 = tokens[1];
                        System.out.println("param1 == " + param1 + "\tparam2 == " + param2);

                        String postActionMsg = deletePerfumeByID(param2);

                        socketWriter.println(postActionMsg);
                    }
                    else if (message.equals("GetProductData"))
                    {
                        String serialized = findAllPerfumeJSON();

                        Type perfList = new TypeToken<ArrayList<Perfume>>(){}.getType();
                        List<Perfume> gottenList = new Gson().fromJson(serialized, perfList);

                        String summaryObject = getStats(gottenList);

                        socketWriter.println(summaryObject);
                    }
                    else
                    {
                        socketWriter.println("I'm sorry I don't understand :(");
                    }
                }

                socket.close();

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }


    private void initialize()
    {
        Perfume p1 = new Perfume(1, "Calvin Klein", "One", 50, (float) 34.99, "Male", 131);  // .7
        Perfume p2 = new Perfume(2,"Calvin Klein", "One", 100, (float) 69.98, "Male", 32); // .7
        Perfume p3 = new Perfume(3,"Dior", "Sauvage", 50, (float) 64.99, "Male", 288); // 1.3
        Perfume p4 = new Perfume(4,"Dior", "Sauvage", 100, (float) 110, "Male", 209);  // 1.1
        Perfume p5 = new Perfume(5,"Chanel", "Bleu de chanel", 50, (float) 70, "Male", 138); // 1.4
        Perfume p6 = new Perfume(6,"Chanel", "Bleu de chanel", 100, (float) 125, "Male", 53); //1.25
        Perfume p7 = new Perfume(7,"Dior", "Homme", 50, (float) 44.99, "Male", 51); // 0.9
        Perfume p8 = new Perfume(8,"Dior", "Homme Sport", 75, (float) 60, "Male", 106); // 0.8
        Perfume p9 = new Perfume(9,"Armani", "Code", 100, (float) 80, "Male", 89); // 0.8
        Perfume p10 = new Perfume(10,"Davidoff", "Cool Water", 200, (float) 69.99, "Male", 10); // 0.35

        WholeSaler ws1 = new WholeSaler("3928436", "1 alex street", "UK");
        WholeSaler ws2 = new WholeSaler("9562098", "16 Monsoir Avenue", "France");
        WholeSaler ws3 = new WholeSaler("0765463", "44 Uber Strasse", "Germany");

        perfumes.add(p1);
        perfumes.add(p2);
        perfumes.add(p3);
        perfumes.add(p4);
        perfumes.add(p5);
        perfumes.add(p6);
        perfumes.add(p7);
        perfumes.add(p8);
        perfumes.add(p9);
        perfumes.add(p10);

        mapOfOrigin.put(p1.get_id(), ws2);
        mapOfOrigin.put(p2.get_id(), ws2);
        mapOfOrigin.put(p3.get_id(), ws2);
        mapOfOrigin.put(p4.get_id(), ws1);
        mapOfOrigin.put(p5.get_id(), ws3);
        mapOfOrigin.put(p6.get_id(), ws1);
        mapOfOrigin.put(p7.get_id(), ws2);
        mapOfOrigin.put(p8.get_id(), ws3);
        mapOfOrigin.put(p9.get_id(), ws2);
        mapOfOrigin.put(p10.get_id(), ws1);

        StockAmountMap.put(p1.getStockLvl(), p1);
        StockAmountMap.put(p2.getStockLvl(), p2);
        StockAmountMap.put(p3.getStockLvl(), p3);
        StockAmountMap.put(p4.getStockLvl(), p4);
        StockAmountMap.put(p5.getStockLvl(), p5);
        StockAmountMap.put(p6.getStockLvl(), p6);
        StockAmountMap.put(p7.getStockLvl(), p7);
        StockAmountMap.put(p8.getStockLvl(), p8);
        StockAmountMap.put(p9.getStockLvl(), p9);
        StockAmountMap.put(p10.getStockLvl(), p10);

        twoFieldQueue.add(p1);
        twoFieldQueue.add(p2);
        twoFieldQueue.add(p3);
        twoFieldQueue.add(p4);
        twoFieldQueue.add(p5);
        twoFieldQueue.add(p6);
        twoFieldQueue.add(p7);
        twoFieldQueue.add(p8);
        twoFieldQueue.add(p9);
        twoFieldQueue.add(p10);
    }

    public String getStats(List<Perfume> gottenList)
    {
        Gson gson = new Gson();

        int totalStock = 0;
        int totalProduct = 0;
        for(Perfume p: gottenList)
        {
            totalStock = totalStock + p.getStockLvl();
            totalProduct++;
        }

        SummaryData data = new SummaryData(totalStock, totalProduct);

        String jsonString = gson.toJson(data);

        return jsonString;
    }

    public String displayList(List <Perfume> list)
    {
        Gson gson = new Gson();

        String jsonString = gson.toJson(list);
//        String res = "";
//
//        for(Perfume p: list) {
//            res = res + "------------------------------";
//            res = res + "\tID : " + p.get_id();
//            res = res + "\tBrand : " + p.getBrand();
//            res = res + "\tName : " + p.getName();
//            res = res + "\tBottle size (ml) : " + p.getSize();
//            res = res + "\tPrice : €" + p.getPrice();
//            res = res + "\tTarget Gender : " + p.getGender();
//            res = res + "\tStock Available : " + p.getStockLvl();
//        }
        return jsonString;
    }

    public void displayStockAmountMap()
    {
        for (Map.Entry<Integer, Perfume> entry : StockAmountMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ".\t Value: " + entry.getValue());
        }
    }

    public void displayTwoFieldQueue()
    {
        while ( !twoFieldQueue.isEmpty() ) {
            System.out.println(twoFieldQueue.remove());
        }
    }

    public void priorityQueueSequence()
    {
        // add two third-priority elements
        queue.add(perfumes.get(0));
        queue.add(perfumes.get(1));

        // add two second-priority level items
        queue.add(perfumes.get(7));
        queue.add(perfumes.get(8));

        // remove and display one element
        System.out.println("Remove and display a single element");
        Perfume p = queue.remove();
        System.out.println(p.toString() + "  -  Price per ml: €" + (p.getPrice()/p.getSize()));

        // add one top-priority element
        queue.add(perfumes.get(4));

        // remove and display all elements in priority order
        System.out.println("\nRemove and display all elements");
        while ( !queue.isEmpty() ) {
            Perfume r = queue.remove();
            System.out.println(r.toString() + "\t-\tPrice per ml: €" + (Double.valueOf(Math.round((r.getPrice()/r.getSize()) * 100)) / 100) );
        }
    }

    public void findAllPerfume()
    {
        try
        {
            System.out.println("\nCall findAllPerfumes()");
            List<Perfume> perfumes = IPerfumeDao.findAllPerfume();

            if( perfumes.isEmpty() )
                System.out.println("There are no Users");
            else {
                for (Perfume perfume : perfumes)
                    System.out.println(perfume.toString());
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public String findAllPerfumeJSON()
    {
        try
        {
            System.out.println("\nCall findAllPerfumesJSON()");
            String jsonString = IPerfumeDao.findAllPerfumeJSON();

            if(jsonString.equals("null"))
                return "No Perfume found";
            else {
                return jsonString;
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
        return "No Perfume found";
    }

    public void findPerfumeByID(String id)
    {
        try
        {
            System.out.println("findPerfumeByID()");
            Perfume perfume = IPerfumeDao.findPerfumeByID(id);

            if(perfume == null)
                System.out.println("No perfume exists with ID: " + id);
            else {
                System.out.println(perfume);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public String findPerfumeByIDJSON(String id)
    {
        try
        {
            System.out.println("findPerfumeByIDJSON()");
            String jsonString = IPerfumeDao.findPerfumeByIDJSON(id);

            if(jsonString.equals("null"))
                return "No Perfume found";
            else {
                return jsonString;
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
        return "No Perfume found";
    }

    public String deletePerfumeByID(String id)
    {
        try
        {
            System.out.println("deletePerfumeByID()");
            Perfume perfume1 = IPerfumeDao.findPerfumeByID(id);
            IPerfumeDao.deletePerfumeByID(id);
            Perfume perfume2 = IPerfumeDao.findPerfumeByID(id);
            String returnVal= "";

            if(perfume1 != null && perfume2 == null)
            {
                returnVal = "Perfume Deleted Successfully";
                System.out.println(returnVal);
            }
            else if(perfume1 == null)
            {
                returnVal = "The Perfume with id = " + id + " wasn't in the database";
                System.out.println(returnVal);
            }
            else {
                returnVal = "Perfume deletion has failed";
                System.out.println(returnVal);
            }
            return returnVal;

        }
        catch( DaoException e )
        {
            e.printStackTrace();
            return "NA";
        }

    }

    public void addPerfumeToDB()
    {
        String brand = "";
        String name = "";
        int enteredSize = 0;
        float enteredPrice = -1;
        String gender  = "";
        int enteredStockLvl = -1;
        Scanner kb = new Scanner(System.in);


        while(brand == "") {
            System.out.println("Please enter Perfume Brand: ");
            brand = kb.nextLine();
        };

        while(name == "") {
            System.out.println("Please enter Perfume Name: ");
            name = kb.nextLine();
        };

        while(enteredSize < 1) {
            System.out.println("Please enter Perfume Size (ml): ");
            enteredSize = kb.nextInt();
        };

        while(enteredPrice <= 0) {
            System.out.println("Please enter Perfume price: ");
            String temp = kb.next();

            try
            {
                Float.parseFloat(temp);

            }
            catch (Exception e)
            {

            }
        };

        while(gender == "") {
            System.out.println("Please enter Perfume gender: ");
            gender = kb.next();
        };

        while(enteredStockLvl <= 0) {
            System.out.println("Please enter Perfume Stock Level: ");
            enteredStockLvl = kb.nextInt();
        };

        try {
            IPerfumeDao.addPerfume(brand, name, enteredSize, enteredPrice, gender, enteredStockLvl);

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }

    }

    public void listFilteredPerfumes(float x)
    {
        System.out.println("Showing all perfumes sub " + x + " Euro, ordered by (price/ml), hi->lo");

        try {
            listDBFiltered = IPerfumeDao.findAllPerfumeSubXEuro(x);
            ArrayList<Perfume> fetchedFilteredArrList = new ArrayList<Perfume>();
            fetchedFilteredArrList.addAll(listDBFiltered);

            for(int i = 0; i < fetchedFilteredArrList.size(); i++)
            {
                queueDBFiltered.add(fetchedFilteredArrList.get(i));
            }
            while ( !queueDBFiltered.isEmpty() ) {
                Perfume r = queueDBFiltered.remove();
                System.out.println(r.toString() + "\t-\tPrice per ml: €" + (Double.valueOf(Math.round((r.getPrice()/r.getSize()) * 100)) / 100) );
            }
        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

}
