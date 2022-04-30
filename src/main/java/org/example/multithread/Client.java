package org.example.multithread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTOs.Perfume;
import org.example.DTOs.SummaryData;

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
    Gson gsonParser = new Gson();

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
                    + "3. AddPerfumeToDb\t\t\n"
                    + "4. DelPerfumeByID\t\texample: DelPerfumeByID 23\n"
                    + "5. GetProductData\n"
                    + "6. Exit\n"
                    + "Enter Option [1,6]";
            System.out.println("\n" + MENU_ITEMS);
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers

            socketWriter.println(command);

            Scanner socketReader = new Scanner(socket.getInputStream());

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
                        Perfume result = gsonParser.fromJson(res, Perfume.class);
                        display(result);
                    }
                }
                else if(command.equals("AddPerfumeToDb"))
                {
                    String details = enterPerfumeJSON();
                    socketWriter.println(details);
                    String res = socketReader.nextLine();

                    Perfume result = gsonParser.fromJson(res, Perfume.class);
                    System.out.println("Coming from server: Successfully added");
                    display(result);
                }

                else if(command.startsWith("DelPerfumeByID"))
                {
                    String res = socketReader.nextLine();

                    if(res == "NA")
                    {
                        System.out.println("Sorry something went wrong on our end :(");
                    }
                    else
                    {
                        System.out.println(res);
                    }

                }
                else if(command.equals("GetProductData"))
                {
                    socketWriter.println(command);
                    String res = socketReader.nextLine();

                    SummaryData data = gsonParser.fromJson(res, SummaryData.class);
                    System.out.println("Coming from server:");
                    displayData(data);
                }

                System.out.println("\n" + MENU_ITEMS);
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

    public void displayData(SummaryData s)
    {
        System.out.println("------------------------------");
        System.out.println("\tTotal Stock : " + s.getTotalStock());
        System.out.println("\tAmount of different products : " + s.getTotalProducts());
        System.out.println("------------------------------");
    }

    public String enterPerfume()
    {
        String brand = "";
        String name = "";
        int enteredSize = 0;
        float enteredPrice = -1;
        String gender  = "";
        int enteredStockLvl = -1;
        Scanner kb = new Scanner(System.in);
        String exportString = "AddPerfumeToDb";


        while(brand == "") {
            System.out.println("Please enter Perfume Brand: ");
            brand = kb.nextLine();
        };
        exportString = exportString + " " + brand;

        while(name == "") {
            System.out.println("Please enter Perfume Name: ");
            name = kb.nextLine();
        };
        exportString = exportString + " " + name;

        while(enteredSize < 1) {
            System.out.println("Please enter Perfume Size (ml): ");
            enteredSize = kb.nextInt();
        };
        exportString = exportString + " " + enteredSize;

        while(enteredPrice <= 0) {
            System.out.println("Please enter Perfume price: ");
            enteredPrice = kb.nextFloat();
        };
        exportString = exportString + " " + enteredPrice;

        while(gender == "") {
            System.out.println("Please enter Perfume gender: ");
            gender = kb.next();
        };
        exportString = exportString + " " + gender;

        while(enteredStockLvl <= 0) {
            System.out.println("Please enter Perfume Stock Level: ");
            enteredStockLvl = kb.nextInt();
        };
        exportString = exportString + " " + enteredStockLvl;

        return exportString;
    }

    public String enterPerfumeJSON()
    {

        Perfume perfume1 = new Perfume();

        String brand = "";
        String name = "";
        int enteredSize = 0;
        float enteredPrice = -1;
        String gender  = "";
        int enteredStockLvl = -1;
        Scanner kb = new Scanner(System.in);
        String exportString = "AddPerfumeToDb";


        while(brand == "") {
            System.out.println("Please enter Perfume Brand: ");
            brand = kb.nextLine();
        };
        perfume1.setBrand(brand);

        while(name == "") {
            System.out.println("Please enter Perfume Name: ");
            name = kb.nextLine();
        };
        perfume1.setName(name);

        while(enteredSize <= 0) {
            System.out.println("Please enter Perfume Size (ml): ");
            String temp = kb.next();
            try
            {
                enteredSize = Integer.parseInt(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };

        perfume1.setSize(enteredSize);

        while(enteredPrice <= 0) {
            System.out.println("Please enter Perfume price: ");
            String temp = kb.next();
            try
            {
                enteredPrice = Float.parseFloat(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };

        perfume1.setPrice(enteredPrice);

        while(gender == "") {
            System.out.println("Please enter Perfume gender: ");
            gender = kb.next();
        };
        perfume1.setGender(gender);

        while(enteredStockLvl <= 0) {
            System.out.println("Please enter Perfume Stock Level: ");
            String temp = kb.next();
            try
            {
                enteredStockLvl = Integer.parseInt(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };
        perfume1.setStockLvl(enteredStockLvl);

        Gson gsonParser = new Gson();   // instantiate a Gson Parser

        String result = gsonParser.toJson(perfume1);

        return result;
    }

}
