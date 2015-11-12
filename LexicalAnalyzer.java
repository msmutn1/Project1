public class LexicalAnalyzer implements LexicalInterface
{
    // Global Variables
    private String sourceLine;
    private char[] lexeme = new char [100];
    private char nextChar;
    private int lexLength;
    private int position;

    @Override
    public boolean isSpace(String c) {
        return false;
    }

    @Override
    public boolean lookupToken() {
        return false;
    }

    private String legal = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,:_!/";

    public boolean legalText(char c)
    {
        if(legal.contains(Character.toString(c))) return true;
        else return false;
    }

    // Entry point
    public void start(String line){
        sourceLine = line;
        position = 0;

        getCharacter();
        getNextToken();
    }

    // Sorts the characters into tokens
    public void getNextToken() {
        lexLength = 0;

        getNonBlank();
        addCharacter();
        getCharacter();

        while((nextChar != '\n') && (nextChar != ' ')){
            addCharacter();
            getCharacter();
        }

        String newToken = new String(lexeme);
        // Set new token
        ProjectMain.currentToken = newToken.substring(0, lexLength);
    }

    // Get next character
    public void getCharacter()
    {
        if (position < sourceLine.length()) nextChar = sourceLine.charAt(position++);
        else nextChar = '\n';
    }

    // Check for space
    public boolean isSpace(char c)
    {
        if (c == ' ') return true;
        else return false;
    }

    // Get next non-blank
    public void getNonBlank()
    {
        while(isSpace(nextChar))getCharacter();
    }

    // Add the current character to the token
    public void addCharacter()
    {
        if(lexLength <= 100000)
        {
            lexeme[lexLength++] = nextChar;
            lexeme[lexLength] = 0;
        }
        else
        {
            System.out.println("Lexeme too long.");

            if(!isSpace(nextChar))
            {
                while(!isSpace(nextChar))
                {
                    getCharacter();
                }
            }
            lexLength = 0;
            getNonBlank();
            addCharacter();

        }
    }
}
