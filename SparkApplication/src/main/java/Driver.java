
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.sql.SparkSession;

import java.io.File;

public class Driver {
    public static void main(String[] args) {
        //set spark to run on local --- not on cluster
        SparkConf conf = new SparkConf().setAppName("FifaPlayersApacheSparkApplication").setMaster("local");
        //create new spark context  sc.
        JavaSparkContext sc = new JavaSparkContext(conf);


        // warehouseLocation points to the default location for managed databases and tables
        String warehouseLocation = new File("spark-warehouse").getAbsolutePath();
        SparkSession spark = SparkSession
                .builder()
                .appName("FifaPlayersApacheSparkApplication")
                .config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .getOrCreate();

    }
}
