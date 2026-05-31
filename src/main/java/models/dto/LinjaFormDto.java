package models.dto;

/**
 * DTO për formën e linjës (create + update).
 */
public class LinjaFormDto implements IRequestDto {

    private final int     idAeroportitNisjes;
    private final int     idAeroportitMbrrritjes;
    private final Integer distancaKm;
    private final Integer kohaFluturimitMin;
    private final boolean eshteAktive;

    public LinjaFormDto(int idAeroportitNisjes, int idAeroportitMbrrritjes,
                        Integer distancaKm, Integer kohaFluturimitMin,
                        boolean eshteAktive) {
        this.idAeroportitNisjes     = idAeroportitNisjes;
        this.idAeroportitMbrrritjes = idAeroportitMbrrritjes;
        this.distancaKm             = distancaKm;
        this.kohaFluturimitMin      = kohaFluturimitMin;
        this.eshteAktive            = eshteAktive;
    }

    public int     getIdAeroportitNisjes()     { return idAeroportitNisjes; }
    public int     getIdAeroportitMbrrritjes() { return idAeroportitMbrrritjes; }
    public Integer getDistancaKm()             { return distancaKm; }
    public Integer getKohaFluturimitMin()       { return kohaFluturimitMin; }
    public boolean isEshteAktive()             { return eshteAktive; }
}