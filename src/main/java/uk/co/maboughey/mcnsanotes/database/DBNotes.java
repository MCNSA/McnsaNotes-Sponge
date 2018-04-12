package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.type.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class DBNotes {
    private PreparedStatement preparedStatement;
    private Connection connect;

    public boolean writeNote(Note note) {
        boolean ret = true;
        try {
            // load the database
            connect = DatabaseManager.getConnection();

            // write the statement
            preparedStatement = connect.prepareStatement("insert into notes (id, note, server, date, uuid, noter_uuid) values (NULL, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, note.note);
            preparedStatement.setString(2, note.server);
            preparedStatement.setDate(3, new java.sql.Date((new java.util.Date()).getTime()));
            preparedStatement.setString(4, note.notee);
            preparedStatement.setString(5, note.noteTaker);


            // and execute it!
            preparedStatement.executeUpdate();
        }
        catch(Exception e) {
            McnsaNotes.log.error("Database error writing note: "+e.getMessage());
            ret = false;
        }
        finally {
            DatabaseManager.close();
        }
        return ret;
    }

    public LinkedList<Note> getNotes(String target) {
        // begin storing the results
        LinkedList<Note> notes = new LinkedList<Note>();

        //Get uuid for target
        String UUID = DBuuid.getUUID(target);

        try {
            // load the database
            DatabaseManager.connect();

            // get the results
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM notes WHERE uuid=?");
            statement.setString(1, UUID);
            ResultSet results = statement.executeQuery();

            //Now we have the results, lets loop through and create notes
            while (results.next()) {
                Note note = new Note();
                note.id = results.getInt("id");
                note.note = results.getString("note");
                note.server = results.getString("server");
                note.noteDate = results.getDate("date");

                //Check if uuids are set
                if (results.getString("uuid") != null)
                    note.notee = results.getString("uuid");
                if (results.getString("noter_uuid") != null)
                    note.noteTaker = results.getString("noter_uuid");

                notes.add(note);
            }
        }
        catch(Exception e) {
            McnsaNotes.log.error("Database Error getting notes: "+e.getLocalizedMessage());
        }
        finally {
            DatabaseManager.close();
        }
        return notes;
    }
}
