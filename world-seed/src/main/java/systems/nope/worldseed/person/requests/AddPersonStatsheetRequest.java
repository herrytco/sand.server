package systems.nope.worldseed.person.requests;

public class AddPersonStatsheetRequest {
    private int personId;
    private int statsheetId;

    public AddPersonStatsheetRequest() {
    }

    public AddPersonStatsheetRequest(int personId, int statsheetId) {
        this.personId = personId;
        this.statsheetId = statsheetId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getStatsheetId() {
        return statsheetId;
    }

    public void setStatsheetId(int statsheetId) {
        this.statsheetId = statsheetId;
    }
}
