import java.io.*;

public class RemoveLine {

    private String filePath;

    public RemoveLine(String filePath) {
        this.filePath = filePath;
    }

    public void removeFromJournalFile(String journalNameToRemove) {
        try {
            File inputFile = new File(filePath);
            File tempFile = new File("tempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            try {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    // Check if the line contains the journal name to be removed
                    if (currentLine.contains(journalNameToRemove)) {
                        continue; // Skip this line (remove it)
                    }
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            } finally {
                // Close the resources in the finally block to ensure proper cleanup
                writer.close();
                reader.close();
            }

            // Rename the temporary file to the original file name
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
