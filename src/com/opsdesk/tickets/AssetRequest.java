package com.opsdesk.tickets;

public class AssetRequest extends Ticket {

    private String assetName;

    public AssetRequest(int id, String title, TicketStatus status, Priority priority, String assetName) {
        super(id, title, status, priority);
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }

    @Override
    public String describe() {
        return describeAs("Asset request", "asset: " + assetName);
    }
}
