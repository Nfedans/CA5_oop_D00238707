package org.example;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Nikita Fedans project
 *
 */
public class App 
{
    Perfume perfume;
    PerfumeManager perfumeManager;
    //ArrayList<Perfume> perfumes;

    public static void main(String[] args)
    {
        App app = new App();
        app.start();
    }

    public void start()
    {
        System.out.println("Projects part 1 - CA5");
        ArrayList<Perfume> perfumes = new ArrayList<>();
        initialize(perfumes);

        try {
            displayMainMenu(perfumes);        // User Interface - Menu
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Program Over");

    }

    private void displayMainMenu(ArrayList<Perfume> perfumes) throws IOException {

        final String MENU_ITEMS = "\nMAIN MENU\n"
                + "1. View All Perfume\n"
                + "2. Exit\n"
                + "Enter Option [1,2]";

        final int VIEW_PERFUME = 1;
        final int EXIT = 2;

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
                    case EXIT:
                        System.out.println("Menu Exited");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range");
            }
        } while (option != EXIT);

        System.out.println("\nExiting Main Menu, goodbye.");

    }



    private void initialize( List list)
    {
        Perfume p1 = new Perfume("p_000001", "Calvin Klein", "One", 50, 34.99, "Male", 131);
        Perfume p2 = new Perfume("p_000002","Calvin Klein", "One", 100, 59.99, "Male", 32);
        Perfume p3 = new Perfume("p_000003","Dior", "Sauvage", 50, 64.99, "Male", 288);
        Perfume p4 = new Perfume("p_000004","Dior", "Sauvage", 100, 110, "Male", 209);
        Perfume p5 = new Perfume("p_000005","Chanel", "Bleu de chanel", 50, 70, "Male", 138);
        Perfume p6 = new Perfume("p_000006","Chanel", "Bleu de chanel", 100, 125, "Male", 53);
        Perfume p7 = new Perfume("p_000007","Dior", "Homme", 50, 44.99, "Male", 51);
        Perfume p8 = new Perfume("p_000008","Dior", "Homme Sport", 75, 57, "Male", 106);
        Perfume p9 = new Perfume("p_000009","Armani", "Code", 100, 80, "Male", 89);
        Perfume p10 = new Perfume("p_000010","Davidoff", "Cool Water", 200, 69.99, "Male", 10);


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
    }

    /*
    public void displayAllPerfumes(List perfumeList) {
        for (Perfume p : perfumeList) {
            System.out.println(p.toString());
        }
    } */

}
