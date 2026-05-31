package models.dto;

public class AeroportiItem {
    private final int    id;
    private final String label;   // "PRN – Adem Jashari"

    public AeroportiItem(int id, String kodiIata, String emri) {
        this.id    = id;
        this.label = kodiIata + " – " + emri;
    }

    public int    getId()    { return id; }
    public String getLabel() { return label; }

    @Override
    public String toString() { return label; } // ComboBox e shfaq këtë
}