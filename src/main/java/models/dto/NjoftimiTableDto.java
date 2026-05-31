package models.dto;

public class NjoftimiTableDto {
    private final int idNjoftimit;
    private final String tipi;
    private final String mesazhi;
    private final String dataNjoftimit;

    public NjoftimiTableDto(int idNjoftimit, String tipi, String mesazhi, String dataNjoftimit) {
        this.idNjoftimit = idNjoftimit;
        this.tipi = tipi;
        this.mesazhi = mesazhi;
        this.dataNjoftimit = dataNjoftimit;
    }

    public int getIdNjoftimit() {
        return idNjoftimit;
    }

    public String getTipi() {
        return tipi;
    }

    public String getMesazhi() {
        return mesazhi;
    }

    public String getDataNjoftimit() {
        return dataNjoftimit;
    }
}