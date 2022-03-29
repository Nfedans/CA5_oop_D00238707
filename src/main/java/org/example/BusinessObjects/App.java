package org.example.BusinessObjects;

import org.example.DAOs.MySqlPerfumeDao;
import org.example.DAOs.PerfumeDaoInterface;
import org.example.DTOs.Perfume;
import org.example.DTOs.WholeSaler;
import org.example.Exceptions.DaoException;

import java.io.IOException;
import java.util.*;

/**
 * Nikita Fedans project
 *
 */
public class App 
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
        App app = new App();
        app.start();
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

        try {
            displayMainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Program Over");

    }

    private void displayMainMenu() throws IOException {

        final String MENU_ITEMS = "\nMAIN MENU\n"
                + "1. View All Perfume\n"
                + "2. Retrieve WholeSaler by Perfume\n"
                + "3. display the objects from the TreeMap\n"
                + "4. PriorityQueue Sequence Simulation\n"
                + "5. PriorityQueue Two-Field (Brand, stockLvl)\n"
                + "6. Find all perfume from database\n"
                + "7. Find one perfume from database by ID\n"
                + "8. Delete one perfume from database by ID\n"
                + "9. Add perfume to database\n"
                + "10. List perfumes filtered by price\n"
                + "11. Exit\n"
                + "Enter Option [1,11]";

        final int VIEW_PERFUME = 1;
        final int RETRIEVE_WHOLESALER_PERFUME = 2;
        final int DISPLAY_OBJECTS_FROM_TREEMAP = 3;
        final int PRIORITYQUEUE_SEQUENCE_SIMULATION = 4;
        final int PRIORITYQUEUE_BRAND_STOCK = 5;
        final int PULL_ALL_PERFUME_FROM_DB = 6;
        final int PULL_PERFUME_FROM_DB_BY_ID = 7;
        final int DELETE_PERFUME_FROM_DB_BY_ID = 8;
        final int ADD_PERFUME_TO_DB = 9;
        final int LIST_FILTERED = 10;
        final int EXIT = 11;

        Scanner keyboard = new Scanner(System.in);
        int option = 0;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = keyboard.nextLine();
                option = Integer.parseInt(usersInput);
                switch (option) {
                    case VIEW_PERFUME:
                        System.out.println(" ___ List of all Perfumes ___  ");
                        displayList(perfumes);
                            break;
                    case RETRIEVE_WHOLESALER_PERFUME:
                        System.out.println(" ___ Find Wholesaler of a Perfume ___  ");
                        System.out.println("Please enter Perfume ID: ");
                        String id = keyboard.nextLine();
                        int _id = Integer.parseInt(id);
                        WholeSaler ws = mapOfOrigin.get(_id);
                        System.out.println(ws);
                        break;
                    case DISPLAY_OBJECTS_FROM_TREEMAP:
                        System.out.println(" ___ Displaying Treemap Objects ___ ");
                        displayStockAmountMap();
                        break;
                    case PRIORITYQUEUE_SEQUENCE_SIMULATION:
                        System.out.println(" ___ Sequence Below ___  ");
                        priorityQueueSequence();
                        break;
                    case PRIORITYQUEUE_BRAND_STOCK:
                        System.out.println("Priority queue, sorting by brand name alphabetically & stockLevel, low to high");
                        displayTwoFieldQueue();
                        break;
                    case PULL_ALL_PERFUME_FROM_DB:
                        findAllPerfume();
                        break;
                    case PULL_PERFUME_FROM_DB_BY_ID:
                        System.out.println("Please enter Perfume ID: ");
                        String idTag = keyboard.nextLine();
                        findPerfumeByID(idTag);
                        break;
                    case DELETE_PERFUME_FROM_DB_BY_ID:
                        System.out.println("Please enter Perfume ID: ");
                        String idForDeletion = keyboard.nextLine();
                        deletePerfumeByID(idForDeletion);
                        break;
                    case ADD_PERFUME_TO_DB:
                        addPerfumeToDB();
                        break;
                    case LIST_FILTERED:
                        System.out.println("Please enter maximum perfume price: ");
                        float filteringPrice = keyboard.nextFloat();
                        keyboard.nextLine();
                        listFilteredPerfumes(filteringPrice);
                        break;
                    case EXIT:
                        System.out.println("Menu Exited");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range [1,11]");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range [1,11]");
            }
        } while (option != EXIT);

        System.out.println("\nExiting Main Menu, goodbye.");

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



    public void displayList(List <Perfume> list)
    {
        for(Perfume p: list) {
            System.out.println("------------------------------");
            System.out.println("\tID : " + p.get_id());
            System.out.println("\tBrand : " + p.getBrand());
            System.out.println("\tName : " + p.getName());
            System.out.println("\tBottle size (ml) : " + p.getSize());
            System.out.println("\tPrice : €" + p.getPrice());
            System.out.println("\tTarget Gender : " + p.getGender());
            System.out.println("\tStock Available : " + p.getStockLvl());
        }
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

    public void deletePerfumeByID(String id)
    {
        try
        {
            System.out.println("deletePerfumeByID()");
            Perfume perfume1 = IPerfumeDao.findPerfumeByID(id);
            IPerfumeDao.deletePerfumeByID(id);
            Perfume perfume2 = IPerfumeDao.findPerfumeByID(id);

            if(perfume1 != null && perfume2 == null)
            {
                System.out.println("Perfume Deleted Successfully");
            }
            else if(perfume1 == null)
            {
                System.out.println("The Perfume with id = " + id + " wasn't in the database");
            }
            else {
                System.out.println("Perfume deletion has failed");
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
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
            enteredPrice = kb.nextFloat();
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
            System.out.println("Added Successfully");
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
