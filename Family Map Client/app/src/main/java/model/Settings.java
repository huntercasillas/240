package model;

/**
 * Class that contains all information for settings.
 */
public class Settings {

    private String mapType, lifeStoryColor, familyTreeColor, spouseColor;
    private boolean lifeStoryStatus, familyTreeStatus, spouseStatus;

    /**
     * Constructor.
     */
    Settings() {
        lifeStoryStatus = true;
        familyTreeStatus = true;
        spouseStatus = true;
        mapType = "Normal";
        lifeStoryColor = "Red";
        familyTreeColor = "Green";
        spouseColor = "Blue";
    }

    public boolean getLifeStoryStatus() {
        return lifeStoryStatus;
    }

    public void setLifeStoryStatus(boolean lifeStoryStatus) {
        this.lifeStoryStatus = lifeStoryStatus;
    }

    public boolean getFamilyTreeStatus() {
        return familyTreeStatus;
    }

    public void setFamilyTreeStatus(boolean familyTreeStatus) {
        this.familyTreeStatus = familyTreeStatus;
    }

    public boolean getSpouseStatus() {
        return spouseStatus;
    }

    public void setSpouseStatus(boolean spouseStatus) {
        this.spouseStatus = spouseStatus;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getLifeStoryColor() {
        return lifeStoryColor;
    }

    public void setLifeStoryColor(String lifeStoryColor) {
        this.lifeStoryColor = lifeStoryColor;
    }

    public String getFamilyTreeColor() {
        return familyTreeColor;
    }

    public void setFamilyTreeColor(String familyTreeColor) {
        this.familyTreeColor = familyTreeColor;
    }

    public String getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseColor(String spouseColor) {
        this.spouseColor = spouseColor;
    }
}