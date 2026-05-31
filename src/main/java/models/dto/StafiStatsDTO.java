package models.dto;

public class StafiStatsDTO {
    private int totaliStafit;
    private int stafiAktiv;
    private int numriDepartamenteve;

    public StafiStatsDTO(int totaliStafit, int stafiAktiv, int numriDepartamenteve) {
        this.totaliStafit = totaliStafit;
        this.stafiAktiv = stafiAktiv;
        this.numriDepartamenteve = numriDepartamenteve;
    }

    public int getTotaliStafit() { return totaliStafit; }
    public int getStafiAktiv() { return stafiAktiv; }
    public int getNumriDepartamenteve() { return numriDepartamenteve; }
}