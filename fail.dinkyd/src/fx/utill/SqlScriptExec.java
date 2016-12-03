package fx.utill;

import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dinkyd.</br>
 * <strong>/!\ Driver JDBC necessaire.</strong>
 */
public class SqlScriptExec {
    /**
     * Execute le fichier {@code File} sur la connecion {@code connection}
     * @param connection
     * @param file
     * @throws IOException
     * @throws SQLException
     */
    public static void execute(Connection connection, File file)
    throws IOException, SQLException {
System.out.println("Lecture de : " + file.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new
                FileReader(file));
        StringBuffer sqlBuf = new StringBuffer();
        String line;
        boolean statementReady = false;
        int count = 0;
        boolean declaringFunction = false;
        while ((line = reader.readLine()) != null) {
            // different continuation for oracle and postgres
            line = line.trim();

            if(line.contains("$$") || line.contains("$_$") || line.contains("$BODY$")) {
                declaringFunction = !declaringFunction;
            }
//___            //perso pour postgre!!
            if( line.contains("CREATE OR REPLACE")){
System.out.println("SQLScriptEngine.execute(): c'est une FUNCTION !!");
                declaringFunction = true;
            }

            if(line.equals("LANGUAGE 'plpgsql';")){

                sqlBuf.append(' ');
                sqlBuf.append(line);
                statementReady = true;
            }


//___ Gaffe au else if qui suis si tu enleve la chiote du dessus..

            else if (line.equals("--/exe/--"))   // execute finished statement for postgres
            {
                sqlBuf.append(' ');
                statementReady = true;
            }
            else if (line.equals("/")) // execute finished statement for oracle
            {
                sqlBuf.append(' ');
                statementReady = true;
            }
            else if (line.startsWith("--") || line.length() == 0)  // comment or empty
            {
                continue;
            }
            else if (!declaringFunction && line.endsWith(";"))
            {
                sqlBuf.append(' ');
                statementReady = true;
                sqlBuf.append(line.substring(0, line.length() - 1));
            }
            else
            {
                sqlBuf.append(' ');
                sqlBuf.append(line);
                statementReady = false;
            }
            if (statementReady) {
                if (sqlBuf.length() == 0) continue;
System.out.println(sqlBuf.toString());
                    executeQuery(connection, sqlBuf.toString().trim());
                    count ++;
                    sqlBuf.setLength(0);
                }
        }

        System.out.println("" + count + " statements processed");
        System.out.println("Import done sucessfully");

    }

    public static void executeQuery(Connection connection, String request) {
        Statement statement;
        try {
            System.out.println("Executing statement : " + request);
            statement = connection.createStatement();
            statement.execute(request.trim());
        }
        catch(PSQLException psqlE){
            System.err.println("SQLScriptEngine.executeQuery():\n\tSQLState = "
                    +psqlE.getSQLState()+"\n\t"+psqlE.getServerErrorMessage()
                    +"\n\tSur : requete =" + request
            );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
