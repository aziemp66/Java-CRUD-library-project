package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {
    public static String ambiltahun () throws IOException {
        boolean tahunvalid = false;
        Scanner terminalinput = new Scanner(System.in);
        String tahuninput = terminalinput.nextLine();
        while (!tahunvalid) {
            try {
                Year.parse(tahuninput);
                tahunvalid = true;
            } catch (Exception e) {
                System.err.println("Format Tahun yang anda masukkan salah, format=(yyyy)");
                System.out.println("Sialahkan input ulang tahun");
                tahuninput = terminalinput.nextLine();
            }
        }
        return tahuninput;
    }
    public static boolean cekBukudiDatabase(String[] keywords, boolean isdisplay) throws IOException{


        FileReader fileReader = new FileReader("database.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String data = bufferedReader.readLine();
        boolean isexist = false;
        int nomordata = 0;
        if (isdisplay) {
            System.out.print("\n| No |\tTahun  |\tPenulis                |\tPenerbit               |\tJudul Buku\n");
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
        while (data != null){

            //cek keywords di dalam baris
            isexist = true;
            for (String keyword : keywords){
                isexist = isexist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            //jika keywords cocok maka ditampilkan
            if (isexist){
                if (isdisplay) {
                    nomordata++;
                    StringTokenizer stringTokenizer = new StringTokenizer(data,",");
                    stringTokenizer.nextToken();
                    System.out.printf("|%2d  ", nomordata);
                    System.out.printf("|\t%4s   ", stringTokenizer.nextToken());
                    System.out.printf("|\t%-20s   ", stringTokenizer.nextToken());
                    System.out.printf("|\t%-20s   ", stringTokenizer.nextToken());
                    System.out.printf("|\t%s   ", stringTokenizer.nextToken());
                    System.out.print("\n");

                } else {
                    break;
                }
            }

            data = bufferedReader.readLine();
        }
        if (isdisplay) {
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
        return isexist;
    }
    public static boolean getYesorNo(String Message) {
        Scanner terminalinput = new Scanner(System.in);
        System.out.println("\n"+Message+ "(y/n)");
        String pilihanuser = terminalinput.next();

        while (!pilihanuser.equalsIgnoreCase("y") && !pilihanuser.equalsIgnoreCase("n")){
            System.err.println("Pilihan anda bukan y atau n");
            System.out.println("\n"+Message+ "(y/n)");
            pilihanuser = terminalinput.next();
            clearscreen();
        }

        return pilihanuser.equalsIgnoreCase("y");
    }
    public static long ambilentrypertahun(String penulis, String tahun)throws IOException{
        FileReader fileReader = new FileReader("database.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        long entry = 0;
        String data = bufferedReader.readLine();
        Scanner datascanner;
        String primarykey;

        while (data != null){
            datascanner = new Scanner(data);
            datascanner.useDelimiter(",");
            primarykey = datascanner.next();
            datascanner = new Scanner(primarykey);
            datascanner.useDelimiter("_");

            penulis = penulis.replaceAll("\\s+","");

            if (penulis.equalsIgnoreCase(datascanner.next()) && tahun.equalsIgnoreCase(datascanner.next()) ){
                entry = datascanner.nextInt();
            }
            data = bufferedReader.readLine();
        }
        return entry;
    }
    public static void clearscreen(){
        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.println("\033\143");
            }
        } catch (Exception ex){
            System.err.println("Tidak bisa clear screen");
        }
    }
}
