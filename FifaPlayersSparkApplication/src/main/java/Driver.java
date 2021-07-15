import net.minidev.json.JSONObject;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;


public class Driver {
    public static void main(String[] args) {
        String appName = "Fifa Players Spark Application";
        String csvFile = "fifaStats.csv";
        SparkConf conf = new SparkConf().setAppName(appName).setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        SparkSession spark = SparkSession
                .builder()
                .appName(appName)
                .getOrCreate();

        //read fifa stats in dataframes -- players dataset
        Dataset<Row> players_df = spark.read()
                .format("csv")
                .option("header","true")
                .load(csvFile);
//        players_df.show(1000);

        //read all fifa players countries in df
        Dataset<String> countries_df =  players_df.select(col("Nationality")).distinct().as(Encoders.STRING());
//        countries_df.show(200);

        //define Continents df
        List<String> continentsNames= Arrays.asList("Africa","Asia","Oceania","Europe","North America","South America","Australia");
        Dataset<String> continents_df = spark.createDataset(continentsNames,Encoders.STRING());
//        continents_df.show();


        //define countries Continents df
        Dataset<Row> countries_continents_df = spark.read()
                .format("csv")
                .option("header","true")
                .load("Countries-Continents.csv");
//        countries_continents_df.show(1000);


        //this contains all player details with their continent and countries
        Dataset<Row> all_players_df =  players_df.join(countries_continents_df,
                players_df.col("Nationality").equalTo(countries_continents_df.col("Country")), "left");
        //show number of players from each continent
        all_players_df.groupBy(col("Continent")).count().show();


        //fasten future action on this dataset
        all_players_df.persist(StorageLevel.MEMORY_AND_DISK());


        /*
        *  we can take each player name with continent using filtering or pure sql command
        * the following part is using filter()
        * need to be written in separate CSV files
        */
        Dataset<Row> asia_players_df =  all_players_df.filter(col("Continent").equalTo("Asia")).select(col("Name"),col("Continent"));
        Dataset<Row> europe_players_df =  all_players_df.filter(col("Continent").equalTo("Europe")).select(col("Name"),col("Continent"));
        Dataset<Row> SA_players_df =  all_players_df.filter(col("Continent").equalTo("South America")).select(col("Name"),col("Continent"));
        Dataset<Row> NA_players_df =  all_players_df.filter(col("Continent").equalTo("North America")).select(col("Name"),col("Continent"));
        Dataset<Row> africa_players_df =  all_players_df.filter(col("Continent").equalTo("Africa")).select(col("Name"),col("Continent"));
        Dataset<Row> oceania_players_df =  all_players_df.filter(col("Continent").equalTo("Oceania")).select(col("Name"),col("Continent"));

//        asia_players_df.show();
//        europe_players_df.show();
//        SA_players_df.show();
//        NA_players_df.show();
//        africa_players_df.show();
//        oceania_players_df.show();

        /*
        * we cam also do partitioning when writing
        */
//        all_players_df.write().partitionBy("Continent").saveAsTable("PlayersContinent");

        

        //since we need to store 100GB we need to set spark.sql.files.maxPartitionBytes to define number of cores used in Cluster mode
    }
}
