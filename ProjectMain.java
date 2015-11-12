

import java.io.*;
import java.util.ArrayList;

/**
 * Matthew Smutny
 * COSC455 Project 1
 * Coded in Intellij IDEA
 */
public class ProjectMain
{
    // Global Variables
    public static String currentToken;
    public static LexicalAnalyzer lexer;
    public static ArrayList<String> parsed;
    public static ArrayList<String> HTML;
    public static boolean errorLocated = false;

    // Main Class
    public static void main(String[] args)
    {
        // Local Variables
        String path = "";
        String contents = "";
        String line = "";
        BufferedReader br = null;
        lexer = new LexicalAnalyzer();
        SyntaxAnalyzer parser = new SyntaxAnalyzer();
        SemanticAnalyzer sem = new SemanticAnalyzer();
        parsed = new ArrayList<String>();
        HTML = new ArrayList<String>();

        // Make sure there are arguments
        if(args.length == 1)
        {
            path = args[0];
        }
        else
        {
            System.out.println("Incorrect arguments.");
            System.exit(1);
        }

        // Make sure .mkd file is passed in
        if(path.substring(path.lastIndexOf("."), path.length()).equals(".mkd"))
        {
            //System.out.println("MKD file.");
        }
        else
        {
            System.out.println("Not an MKD file.");
        }

        // Read the .mkd file
        File mkdfil = new File(path);
        try
        {
            br = new BufferedReader(new FileReader(mkdfil));

            while((line = br.readLine()) != null)
            {
                contents = contents + " " + line;
            }

            lexer.start(contents); // Pass contents to lexical analyzer
            try
            {
                parser.markdown(); // Check tokens for correct syntax
            }
            catch(CompilerException e)
            {
                System.out.println(e);
            }

            if(!errorLocated)
            {
                sem.createHTML(); // Generate HTML code
                File result = new File(path.substring(0, path.lastIndexOf(".")) + ".html");
                result.createNewFile();

                PrintWriter writer = new PrintWriter(result.getName(), "UTF-8");

                for(int i = 0; i < HTML.size(); i++)
                {
                    writer.println(HTML.get(i));
                }

                writer.close();

            }
            else
            {
                System.out.println("HTML File not generated.");
            }


        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
