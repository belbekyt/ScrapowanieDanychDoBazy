import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.sql.Statement;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    private static Statement stmt;
    private static ResultSet results;
    public static void main(String[] args) throws IOException, SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Connection connect = Jsoup.connect("https://www.skatepro.com.pl/c84.htm");

        java.sql.Connection conn = DBConnection.createNewDBconnection();
        stmt = (conn).createStatement();

        Document document = connect.get();

        Elements allSkateboardNames = document.select(".product_name_trim");
        List<String> skateboardNamesCutted = new ArrayList<String>();
        for(Element elem: allSkateboardNames) {
            String str = elem.text();
            str = str.replace(" Deskorolka Kompletna", "");
            str = str.replace(" Deskorolka kompletna", "");
            str = str.replace(" Dla Dzieci", "");
            skateboardNamesCutted.add(str);
        }

        Elements allSkateboardPrices = document.select("span.gpn");
        List<String> skateboardPricesCutted = new ArrayList<String>();
        for(Element elem: allSkateboardPrices) {
            skateboardPricesCutted.add(elem.text());
        }

        List<Skateboard> skateboards = new ArrayList<Skateboard>();
        for(int i=0; i<skateboardPricesCutted.size()-1; i++){
            Skateboard skateboard = new Skateboard(skateboardNamesCutted.get(i), skateboardPricesCutted.get(i));
            skateboards.add(skateboard);
        }

        Scanner sc=new Scanner(System.in);
        System.out.println("-------MENU-------");
        System.out.println("Co chcesz zrobić?");
        System.out.println("1 - Usuń wszystkie dane z bazy");
        System.out.println("2 - Pobierz nowe dane do bazy (zalecane na początku)");
        System.out.println("3 - Wyświetl dane z bazy (domyślne w przypadku podania złej wartości)");
        int chose = sc.nextInt();

        if(chose==1){
            String sql_delete = "DELETE FROM skateboard;";
            PreparedStatement preparedStmt1 = conn.prepareStatement(sql_delete);
            preparedStmt1.execute();
        }
        else if(chose==2){
            Date dataStart = new Date();
            System.out.println("Rozpoczęcie Pobierania Danych: " + formatter.format(dataStart));

            for(int i=0; i<skateboards.size(); i++){
                String sql_insert = "INSERT INTO skateboard(name, price, data) VALUES(?,?,?);";
                PreparedStatement preparedStmt = conn.prepareStatement(sql_insert);
                preparedStmt.setString(1, skateboards.get(i).getName());
                preparedStmt.setString(2, skateboards.get(i).getPrice());
                Date date = new Date();
                preparedStmt.setString(3, formatter.format(date));
                preparedStmt.execute();
            }

            Date dataEnd = new Date();
            System.out.println("Zakończenie Pobierania Danych: " + formatter.format(dataEnd));
        }
        else{
            String sql_select = "SELECT * FROM skateboard;";
            PreparedStatement preparedStmt2 = conn.prepareStatement(sql_select);
            ResultSet rs = preparedStmt2.executeQuery();

            while (rs.next()) {
                System.out.println("-------"+rs.getString(1)+"-------");
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(" ");
            }
        }
    }
}