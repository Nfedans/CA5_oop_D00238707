package org.example;

import java.io.IOException;
import java.util.*;
import java.util.PriorityQueue;

/**
 * Nikita Fedans project
 *
 */
public class App 
{
    Perfume perfume;
    WholeSaler wholeSaler;

    public static void main(String[] args)
    {
        App app = new App();
        app.start();
    }

    public void start()
    {
        System.out.println("Projects part 1 - CA5");
        ArrayList<Perfume> perfumes = new ArrayList<>();
        Map<String, WholeSaler> mapOfOrigin = new HashMap<>();
        Map<Integer, Perfume> StockAmountMap = new TreeMap<>();
        PriorityQueue<Perfume> queue = new PriorityQueue<>();
        PriorityQueue<Perfume> twoFieldQueue = new PriorityQueue<>(new BrandStockComparator());

        initialize(perfumes, mapOfOrigin, StockAmountMap, twoFieldQueue);

        try {
            displayMainMenu(perfumes, mapOfOrigin, StockAmountMap, queue, twoFieldQueue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Program Over");

    }

    private void displayMainMenu(ArrayList<Perfume> perfumes, Map<String, WholeSaler> mapOfOrigin, Map<Integer, Perfume> StockAmountMap, PriorityQueue<Perfume> queue, PriorityQueue<Perfume> twoFieldQueue) throws IOException {

        final String MENU_ITEMS = "\nMAIN MENU\n"
                + "1. View All Perfume\n"
                + "2. Retrieve WholeSaler by Perfume\n"
                + "3. display the objects from the TreeMap\n"
                + "4. PriorityQueue Sequence Simulation\n"
                + "5. PriorityQueue Two-Field (Brand, stockLvl)\n"
                + "6. Exit\n"
                + "Enter Option [1,6]";

        final int VIEW_PERFUME = 1;
        final int RETRIEVE_WHOLESALER_PERFUME = 2;
        final int DISPLAY_OBJECTS_FROM_TREEMAP = 3;
        final int PRIORITYQUEUE_SEQUENCE_SIMULATION = 4;
        final int PRIORITYQUEUE_BRAND_STOCK = 5;
        final int EXIT = 6;

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

                        for(Perfume p: perfumes) {
                            System.out.println("------------------------------");
                            System.out.println("\tID : " + p.get_id());
                            System.out.println("\tBrand : " + p.getBrand());
                            System.out.println("\tName : " + p.getName());
                            System.out.println("\tBottle size (ml) : " + p.getSize());
                            System.out.println("\tPrice : €" + p.getPrice());
                            System.out.println("\tTarget Gender : " + p.getGender());
                            System.out.println("\tStock Available : " + p.getStockLvl());
                        }
                            break;
                    case RETRIEVE_WHOLESALER_PERFUME:
                        System.out.println(" ___ Find Wholesaler of a Perfume ___  ");
                        System.out.println("Enter Perfume ID: ");
                        String id = keyboard.nextLine();

                        WholeSaler ws = mapOfOrigin.get(id);
                        System.out.println(ws);
                        break;
                    case DISPLAY_OBJECTS_FROM_TREEMAP:
                        System.out.println(" ___ Displaying Treemap Objects ___ ");

                        for (Map.Entry<Integer, Perfume> entry : StockAmountMap.entrySet()) {
                            System.out.println("Key: " + entry.getKey() + ".\t Value: " + entry.getValue());
                        }
                        break;
                    case PRIORITYQUEUE_SEQUENCE_SIMULATION:
                        System.out.println(" ___ Sequence Below ___  ");

                        // add two third-priority elements
                        queue.add(perfumes.get(0));
                        queue.add(perfumes.get(1));

                        // add two second-priority level items
                        queue.add(perfumes.get(7));
                        queue.add(perfumes.get(8));

                        // remove and display one element
                        System.out.println("Remove and display a single element");
                        System.out.println(queue.remove());

                        // add one top-priority element
                        queue.add(perfumes.get(4));

                        // remove and display all elements in priority order
                        System.out.println("\nRemove and display all elements");
                        while ( !queue.isEmpty() ) {
                            System.out.println(queue.remove());
                        }
                        break;
                    case PRIORITYQUEUE_BRAND_STOCK:
                        System.out.println("Priority queue, sorting by brand name alphabetically & stockLevel, high to low");

                        while ( !twoFieldQueue.isEmpty() ) {
                            System.out.println(twoFieldQueue.remove());
                        }
                        break;
                    case EXIT:
                        System.out.println("Menu Exited");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range [1,6]");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range [1,6]");
            }
        } while (option != EXIT);

        System.out.println("\nExiting Main Menu, goodbye.");

    }

    private void initialize( List list, Map<String, WholeSaler> mapOfOrigin, Map<Integer, Perfume> StockAmountMap, PriorityQueue<Perfume> twoFieldQueue)
    {
        Perfume p1 = new Perfume("p_000001", "Calvin Klein", "One", 50, 34.99, "Male", 131);  // .7
        Perfume p2 = new Perfume("p_000002","Calvin Klein", "One", 100, 69.98, "Male", 32); // .7
        Perfume p3 = new Perfume("p_000003","Dior", "Sauvage", 50, 64.99, "Male", 288); // 1.3
        Perfume p4 = new Perfume("p_000004","Dior", "Sauvage", 100, 110, "Male", 209);  // 1.1
        Perfume p5 = new Perfume("p_000005","Chanel", "Bleu de chanel", 50, 70, "Male", 138); // 1.4
        Perfume p6 = new Perfume("p_000006","Chanel", "Bleu de chanel", 100, 125, "Male", 53); //1.25
        Perfume p7 = new Perfume("p_000007","Dior", "Homme", 50, 44.99, "Male", 51); // 0.9
        Perfume p8 = new Perfume("p_000008","Dior", "Homme Sport", 75, 60, "Male", 106); // 0.8
        Perfume p9 = new Perfume("p_000009","Armani", "Code", 100, 80, "Male", 89); // 0.8
        Perfume p10 = new Perfume("p_000010","Davidoff", "Cool Water", 200, 69.99, "Male", 10); // 0.35

        WholeSaler ws1 = new WholeSaler("3928436", "1 alex street", "UK");
        WholeSaler ws2 = new WholeSaler("9562098", "16 Monsoir Avenue", "France");
        WholeSaler ws3 = new WholeSaler("0765463", "44 Uber Strasse", "Germany");

        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        list.add(p7);
        list.add(p8);
        list.add(p9);
        list.add(p10);

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



}
