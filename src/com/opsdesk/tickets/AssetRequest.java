package com.opsdesk.tickets;

public class AssetRequest extends Ticket {

    private String assetName;

    public AssetRequest(int id, String title, String status, String priority, String assetName) {
        super(id, title, status, priority);
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }

    @Override
    public String describe() {
        return "Asset request #" + getId() + " [" + getPriority() + ", " + getStatus() + "]: "
                + getTitle() + " (asset: " + assetName + ")";
    }
}
