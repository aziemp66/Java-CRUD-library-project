package CRUD;
//Import Java Library
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Operasi {
    public static void tampilkandata() throws IOException{
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader("database.txt");
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silakan tambah data terlebih dahulu");
            tambahdata();
            return;
        }
        String data = bufferedReader.readLine();
        int nomordata = 0;
        System.out.print("\n| No |\tTahun  |\tPenulis                         |\tPenerbit                        |\tJudul Buku\n");
        System.out.println("-------------------------------------------------------------------------------------------------------");

        while (data != null) {
            nomordata++;
            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

            stringTokenizer.nextToken();
            System.out.printf("|%2d  ", nomordata);
            System.out.printf("|\t%4s   ", stringTokenizer.nextToken());
            System.out.printf("|\t%-30s   ", stringTokenizer.nextToken());
            System.out.printf("|\t%-30s   ", stringTokenizer.nextToken());
            System.out.printf("|\t%s   ", stringTokenizer.nextToken());
            System.out.print("\n");

            data = bufferedReader.readLine();
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");


        bufferedReader.close();
        fileReader.close();
    }
    public static void caridata() throws IOException{
        //Membaca database ada atau tidak

        try {
            File file = new File("database.txt");
        } catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahdata();
            return;
        }
        //Kita ambil keyword dari user
        Scanner terminalinput = new Scanner(System.in);
        System.out.print("Masukkan kata kunci untuk mencari buku ! ");
        String caristring = terminalinput.nextLine();
        System.out.println(caristring);

        String[]  keywords = caristring.split("\\s+");

        //Kita cek keyword di database
        Utility.cekBukudiDatabase(keywords,true);
    }
    public static void updatedata() throws IOException {
        // kita ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilkandata();

        // ambil user input / pilihan data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan nomor buku yang akan diupdate: ");
        int updateNum = terminalInput.nextInt();

        // tampilkan data yang ingin diupdate

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data,",");

            // tampilkan entrycounts == updateNum
            if (updateNum == entryCounts){
                System.out.println("\nData yang ingin anda update adalah:");
                System.out.println("---------------------------------------");
                System.out.println("Referensi           : " + st.nextToken());
                System.out.println("Tahun               : " + st.nextToken());
                System.out.println("Penulis             : " + st.nextToken());
                System.out.println("Penerbit            : " + st.nextToken());
                System.out.println("Judul               : " + st.nextToken());

                // update data

                // mengambil input dari user

                String[] fieldData = {"tahun","penulis","penerbit","judul"};
                String[] tempData = new String[4];

                st = new StringTokenizer(data,",");
                String originalData = st.nextToken();

                for(int i=0; i < fieldData.length ; i++) {
                    boolean isUpdate = Utility.getYesorNo("apakah anda ingin merubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate){
                        //user input

                        if (fieldData[i].equalsIgnoreCase("tahun")){
                            System.out.print("masukan tahun terbit, format=(YYYY): ");
                            tempData[i] = Utility.ambiltahun();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                            tempData[i] = terminalInput.nextLine();
                        }

                    } else {
                        tempData[i] = originalData;
                    }
                }

                // tampilkan data baru ke layar
                st = new StringTokenizer(data,",");
                st.nextToken();
                System.out.println("\nData baru anda adalah ");
                System.out.println("---------------------------------------");
                System.out.println("Tahun               : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("Penulis             : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("Penerbit            : " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("Judul               : " + st.nextToken() + " --> " + tempData[3]);


                boolean isUpdate = Utility.getYesorNo("apakah anda yakin ingin mengupdate data tersebut");

                if (isUpdate){

                    // cek data baru di database
                    boolean isExist = Utility.cekBukudiDatabase(tempData,false);

                    if(isExist){
                        System.err.println("data buku sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        // copy data
                        bufferedOutput.write(data);

                    } else {

                        // format data baru kedalam database
                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

                        // kita bikin primary key
                        long nomorEntry = Utility.ambilentrypertahun(penulis, tahun) + 1;

                        String punulisTanpaSpasi = penulis.replaceAll("\\s+","");
                        String primaryKey = punulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;

                        // tulis data ke database
                        bufferedOutput.write(primaryKey + "," + tahun + ","+ penulis +"," + penerbit + ","+judul);
                    }
                } else {
                    // copy data
                    bufferedOutput.write(data);
                }
            } else {
                // copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }

        // menulis data ke file
        bufferedOutput.flush();
        bufferedOutput.close();
        fileOutput.close();
        bufferedInput.close();
        fileInput.close();

        System.gc();

        // delete original database
        database.delete();
        // rename file tempDB menjadi database
        tempDB.renameTo(database);
    }
    public static void deletedata() throws IOException {
        // kita ambil database original

        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        //Tampilkan data
        System.out.println("List Buku");
        tampilkandata();

        //Kita ambil user input untuk mendelete data
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nMasukkan nomor buku yang akan dihapus");
        int deleteNum = scanner.nextInt();

        //Looping untuk membaca tiap data baris dan skip data yang akan didelete
        int entrycount = 0;

        String data = bufferedInput.readLine();

        while (data!= null){
            entrycount++;
            boolean isdelete = false;

            StringTokenizer stringTokenizer = new StringTokenizer(data,",");
            //tampilkan data yang ingin di hapus
            if (deleteNum == entrycount){
                System.out.println("\nData yang ingin anda hapus adalah : ");
                System.out.println("----------------------------------------");
                System.out.println("Primary key      : "+stringTokenizer.nextToken());
                System.out.println("Tahun Terbit     : "+stringTokenizer.nextToken());
                System.out.println("Penulis          : "+stringTokenizer.nextToken());
                System.out.println("Judul            : "+stringTokenizer.nextToken());
                System.out.println("Penerbit         : "+stringTokenizer.nextToken());
                isdelete = Utility.getYesorNo("Apakah anda yakin akan menghapus data ini?");
            }
            if(isdelete){
                //skip pindahkan data dari original ke sementara
                System.out.println("Data berhasil dihapus");
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }
        //Menulis data ke file
        bufferedOutput.flush();
        bufferedInput.close();
        bufferedOutput.close();
        fileInput.close();
        fileOutput.close();

        System.gc();

        //delete original file
        database.delete();
        //rename file sementara ke database
        tempDB.renameTo(database);

    }
    public static void tambahdata() throws IOException{
        FileWriter fileWriter = new FileWriter("database.txt",true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //Mengambil input User
        Scanner terminalinput = new Scanner(System.in);
        String penulis,judul,penerbit,tahun;

        System.out.println("Masukkan nama Penulis :");
        penulis = terminalinput.nextLine();
        System.out.println("Masukkan judul buku : ");
        judul = terminalinput.nextLine();
        System.out.println("Masukkan nama penerbit : ");
        penerbit = terminalinput.nextLine();
        System.out.println("Masukkan tahun terbit, format=(yyyy) : ");
        tahun = Utility.ambiltahun();

        //Cek Buku di Database
        String[] keywords = {tahun+","+penulis+","+penerbit+","+judul};
        System.out.println(Arrays.toString(keywords));

        boolean isexist = Utility.cekBukudiDatabase(keywords,false);

        if(!isexist){
            System.out.println(Utility.ambilentrypertahun(penulis, tahun));
            long nomorentry = Utility.ambilentrypertahun(penulis, tahun) + 1;
            String penulistanpaspasi = penulis.replaceAll("\\s+","");
            String primarykey = penulistanpaspasi+"_"+tahun+"_"+nomorentry;
            System.out.println("\nData yang akan anda masukkan adalah");
            System.out.println("-----------------------------------------------");
            System.out.println("Primary key      : "+primarykey);
            System.out.println("Tahun Terbit     : "+tahun);
            System.out.println("Penulis          : "+penulis);
            System.out.println("Judul            : "+judul);
            System.out.println("Penerbit         : "+penerbit);

            boolean istambah = Utility.getYesorNo("Apakah anda ingin menambahkan data tersebut? ");

            if(istambah){
                bufferedWriter.write(primarykey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }

        } else {
            System.out.println("Buku yang anda masukkan sudah tersedia di data base dengan data berikut");
            Utility.cekBukudiDatabase(keywords, true);
        }
    }
}
