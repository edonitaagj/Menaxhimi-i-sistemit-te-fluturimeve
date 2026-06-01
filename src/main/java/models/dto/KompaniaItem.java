package models.dto;

/**
 * Item për cmbKompania — shfaq emrin, ruan id.
 * Nëse VendiItem / AeroportiItem janë tashmë në projekt,
 * ky pattern është identik.
 */
public class KompaniaItem {

    private final int    id;
    private final String label;   // "W6 – Wizz Air"

    public KompaniaItem(int id, String kodiIata, String emriShkurter) {
        this.id    = id;
        this.label = kodiIata + " – " + emriShkurter;
    }

    public int    getId()    { return id; }
    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }  // ComboBox e shfaq këtë
}