import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.*;


public class Driver {
     public static void main(String[] args) {
        String appName = "Fifa Players Spark Application";
        String csvFile = "fifaStats.csv";
        SparkConf conf = new SparkConf().setAppName(appName).setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        Encoder<Player> playerEncoder = Encoders.bean(Player.class);

        SparkSession spark = SparkSession
                .builder()
                .appName(appName)
                .getOrCreate();

        //read fifa stats in dataframes -- players dataset
        Dataset<Row> players_df = spark.read()
                .format("csv")
                .option("header","true")
                .load(csvFile);

        //read all fifa players countries in df
        Dataset<String> countries_df =  players_df.select(col("Nationality")).distinct().as(Encoders.STRING());

        //define countries Continents df
        Dataset<Row> countries_continents_df = spark.read()
                .format("csv")
                .option("header","true")
                .load("Countries-Continents.csv");


        //this contains all player details with their continent and countries
        Dataset<Player> all_players_df =  players_df
                .join(
                     countries_continents_df, //join column
                     players_df.col("Nationality").equalTo(countries_continents_df.col("Country")),//condition
                     "left" //join type
                )
                //select all columns with mapping on value and salary column to remove $MK, and save it as a new column called NumberValue/Salary
                .select(col("Name"),col("Age"),col("Nationality"),col("Continent"),col("Score"),col("Club"),col("Value"),col("Salary"),regexp_replace(col("Salary"),"[€MK]","").alias("NumSalary"),regexp_replace(col("Value"),"[€MK]","").alias("NumValue"))
                .as(playerEncoder);

//        all_players_df.show();


        //show number of players from each continent
//        all_players_df.groupBy(col("Continent")).count().show();
        //fasten future action on this dataset
        all_players_df.persist(StorageLevel.MEMORY_AND_DISK());


        /*
        *  we can take each player name with continent using filtering or pure sql command
        * the following part is using filter()
        * need to be written in separate CSV files
        */
        Dataset<Player> asia_players_df =  all_players_df.filter(col("Continent").equalTo("Asia")).as(playerEncoder);
        Dataset<Player> europe_players_df =  all_players_df.filter(col("Continent").equalTo("Europe")).as(playerEncoder);
        Dataset<Player> SA_players_df =  all_players_df.filter(col("Continent").equalTo("South America")).as(playerEncoder);
        Dataset<Player> NA_players_df =  all_players_df.filter(col("Continent").equalTo("North America")).as(playerEncoder);
        Dataset<Player> africa_players_df =  all_players_df.filter(col("Continent").equalTo("Africa")).as(playerEncoder);
        Dataset<Player> oceania_players_df =  all_players_df.filter(col("Continent").equalTo("Oceania")).as(playerEncoder);

        /*
        * we cam also do partitioning when writing ---> part 1
        */
        all_players_df.write().partitionBy("Continent").csv("ContinentsPlayers");



        //since we need to store 100GB we need to set spark.sql.files.maxPartitionBytes to define number of cores used in Cluster mode
    }
}
