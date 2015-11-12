import java.util.ArrayList;


public class SemanticAnalyzer
{
    // Generate HTML code
    public void createHTML()
    {
        String currentToken = "";
        int counter = 0;
        boolean ending = false;
        ArrayList<String> vars = new ArrayList<String>();

        for(int i = 0; i < ProjectMain.parsed.size(); i++)
        {
            if(ProjectMain.parsed.get(i).equals("#BEGIN")) ProjectMain.HTML.add("<HTML>");
            if(ProjectMain.parsed.get(i).equals("#END")) ProjectMain.HTML.add("</HTML>");
            if(ProjectMain.parsed.get(i).equals("<")) ProjectMain.HTML.add("<title>");
            if(ProjectMain.parsed.get(i).equals(">")) ProjectMain.HTML.add("</title>");
            if(ProjectMain.parsed.get(i).equals("{")) ProjectMain.HTML.add("<p>");
            if(ProjectMain.parsed.get(i).equals("}")) ProjectMain.HTML.add("</p>");

            if(ProjectMain.parsed.get(i).equals("$DEF"))
            {
                ProjectMain.HTML.add("</p>");
            }

            if(ProjectMain.parsed.get(i).equals("^"))
            {
                if(ending)
                {
                    ProjectMain.HTML.add("</head>");
                    ending = false;
                }
                else
                {
                    ProjectMain.HTML.add("<head>");
                    ending = true;
                }
            }

            if(ProjectMain.parsed.get(i).equals("**"))
            {
                if(ending)
                {
                    ProjectMain.HTML.add("</b>");
                    ending = false;
                }
                else
                {
                    ProjectMain.HTML.add("<b>");
                    ending = true;
                }
            }

            if(ProjectMain.parsed.get(i).equals("*"))
            {
                if(ending)
                {
                    ProjectMain.HTML.add("</i>");
                    ending = false;
                }
                else
                {
                    ProjectMain.HTML.add("<i>");
                    ending = true;
                }
            }

            if(ProjectMain.parsed.get(i).equals("+"))
            {

                counter = 1;
                currentToken = ProjectMain.parsed.get(i).toString();
                while(!currentToken.contains(";"))
                {
                    currentToken = currentToken + " " +  ProjectMain.parsed.get(i+counter).toString();
                    counter++;
                    i++;
                }
                ProjectMain.HTML.add("<ul>");
                ProjectMain.HTML.add("<li>" + currentToken.substring(1, currentToken.indexOf(";")) + "</li>");
                ProjectMain.HTML.add("</ul>");
                i++;
            }

            if(ProjectMain.parsed.get(i).equals("~"))
            {
                ProjectMain.HTML.add("<br>");
            }

            if(ProjectMain.parsed.get(i).toString().charAt(0) == '@')
            {
                counter = 1;
                currentToken = ProjectMain.parsed.get(i).toString();
                while(!currentToken.contains(")"))
                {
                    currentToken = currentToken + " " +  ProjectMain.parsed.get(i+counter).toString();
                    counter++;
                    i++;
                }
                ProjectMain.HTML.add("<audio controls>");
                ProjectMain.HTML.add("<source src=\""
                        + currentToken.substring(2, currentToken.length() - 1)
                        + "\" type=\"audio/"
                        + currentToken.substring(currentToken.lastIndexOf(".") + 1, currentToken.length() - 1)
                        + "\">");
                ProjectMain.HTML.add("</audio>");
                i++;
            }

            if(ProjectMain.parsed.get(i).toString().charAt(0) == '%')
            {
                counter = 0;
                currentToken = ProjectMain.parsed.get(i).toString();
                while(!currentToken.contains(")"))
                {
                    currentToken = currentToken + " " +  ProjectMain.parsed.get(i+counter).toString();
                    counter++;
                    i++;
                }
                ProjectMain.HTML.add("<iframe width=\"320\" height=\"240\" src=\""
                        + currentToken.substring(2, ProjectMain.parsed.get(i).toString().length() - 1)
                        + "\">");
                ProjectMain.HTML.add("</iframe>");
                i++;
            }

            if(ProjectMain.parsed.get(i).toString().charAt(0) == '[')
            {
                counter = 1;
                currentToken = ProjectMain.parsed.get(i).toString();
                while(!currentToken.contains("]"))
                {
                    currentToken = currentToken + " " +  ProjectMain.parsed.get(i+counter).toString();
                    counter++;
                    i++;
                }
                ProjectMain.HTML.add("<a href=\"" + currentToken.substring(currentToken.indexOf("(") + 1, currentToken.length() - 1) + "\">"
                        + currentToken.substring(1, currentToken.indexOf("]"))
                        + "</a>");
                i++;
            }

            if(ProjectMain.lexer.legalText(ProjectMain.parsed.get(i).toString().charAt(0)))
            {
                ProjectMain.HTML.add(ProjectMain.parsed.get(i).toString());
            }
        }
    }
}
