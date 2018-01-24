### Building and running

* Create an executable jar (Java 9 required):

        mvn clean package

* Viewing the online help:

        java -jar target/wordlist.jar --help

* Splitting 6-letter words (default):

        java -jar target/wordlist.jar testdata/wordlist_utf8.txt

* Splitting 5-letter words:

        java -jar target/wordlist.jar -n5 testdata/wordlist_utf8.txt

* Writing to file, instead of stdout:

        java -jar target/wordlist.jar testdata/wordlist_utf8.txt output.txt

* Reading from stdin:

        echo -e "foo\nbar\nfoobar" | java -jar target/wordlist.jar -

* Using a different input encoding:

        java -jar target/wordlist.jar --input-charset=ISO-8859-1 testdata/wordlist.txt
