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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.*;


public class Driver {
     public static void main(String[] args) {
        String appName = "Fifa Players Spark Application";
        String csvFile = "fifaStats.csv";
        String updatedCsvFile = "updatedPlayers.csv";

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
                .select(col("Name"),col("Age"),col("Nationality"),col("Continent"),col("Score"),col("Club"),col("Value"),col("Salary"))
//        regexp_replace(col("Salary"),"[€MK]","").alias("NumSalary"),regexp_replace(col("Value"),"[€MK]","").alias("NumValue") --> to be put in select if I want to delete $MK from Salary and Value column
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
        * we cam also do partitioning when writing ---> part 2
        */
        all_players_df.write().mode(SaveMode.Overwrite).partitionBy("Continent").csv("ContinentsPlayers");

        //TODO: Write all players dataset to hive database.

         //        part 4
         //        Updated Dataset of Players -- Updates in Salaries;

        //read changed player file
        Dataset<Row> updated_players_part_df = spark.read()
                .format("csv")
                .option("header","true")
                .load(updatedCsvFile);

//        updated_players_part_df.show();

        // get dataset in Player type  -- merge it with continents
        Dataset<Player> updated_players_df =  updated_players_part_df
          .join(
                  countries_continents_df, //join column
                  updated_players_part_df.col("Nationality").equalTo(countries_continents_df.col("Country")),//condition
                  "left" //join type
          )
          .select(col("Name"),col("Age"),col("Nationality"),col("Continent"),col("Score"),col("Club"),col("Value"),col("Salary"))
          .as(playerEncoder);
        updated_players_df.persist(StorageLevel.MEMORY_AND_DISK());


        /*
        * Update Process
        * define updated values
        * subtract updated values from all values
        * get new updates
        * union new updated values with subtracted values
        */

//      Dataset<Player> notToUpdate = all_players_df.except(updated_players_df).sort(desc("Score"));
        Dataset<Player> notToUpdate = all_players_df.join(updated_players_df,
                all_players_df.col("Name").equalTo(updated_players_df.col("Name")), "leftanti")
                .sort(desc("Score"))
                .as(playerEncoder);
        notToUpdate.persist(StorageLevel.MEMORY_AND_DISK());
//        notToUpdate.show(30);
//        updated_players_df.show(22);

        Dataset<Player> all_updated_players_df = updated_players_df.union(notToUpdate).sort(desc("Score"));
//        all_updated_players_df.show(50);

        all_players_df=all_updated_players_df.coalesce(1); //get dataset in one partition to write in one csv file output,
        //this won’t trigger data shuffling across the nodes of the Spark Cluster.
        all_players_df.show(30);
        all_players_df.write().mode(SaveMode.Overwrite).partitionBy("Continent").csv("ContinentsPlayersUpdated");



        //TODO: Update Tables with new Values in updated Players Dataset -- Write Query
    }
}
