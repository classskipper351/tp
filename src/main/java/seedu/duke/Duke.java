package seedu.duke;

public class Duke {

    private static final String name = "MathGenius";
    private static final Ui ui = new Ui(name);

    /**
     * Main entry-point for the java.duke.Duke application.
     */

    public static void run() {
        ui.greet();

        Storage.readFile();
        Ui.showLine();

        String command = ui.readCommand();

        while (!command.equals("exit")) {
            Parser.parse(command, ui);
            command = ui.readCommand();
        }
        ui.exit();
    }

    public static void main(String[] args) {
        assert true : "dummy assertion set to fail";
        run();
    }
}
