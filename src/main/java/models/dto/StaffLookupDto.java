package models.dto;

public class StaffLookupDto {
    private Integer id;
    private String label;

    public StaffLookupDto() {}

    public StaffLookupDto(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public String toString() {
        return label;
    }
}