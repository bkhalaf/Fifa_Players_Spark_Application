import net.minidev.json.JSONObject;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

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
        SparkConf conf = new SparkConf().setAppName(appName).setMaster("local");
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
        List<String> continentsNames= Arrays.asList("Africa","Asia","Antarctica","Europe","North America","South America","Australia");
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
                players_df.col("Nationality").equalTo(countries_continents_df.col("Country")), "left")
                .select(col("Name"),col("Continent"));
        //fasten future action on this dataset
        all_players_df.persist(StorageLevel.MEMORY_AND_DISK());

        //show number of players from each continent
        all_players_df.groupBy(col("Continent")).count().show();

    }
}
