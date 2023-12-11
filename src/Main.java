import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Irasykite is eiles, naudojant kablelius: Pavadinimas, Kaina, tt... ");

        // Read user input
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        // Split user input by commas
        String[] data = userInput.split(",");

        // Check if the input has the correct number of fields
        if (data.length == 5) {
            String ID = data[0].trim();
            String ZUR_ID = data[1].trim();
            String ZUR_DET_ID = data[2].trim();
            String Line_ID = data[3].trim();
            String DUOM = data[4].trim();

            // Create an instance of AddLine and add the line to the database
            AddLine addLine = new AddLine();
            addLine.addLineToDatabase(ID, ZUR_ID, ZUR_DET_ID,Line_ID, DUOM);

            System.out.println("Line added to the database.");
        } else {
            System.out.println("Invalid input format. Please provide four fields separated by commas.");
        }

        // Close the scanner to prevent resource leak
        scanner.close();
    }
}
