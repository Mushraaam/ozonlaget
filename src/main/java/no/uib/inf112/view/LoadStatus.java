package no.uib.inf112.view;

public class LoadStatus {
    private String currentStatus;
    private int percentComplete;

    public LoadStatus(){
        this.currentStatus = "Initiallizing...";
        this.percentComplete = 0;
    }

    /**
     * Sets the status for loadingscreen.
     * Sleeps for 100ms to allow the status to be rendered
     * @param newStatus
     * @param newPercent
     * @throws InterruptedException
     */
    public void setStatus(String newStatus, int newPercent){
        if (newStatus == null || newStatus.isBlank() || newPercent < percentComplete){
            return;
        }
        this.currentStatus = newStatus;
        this.percentComplete = newPercent;

        //Sleep so the new status actually has time to show
        //this is purely cosmetic and actually increases laod time
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the current loading activity
     */
    public String getStatus(){
        return this.currentStatus;
    }

    /**
     * @return rough percentage of completion
     * will look like: ??% - example: 10%
     */
    public String percentComplete(){
        return this.percentComplete + "%";
    }
}
