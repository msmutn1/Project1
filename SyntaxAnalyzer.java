import java.util.ArrayList;

public class SyntaxAnalyzer implements SyntaxInterface
{

    protected boolean errorFound;
    protected boolean found;
    ArrayList<String> vars = new ArrayList<String>();

    @Override
    public void innerItem() throws CompilerException {
        // Not used
    }

    /**
     * This method implements the BNF grammar rule for the document annotation.
     * @throws CompilerException
     */
    public void markdown() throws CompilerException
    {

        if(ProjectMain.currentToken.equals("#BEGIN"))
        {
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
        else
        {
            System.out.println("MKD file must start with #BEGIN and end with #END.");
        }

        if(!errorFound) variableDefine();
        if(!errorFound) head();
        if(!errorFound) body();

        if(ProjectMain.currentToken.equals("#END"))
        {
            ProjectMain.parsed.add(ProjectMain.currentToken);
            //ProjectMain.lexer.getNextToken();
        }
        else
        {
            if(!errorFound) System.out.println("MKD file must start with #BEGIN and end with #END.");
        }

        if(errorFound)
        {
            ProjectMain.errorLocated = true;
        }
    }

    /**
     * This method implements the BNF grammar rule for the head annotation.
     * @throws CompilerException
     */
    public void head() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("^"))
        {
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            if(!errorFound) title();

            if(!errorFound && ProjectMain.currentToken.equals("^"))
            {
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                System.out.println("Error on header. Use format ^ TEXT ^");
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the title annotation.
     * @throws CompilerException

     */
    public void title() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("<"))
        {
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals(">"))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals("#END"))
                {
                    System.out.println("Error on title. Use format < TEXT >");
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals(">"))
            {
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the body annotation.
     * @throws CompilerException
     */
    public void body() throws CompilerException
    {
        while(!ProjectMain.currentToken.equals("#END"))
        {
            found = false;
            if(!errorFound) paragraph();
            if(!errorFound) listitem();
            if(!errorFound) link();
            if(!errorFound) audio();
            if(!errorFound) video();
            if(!errorFound) variableUse();
            if(!errorFound) newline();
            if(!errorFound) innerText();

            if(!found)
            {
                System.out.println("Unrecognized token was spotted. Cancelling operation.");
                errorFound = true;
                break;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the paragraph annotation.
     * @throws CompilerException
     */
    public void paragraph() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("{"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals("}"))
            {
                if(!errorFound) variableDefine();
                if(!errorFound) listitem();
                if(!errorFound) link();
                if(!errorFound) audio();
                if(!errorFound) video();
                if(!errorFound) variableUse();
                if(!errorFound) newline();
                if(!errorFound) bold();
                if(!errorFound) italics();
                if(!errorFound) innerText();

                if(ProjectMain.currentToken.equals("#END")) // DOES NOT WORK
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("}"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                System.out.println("Error on paragraph. Use format { variable | list | audio | video | newline | bold | italics | text }");
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the inner-text annotation.
     * @throws CompilerException
     */
    public void innerText() throws CompilerException
    {
        if(ProjectMain.lexer.legalText(ProjectMain.currentToken.charAt(0)))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
    }

    /**
     * This method implements the BNF grammar rule for the variable-define annotation.
     * @throws CompilerException
     */
    public void variableDefine() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("$DEF"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals("="))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals("#END"))
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("="))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                errorFound = true;
            }

            while(!ProjectMain.currentToken.equals("$END"))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals("#END"))
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("$END"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                System.out.println("Error on variable definition. Use the format $DEF text = text $END");
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the variable-use annotation.
     * @throws CompilerException
     */
    public void variableUse() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("$USE"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals("$END"))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals("#END"))
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("$END"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                System.out.println("Error on variable usage. Use the format $USE variable $END");
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the bold annotation.
     * @throws CompilerException
     */
    public void bold() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("**"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals("**"))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals(""))
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("**"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the italics annotation.
     * @throws CompilerException
     */
    public void italics() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("*"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals("*"))
            {
                if(!errorFound) innerText();
                if(ProjectMain.currentToken.equals(""))
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals("*"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the listitem annotation.
     * @throws CompilerException
     */
    public void listitem() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("+"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();

            while(!ProjectMain.currentToken.equals(";"))
            {
                if(!errorFound) innerText();
                if(!errorFound) variableUse();

                if(ProjectMain.currentToken.equals("")) // DOES NOT WORK
                {
                    errorFound = true;
                    break;
                }
            }

            if(!errorFound && ProjectMain.currentToken.equals(";"))
            {
                found = true;
                ProjectMain.parsed.add(ProjectMain.currentToken);
                ProjectMain.lexer.getNextToken();
            }
            else
            {
                errorFound = true;
            }
        }
    }

    /**
     * This method implements the BNF grammar rule for the link annotation.
     * @throws CompilerException
     */
    public void link() throws CompilerException
    {
        if(ProjectMain.currentToken.charAt(0) == '[')
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
    }

    /**
     * This method implements the BNF grammar rule for the audio annotation.
     * @throws CompilerException
     */
    public void audio() throws CompilerException
    {
        if(ProjectMain.currentToken.charAt(0) == '@')
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
    }

    /**
     * This method implements the BNF grammar rule for the video annotation.
     * @throws CompilerException
     */
    public void video() throws CompilerException
    {
        if(ProjectMain.currentToken.charAt(0) == '%')
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
    }

    /**
     * This method implements the BNF grammar rule for the newline annotation.
     * @throws CompilerException
     */
    public void newline() throws CompilerException
    {
        if(ProjectMain.currentToken.equals("~"))
        {
            found = true;
            ProjectMain.parsed.add(ProjectMain.currentToken);
            ProjectMain.lexer.getNextToken();
        }
    }

    void setError(){errorFound = true;}
    void resetError(){errorFound = false;}
    boolean getError(){return errorFound;}

}