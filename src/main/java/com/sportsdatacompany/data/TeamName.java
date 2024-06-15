package com.sportsdatacompany.data;

public enum TeamName {
    MEXICO("Mexico"),
    CANADA("Canada"),
    SPAIN("Spain"),
    BRAZIL("Brazil"),
    GERMANY("Germany"),
    FRANCE("France"),
    URUGUAY("Uruguay"),
    ITALY("Italy"),
    ARGENTINA("Argentina"),
    AUSTRALIA("Australia");

    public final String teamName;

    TeamName(String teamName) {
        this.teamName = teamName;
    }

    public static boolean contains(String name) {
        for (TeamName teamName : TeamName.values()) {
            if (teamName.teamName.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
