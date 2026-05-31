package models.dto;

public class VendiItem {
    private final int    id;
    private final String emri;

    public VendiItem(int id, String emri) {
        this.id   = id;
        this.emri = emri;
    }

    public int    getId()   { return id; }
    public String getEmri() { return emri; }

    @Override
    public String toString() { return emri; } // ComboBox e shfaq këtë
}