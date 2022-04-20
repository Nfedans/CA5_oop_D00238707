package org.example.multithread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTOs.Perfume;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client
{
    List<Perfume> perfumes;

    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }

    public void start()
    {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket
            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort() );

            System.out.println("Client message: The Client is running and has connected to the server");

            this.perfumes = new ArrayList<>();

            final String MENU_ITEMS = "\nMAIN MENU COMMANDS\n"
                    + "1. DisplayAllPerfume\t\tView All Perfume\n"
                    + "2. DisplayPerfumeById\t\texample: DisplayPerfumeById 13\n"
//
//                    + "2. Retrieve WholeSaler by Perfume\n"
//                    + "3. display the objects from the TreeMap\n"
//                    + "4. PriorityQueue Sequence Simulation\n"
//                    + "5. PriorityQueue Two-Field (Brand, stockLvl)\n"
//                    + "6. Find all perfume from database\n"
//                    + "7. Find one perfume from database by ID\n"
//                    + "8. Delete one perfume from database by ID\n"
//                    + "9. Add perfume to database\n"
//                    + "10. List perfumes filtered by price\n"
//                    + "11. Find all perfume from database as JSON\n"
//                    + "12. Find one perfume from database by ID as JSON\n"
                    + "3. Exit\n"
                    + "Enter Option [1,3]";
            System.out.println("\n" + MENU_ITEMS);
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers

            socketWriter.println(command);

            Scanner socketReader = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply

//            boolean continueLoop=true;
            while(!command.equals("Exit")) {

                if (command.equals("DisplayAllPerfume"))  //we expect the server to return a time
                {
                    String res = socketReader.nextLine();

                    Type perfList = new TypeToken<ArrayList<Perfume>>(){}.getType();
                    List<Perfume> gottenList = new Gson().fromJson(res, perfList);

                    displayList(gottenList);
                }
                else if(command.startsWith("DisplayPerfumeById"))
                {
                    String res = socketReader.nextLine();
                    if(res.equals("No Perfume found"))
                    {
                        System.out.println(res);
                    }
                    else
                    {
                        Gson gsonParser = new Gson();
                        Perfume result = gsonParser.fromJson(res, Perfume.class);
                        display(result);
                    }
//                    System.out.println("Client message: Response from server: " + input);
                }
                else if(command.startsWith("Triple"))
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: " + input);
                }
                else if(command.startsWith("Add"))
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: " + input);
                }
                else// the user has entered the Echo command or an invalid command
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: \"" + input + "\"");
                }

                System.out.println("Please enter new command");
                command = in.nextLine();
                socketWriter.println(command);

            }
            socketWriter.close();
            socketReader.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client message: IOException: "+e);
        }


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

    public void display(Perfume p)
    {
            System.out.println("------------------------------");
            System.out.println("\tID : " + p.get_id());
            System.out.println("\tBrand : " + p.getBrand());
            System.out.println("\tName : " + p.getName());
            System.out.println("\tBottle size (ml) : " + p.getSize());
            System.out.println("\tPrice : €" + p.getPrice());
            System.out.println("\tTarget Gender : " + p.getGender());
            System.out.println("\tStock Available : " + p.getStockLvl());
            System.out.println("------------------------------");
    }

}
