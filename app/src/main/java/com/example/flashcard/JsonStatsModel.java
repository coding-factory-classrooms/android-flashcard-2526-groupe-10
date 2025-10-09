package com.example.flashcard;

public class JsonStatsModel {
    private int totalGames;       // nombre total de quiz joués
    private int totalCorrect;     // nombre total de bonnes réponses
    private int totalWrong;       // nombre total de mauvaises réponses
    private long totalTime;       // temps total de jeu en millisecondes

    // getters et setters pour chaque champ
    public int getTotalGames() { return totalGames; }
    public void setTotalGames(int totalGames) { this.totalGames = totalGames; }

    public int getTotalCorrect() { return totalCorrect; }
    public void setTotalCorrect(int totalCorrect) { this.totalCorrect = totalCorrect; }

    public int getTotalWrong() { return totalWrong; }
    public void setTotalWrong(int totalWrong) { this.totalWrong = totalWrong; }

    public long getTotalTime() { return totalTime; }
    public void setTotalTime(long totalTime) { this.totalTime = totalTime; }


    // Méthode pour calculer le temps moyen passé par partie
    public float getAverageTime() {
        return totalGames == 0 ? 0 : (float) totalTime / totalGames;
    }

}
