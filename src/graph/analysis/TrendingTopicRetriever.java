package graph.analysis;

import java.util.StringTokenizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import tweet.extractor.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

public class TrendingTopicRetriever {

    public static void main(String[] args) {

        try {
            Connection conn = Connector();
            //Statement stmt = conn.createStatement();

            //String query = "insert into trends (author_id,average,standard_dev, trend, c_value, dependency_coeff) value (" + au.getId() + ",-1,-1,-1,-1," + dc + ")";
            String query = "SELECT text FROM twitter.tweet";
            // System.out.println(tweet);

            //PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            //preparedStatement.setString(1, "words");
            ResultSet rs = preparedStatement.executeQuery();

            // retrieve the values of all returned rows and store them in a list
            List<String> words = new ArrayList<String>();
            while(rs.next())
                words.add(rs.getString(1));

            String joined = String.join(" ", words);

            StringTokenizer st1 = new StringTokenizer(joined, " \t\n\r\f,.:;?![]'");

            Map<String, Integer> freq = new HashMap<>();

            ArrayList<String> parts = new ArrayList<>();
            while (st1.hasMoreTokens()) {
                parts.add(st1.nextToken());
            }

            Set<String> hashsetList = new HashSet<>(parts);
            for(String item: hashsetList) {
                int count = 0;
                for (String item2 : parts) {
                    if (item.equals(item2)) {
                        count++;
                    }
                }
                freq.put(item, count);
                //System.out.println("Token " + item);
            }


            for (String key: freq.keySet()) {
                System.out.println("Token " + key + ": " + freq.get(key));
            }

            //stmt.close();
            }
        catch(SQLException e){
            System.out.println(e);
        }

    }

    public static Connection Connector() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/twitter?useUnicode=yes&characterEncoding=UTF8&" + "user=root&password=root");
            return conn;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }




    }

}
