package com.raved.user.service;

/**
 * DTO for user statistics
 */
public class UserStatistics {

    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long suspendedUsers;
    private long bannedUsers;
    private long unverifiedUsers;
    private long usersCreatedToday;

    // Constructors
    public UserStatistics() {}

    public UserStatistics(long totalUsers, long activeUsers, long inactiveUsers, 
                         long suspendedUsers, long bannedUsers, long unverifiedUsers, 
                         long usersCreatedToday) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.inactiveUsers = inactiveUsers;
        this.suspendedUsers = suspendedUsers;
        this.bannedUsers = bannedUsers;
        this.unverifiedUsers = unverifiedUsers;
        this.usersCreatedToday = usersCreatedToday;
    }

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getActiveUsers() { return activeUsers; }
    public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }

    public long getInactiveUsers() { return inactiveUsers; }
    public void setInactiveUsers(long inactiveUsers) { this.inactiveUsers = inactiveUsers; }

    public long getSuspendedUsers() { return suspendedUsers; }
    public void setSuspendedUsers(long suspendedUsers) { this.suspendedUsers = suspendedUsers; }

    public long getBannedUsers() { return bannedUsers; }
    public void setBannedUsers(long bannedUsers) { this.bannedUsers = bannedUsers; }

    public long getUnverifiedUsers() { return unverifiedUsers; }
    public void setUnverifiedUsers(long unverifiedUsers) { this.unverifiedUsers = unverifiedUsers; }

    public long getUsersCreatedToday() { return usersCreatedToday; }
    public void setUsersCreatedToday(long usersCreatedToday) { this.usersCreatedToday = usersCreatedToday; }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "totalUsers=" + totalUsers +
                ", activeUsers=" + activeUsers +
                ", inactiveUsers=" + inactiveUsers +
                ", suspendedUsers=" + suspendedUsers +
                ", bannedUsers=" + bannedUsers +
                ", unverifiedUsers=" + unverifiedUsers +
                ", usersCreatedToday=" + usersCreatedToday +
                '}';
    }
}
