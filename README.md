### Building and running

* Create an executable jar (Java 9 required):

        mvn clean package

* Splitting 6-letter words (default):

        java -jar target/wordlist.jar src/test/data/wordlist_utf8.txt

* Splitting 5-letter words:

        java -jar target/wordlist.jar -l5 src/test/data/wordlist_utf8.txt
