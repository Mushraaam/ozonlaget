package no.uib.inf112.view;

public class LoadStatus {
    private String currentStatus;
    private int percentComplete;

    public LoadStatus(){
        this.currentStatus = "Initiallizing...";
        this.percentComplete = 0;
    }

    public void setStatus(String newStatus, int newPercent){
        if (newStatus == null || newStatus.isBlank() || newPercent < percentComplete){
            return;
        }
        this.currentStatus = newStatus;
        this.percentComplete = newPercent;

    }

    public String getStatus(){
        return this.currentStatus;
    }

    public String percentComplete(){
        return this.percentComplete + "%";
    }
}
