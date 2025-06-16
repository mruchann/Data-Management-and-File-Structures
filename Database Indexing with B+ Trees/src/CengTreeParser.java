import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CengTreeParser
{

    private final static String regex = "^(\\d+)\\|([^|]+)\\|([^|]+)\\|([^|]+)$";
    private final static Pattern pattern = Pattern.compile(regex);

    private static CengBook parseBook(String line) {
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            Integer bookID = Integer.valueOf(matcher.group(1));
            String bookTitle = matcher.group(2);
            String author = matcher.group(3);
            String genre = matcher.group(4);
            return new CengBook(bookID, bookTitle, author, genre);
        }
        else {
            System.out.println("Parse error occurred!");
            return null;
        }
    }

    public static ArrayList<CengBook> parseBooksFromFile(String filename)
    {
        ArrayList<CengBook> bookList = new ArrayList<CengBook>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;

            // until the file finishes
            while ((line = bufferedReader.readLine()) != null) {
                CengBook cengbook = parseBook(line);
                if (cengbook != null) {
                    bookList.add(cengbook);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public static void startParsingCommandLine() throws IOException
    {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("add|")) {
                int beginIndex = "add|".length();
                CengBook cengBook = parseBook(line.substring(beginIndex));
                if (cengBook != null) {
                    CengBookRunner.addBook(cengBook);
                }
                continue;
            }

            if (line.startsWith(("search|"))) {
                int beginIndex = "search|".length();
                Integer key = Integer.valueOf(line.substring(beginIndex));
                CengBookRunner.searchBook(key);
                continue;
            }

            if (line.trim().equals("print")) {
                CengBookRunner.printTree();
                continue;
            }

            if (line.trim().equals("quit")) {
                break;
            }
        }

        scanner.close();

        // There are 4 commands:
        // 1) quit : End the app, gracefully. Print nothing, call nothing, just break off your command line loop.
        // 2) add : Parse and create the book, and call CengBookRunner.addBook(newlyCreatedBook).
        // 3) search : Parse the bookID, and call CengBookRunner.searchBook(bookID).
        // 4) print : Print the whole tree, call CengBookRunner.printTree().

        // Commands (quit, add, search, print) are case-insensitive.
    }
}
