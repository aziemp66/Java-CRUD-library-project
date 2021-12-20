package com.main;

//Import Java Library
import java.io.IOException;
import java.util.Scanner;
//Import CRUD Library
import CRUD.Operasi;
import CRUD.Utility;



public class Main {
    public static void main(String[] args) throws IOException {
        Scanner terminalinput = new Scanner(System.in);
        String pilihanuser;
        boolean islanjutkan = true;

        while (islanjutkan) {
            Utility.clearscreen();
            System.out.println("\nDatabase Perpustakaan\n");
            System.out.println("1.\tLihat seluruh buku");
            System.out.println("2.\tCari data Buku");
            System.out.println("3.\tTambah data buku");
            System.out.println("4.\tUbah data buku");
            System.out.println("5.\tHapus data buku");

            System.out.print("\n\nPilihan anda : ");
            pilihanuser = terminalinput.next();
            switch (pilihanuser) {
                case "1" -> {
                    System.out.println("\n=================");
                    System.out.println("LIST SELURUH BUKU");
                    System.out.println("=================");
                    Operasi.tampilkandata();
                }

                case "2" -> {
                    System.out.println("\n=================");
                    System.out.println("CARI BUKU");
                    System.out.println("=================");
                    Operasi.caridata();
                }

                case "3" -> {
                    System.out.println("\n=================");
                    System.out.println("TAMBAH DATA BUKU");
                    System.out.println("=================");
                    Operasi.tambahdata();
                    Operasi.tampilkandata();
                }

                case "4" -> {
                    System.out.println("\n=================");
                    System.out.println("UBAH DATA BUKU");
                    System.out.println("=================");
                    Operasi.updatedata();
                }

                case "5" -> {
                    System.out.println("\n=================");
                    System.out.println("HAPUS DATA BUKU");
                    System.out.println("=================");
                    Operasi.deletedata();
                }
                default -> System.err.println("\nInput anda tidak ditemukana\nSilahkan pilih 1 sampai 5");
            }

            islanjutkan = Utility.getYesorNo("Apakah anda ingin melanjutkan ? ");
        }

    }


}
