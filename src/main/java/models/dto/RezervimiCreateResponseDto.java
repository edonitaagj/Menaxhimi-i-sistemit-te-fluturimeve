package models.dto;

public class RezervimiCreateResponseDto {
    private boolean success;
    private String message;
    private String kodiRezervimit;
    private Integer idRezervimit;

    public RezervimiCreateResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RezervimiCreateResponseDto(boolean success, String message, String kodiRezervimit, Integer idRezervimit) {
        this.success = success;
        this.message = message;
        this.kodiRezervimit = kodiRezervimit;
        this.idRezervimit = idRezervimit;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getKodiRezervimit() {
        return kodiRezervimit;
    }

    public Integer getIdRezervimit() {
        return idRezervimit;
    }
}