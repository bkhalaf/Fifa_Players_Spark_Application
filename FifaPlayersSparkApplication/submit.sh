aws s3 cp  s3://harrijars jars/. --recursive
hadoop fs -put jars/fifaStats.csv  hdfs://ip-172-31-13-156.us-east-2.compute.internal:8020/user/hadoop/fifaStats.csv
hadoop fs -put jars/updatedPlayers.csv  hdfs://ip-172-31-13-156.us-east-2.compute.internal:8020/user/hadoop/updatedPlayers.csv
hadoop fs -put jars/Countries-Continents.csv  hdfs://ip-172-31-13-156.us-east-2.compute.internal:8020/user/hadoop/Countries-Continents.csv
git clone  https://github.com/MahmoodAbuAwwad/Fifa_Players_Spark_Application.git
cd /home/hadoop/Fifa_Players_Spark_Application/FifaPlayersSparkApplication
mvn clean
mvn compile
mvn install
cp -f /home/hadoop/Fifa_Players_Spark_Application/FifaPlayersSparkApplication/target/FifaPlayersSparkApplication-1.0-SNAPSHOT.jar FifaPlayersSparkApplication-1.0-SNAPSHOT.jar
aws s3 cp FifaPlayersSparkApplication-1.0-SNAPSHOT.jar s3://harrijars/FifaPlayersSparkApplication-1.0-SNAPSHOT.jar
spark-submit --master yarn --deploy-mode cluster --class Driver  FifaPlayersSparkApplication-1.0-SNAPSHOT.jar
